package com.mingxing.system.service.impl;
import java.util.List;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.system.domain.SysNoticeMsg;
import com.mingxing.system.mapper.SysNoticeMsgMapper;
import com.mingxing.system.service.ISysNoticeMsgService;
@Service
public class SysNoticeMsgServiceImpl implements ISysNoticeMsgService {
    @Autowired
    private SysNoticeMsgMapper sysNoticeMsgMapper;
    @Override public List<SysNoticeMsg> selectNoticeMsgList(SysNoticeMsg msg) { return sysNoticeMsgMapper.selectNoticeMsgList(msg); }
    @Override public SysNoticeMsg selectNoticeMsgById(Long msgId) { return sysNoticeMsgMapper.selectNoticeMsgById(msgId); }
    @Override public int insertNoticeMsg(SysNoticeMsg msg) { return sysNoticeMsgMapper.insertNoticeMsg(msg); }
    @Override public int updateNoticeMsg(SysNoticeMsg msg) { return sysNoticeMsgMapper.updateNoticeMsg(msg); }
    @Override public int deleteNoticeMsgById(Long msgId) { return sysNoticeMsgMapper.deleteNoticeMsgById(msgId); }
    @Override public int deleteNoticeMsgByIds(Long[] msgIds) { return sysNoticeMsgMapper.deleteNoticeMsgByIds(msgIds); }
    @Override public int selectUnreadCount(String receiver) { return sysNoticeMsgMapper.selectUnreadCount(receiver); }
    @Override public int selectUnreadCountByReceivers(List<String> receivers) { return sysNoticeMsgMapper.selectUnreadCountByReceivers(receivers); }
    @Override public List<SysNoticeMsg> selectNoticeMsgListByReceivers(List<String> receivers) { return sysNoticeMsgMapper.selectNoticeMsgListByReceivers(receivers); }
    @Override public int markAllAsRead(String receiver) { return sysNoticeMsgMapper.markAllAsRead(receiver); }
    @Override public int markAllAsReadByReceivers(List<String> receivers) { return sysNoticeMsgMapper.markAllAsReadByReceivers(receivers); }
}