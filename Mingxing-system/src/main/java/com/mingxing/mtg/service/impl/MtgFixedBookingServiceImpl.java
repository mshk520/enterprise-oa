package com.mingxing.mtg.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mingxing.common.utils.StringUtils;
import com.mingxing.mtg.domain.MtgBooking;
import com.mingxing.mtg.domain.MtgFixedBooking;
import com.mingxing.mtg.mapper.MtgBookingMapper;
import com.mingxing.mtg.mapper.MtgFixedBookingMapper;
import com.mingxing.mtg.mapper.MtgRoomMapper;
import com.mingxing.mtg.service.IMtgFixedBookingService;

@Service
public class MtgFixedBookingServiceImpl implements IMtgFixedBookingService {

    @Autowired
    private MtgFixedBookingMapper fixedBookingMapper;

    @Autowired
    private MtgBookingMapper bookingMapper;

    @Autowired
    private MtgRoomMapper roomMapper;

    @Override
    public MtgFixedBooking selectFixedBookingById(Long fixedId) {
        MtgFixedBooking fixed = fixedBookingMapper.selectFixedBookingById(fixedId);
        if (fixed != null) {
            List<Long> roomIds = fixedBookingMapper.selectRoomIdsByFixedId(fixedId);
            fixed.setRoomIds(roomIds);
            fixed.setRoomNames(getRoomNames(roomIds));
        }
        return fixed;
    }

    @Override
    public List<MtgFixedBooking> selectFixedBookingList(MtgFixedBooking fixedBooking) {
        List<MtgFixedBooking> list = fixedBookingMapper.selectFixedBookingList(fixedBooking);
        for (MtgFixedBooking f : list) {
            List<Long> roomIds = fixedBookingMapper.selectRoomIdsByFixedId(f.getFixedId());
            f.setRoomIds(roomIds);
            f.setRoomNames(getRoomNames(roomIds));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFixedBooking(MtgFixedBooking fixedBooking) {
        int rows = fixedBookingMapper.insertFixedBooking(fixedBooking);
        if (fixedBooking.getRoomIds() != null && !fixedBooking.getRoomIds().isEmpty()) {
            fixedBookingMapper.insertRoomBatch(fixedBooking.getFixedId(), fixedBooking.getRoomIds());
        }
        generateBookings(fixedBooking);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFixedBooking(MtgFixedBooking fixedBooking) {
        fixedBookingMapper.deleteGeneratedBookingsByFixedId(fixedBooking.getFixedId());
        fixedBookingMapper.deleteRoomByFixedId(fixedBooking.getFixedId());
        if (fixedBooking.getRoomIds() != null && !fixedBooking.getRoomIds().isEmpty()) {
            fixedBookingMapper.insertRoomBatch(fixedBooking.getFixedId(), fixedBooking.getRoomIds());
        }
        generateBookings(fixedBooking);
        return fixedBookingMapper.updateFixedBooking(fixedBooking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFixedBookingByIds(Long[] fixedIds) {
        for (Long fixedId : fixedIds) {
            fixedBookingMapper.deleteGeneratedBookingsByFixedId(fixedId);
            fixedBookingMapper.deleteRoomByFixedId(fixedId);
        }
        return fixedBookingMapper.deleteFixedBookingByIds(fixedIds);
    }

    private void generateBookings(MtgFixedBooking fixed) {
        if (fixed.getRoomIds() == null || fixed.getRoomIds().isEmpty()) {
            return;
        }
        if (fixed.getStartDate() == null || fixed.getEndDate() == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fixed.getStartDate());

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(fixed.getEndDate());

        String startDateTime = "";
        String endDateTime = "";

        while (!cal.after(endCal)) {
            boolean match = false;
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            if ("daily".equals(fixed.getRecurrenceType())) {
                match = true;
            } else if ("weekly".equals(fixed.getRecurrenceType())) {
                int javaDay = dayOfWeek;
                int userDay = (javaDay == Calendar.SUNDAY) ? 7 : javaDay - 1;
                match = (fixed.getRecurrenceDay() != null && userDay == fixed.getRecurrenceDay());
            } else if ("monthly".equals(fixed.getRecurrenceType())) {
                match = (fixed.getRecurrenceDay() != null && dayOfMonth == fixed.getRecurrenceDay());
            }

            if (match) {
                String dateStr = String.format("%tF", cal.getTime());
                startDateTime = dateStr + " " + fixed.getStartTime() + ":00";
                endDateTime = dateStr + " " + fixed.getEndTime() + ":00";

                for (Long roomId : fixed.getRoomIds()) {
                    MtgBooking booking = new MtgBooking();
                    booking.setRoomId(roomId);
                    booking.setSubject(fixed.getBookingTitle());
                    booking.setBookingDate(cal.getTime());
                    booking.setBookingType("hour");
                    booking.setStartTime(parseDateTime(startDateTime));
                    booking.setEndTime(parseDateTime(endDateTime));
                    booking.setBookerId(fixed.getBookerId() != null ? fixed.getBookerId() : fixed.getCreateBy() != null ? 1L : null);
                    booking.setDeptId(fixed.getDeptId());
                    booking.setAttendees(fixed.getAttendees());
                    booking.setContactPhone(fixed.getContactPhone());
                    booking.setMeetingLink(fixed.getMeetingLink());
                    booking.setMeetingPassword(fixed.getMeetingPassword());
                    booking.setServiceItems(fixed.getServiceItems());
                    booking.setBookingStatus("5");
                    booking.setFixedId(fixed.getFixedId());
                    booking.setCreateBy(fixed.getCreateBy());
                    bookingMapper.insertBooking(booking);
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private Date parseDateTime(String dateTimeStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateTimeStr);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getRoomNames(List<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<String> names = new java.util.ArrayList<>();
        for (Long roomId : roomIds) {
            com.mingxing.mtg.domain.MtgRoom room = roomMapper.selectRoomById(roomId);
            if (room != null) {
                names.add(room.getRoomName());
            }
        }
        return names;
    }
}
