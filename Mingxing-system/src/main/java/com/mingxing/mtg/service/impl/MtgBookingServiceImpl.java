package com.mingxing.mtg.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mingxing.common.utils.spring.WsEventPublisher;
import com.mingxing.common.exception.ServiceException;
import com.mingxing.mtg.domain.MtgBooking;
import com.mingxing.mtg.domain.MtgRoom;
import com.mingxing.mtg.domain.MtgServiceDict;
import com.mingxing.mtg.mapper.MtgBookingMapper;
import com.mingxing.mtg.service.IMtgBookingService;
import com.mingxing.mtg.service.IMtgRoomService;
import com.mingxing.mtg.service.IMtgServiceDictService;
import com.mingxing.mtg.mail.SmtpMailService;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MtgBookingServiceImpl implements IMtgBookingService {

    private static final Logger log = LoggerFactory.getLogger(MtgBookingServiceImpl.class);

    @Autowired
    private MtgBookingMapper bookingMapper;

    @Autowired
    private IMtgServiceDictService serviceDictService;

    @Autowired
    private IMtgRoomService roomService;

    @Autowired
    private SmtpMailService smtpMailService;

    @Value("${mail.smtp.to:}")
    private String mailTo;

    @Override
    public MtgBooking selectBookingById(Long bookingId) {
        return bookingMapper.selectBookingById(bookingId);
    }

    @Override
    public List<MtgBooking> selectBookingList(MtgBooking booking) {
        return bookingMapper.selectBookingList(booking);
    }

    @Override
    public List<MtgBooking> selectBookingByDateRange(Date startDate, Date endDate) {
        return bookingMapper.selectBookingByDateRange(startDate, endDate);
    }

    @Override
    public boolean checkTimeConflict(MtgBooking booking) {
        return bookingMapper.checkTimeConflict(booking) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBooking(MtgBooking booking) {
        int rows = bookingMapper.insertBooking(booking);
        if (rows > 0 && booking.getBookingId() != null) {
            MtgBooking saved = bookingMapper.selectBookingById(booking.getBookingId());
            MtgBooking toSend = saved != null ? saved : booking;
            sendServiceNotifyEmail(toSend);
            broadcastBookingChange("create", toSend);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBookingChecked(MtgBooking booking) {
        if (booking.getRoomId() == null) {
            throw new ServiceException("请选择会议室");
        }
        MtgRoom room = roomService.selectRoomByIdForUpdate(booking.getRoomId());
        if (room == null) {
            throw new ServiceException("会议室不存在或已停用");
        }
        if (bookingMapper.checkTimeConflict(booking) > 0) {
            throw new ServiceException("该时间段已被预约，请选择其他时间");
        }
        return insertBooking(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBooking(MtgBooking booking) {
        return bookingMapper.updateBooking(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelBooking(MtgBooking booking) {
        booking.setBookingStatus("3");
        int rows = bookingMapper.updateBooking(booking);
        if (rows > 0) {
            MtgBooking cancelled = bookingMapper.selectBookingById(booking.getBookingId());
            broadcastBookingChange("cancel", cancelled != null ? cancelled : booking);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int auditBooking(MtgBooking booking) {
        return bookingMapper.updateBooking(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBookingHidden(Long bookingId, Long hiddenBy) {
        return bookingMapper.updateBookingHidden(bookingId, hiddenBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBookingByIds(Long[] bookingIds) {
        return bookingMapper.deleteBookingByIds(bookingIds);
    }

    /**
     * 检查预约的服务项中是否有需要邮件通知的服务，若有则发送通知邮件（异步执行）
     */
    @Async("mailExecutor")
    public void sendServiceNotifyEmail(MtgBooking booking) {
        try {
            String serviceItems = booking.getServiceItems();
            if (serviceItems == null || serviceItems.trim().isEmpty()) {
                return;
            }
            boolean needNotify = false;
            StringBuilder needServices = new StringBuilder();
            String[] items = serviceItems.split(",");
            for (String item : items) {
                String name = item.trim();
                if (name.isEmpty()) {
                    continue;
                }
                MtgServiceDict service = serviceDictService.selectServiceDictByName(name);
                if (service != null && "1".equals(service.getEmailNotify())) {
                    needNotify = true;
                    if (needServices.length() > 0) {
                        needServices.append("、");
                    }
                    needServices.append(name);
                }
            }
            if (!needNotify || mailTo == null || mailTo.isEmpty()) {
                return;
            }
            String subject = "会议预约服务需求通知";
            StringBuilder body = new StringBuilder();
            body.append("您好，有一条新的会议预约需要关注服务需求：\n\n");
            body.append("预约主题：").append(booking.getSubject()).append("\n");
            String roomName = booking.getRoomName();
            if ((roomName == null || roomName.isEmpty()) && booking.getRoomId() != null) {
                MtgRoom room = roomService.selectRoomById(booking.getRoomId());
                if (room != null) {
                    roomName = room.getRoomName();
                }
            }
            body.append("会议室：").append(roomName == null ? "" : roomName).append("\n");
            body.append("预约类型：").append("day".equals(booking.getBookingType()) ? "全天" : "按时段").append("\n");
            body.append("预约日期：").append(booking.getBookingDate() == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(booking.getBookingDate())).append("\n");
            body.append("预约时段：").append(booking.getStartTime() == null ? "" : new java.text.SimpleDateFormat("HH:mm").format(booking.getStartTime()))
                    .append(" - ").append(booking.getEndTime() == null ? "" : new java.text.SimpleDateFormat("HH:mm").format(booking.getEndTime())).append("\n");
            body.append("预约人：").append(booking.getBookerName()).append("\n");
            body.append("部门：").append(booking.getDeptName()).append("\n");
            body.append("服务需求：").append(needServices).append("\n\n");
            body.append("请及时安排相关服务，谢谢！");
            boolean ok = smtpMailService.sendTextMail(mailTo, subject, body.toString());
            log.info("预约服务通知邮件发送结果: bookingId={}, ok={}", booking.getBookingId(), ok);
        } catch (Exception e) {
            log.error("预约服务通知邮件发送异常: bookingId={}, error={}", booking.getBookingId(), e.getMessage(), e);
        }
    }

    private void broadcastBookingChange(String action, MtgBooking booking) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);
        payload.put("bookingId", booking.getBookingId());
        payload.put("roomId", booking.getRoomId());
        payload.put("roomName", booking.getRoomName());
        payload.put("bookerName", booking.getBookerName());
        payload.put("bookingStatus", booking.getBookingStatus());
        WsEventPublisher.publish("roomBookingChange", payload);
    }
}
