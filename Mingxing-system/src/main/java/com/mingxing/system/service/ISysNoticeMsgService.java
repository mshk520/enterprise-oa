package com.mingxing.system.service;
import java.util.List;
import com.mingxing.system.domain.SysNoticeMsg;
public interface ISysNoticeMsgService {
    List<SysNoticeMsg> selectNoticeMsgList(SysNoticeMsg msg);
    SysNoticeMsg selectNoticeMsgById(Long msgId);
    int insertNoticeMsg(SysNoticeMsg msg);
    int updateNoticeMsg(SysNoticeMsg msg);
    int deleteNoticeMsgById(Long msgId);
    int deleteNoticeMsgByIds(Long[] msgIds);
    int selectUnreadCount(String receiver);
    int selectUnreadCountByReceivers(List<String> receivers);
    List<SysNoticeMsg> selectNoticeMsgListByReceivers(List<String> receivers);
    int markAllAsRead(String receiver);
    int markAllAsReadByReceivers(List<String> receivers);
}