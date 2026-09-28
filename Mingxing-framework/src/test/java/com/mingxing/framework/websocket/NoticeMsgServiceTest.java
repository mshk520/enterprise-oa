package com.mingxing.framework.websocket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mingxing.system.domain.SysNoticeMsg;
import com.mingxing.system.service.ISysNoticeMsgService;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeMsgService 测试")
class NoticeMsgServiceTest {

    @Mock
    private ISysNoticeMsgService sysNoticeMsgService;

    @InjectMocks
    private NoticeMsgService noticeMsgService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("sendCarApplyNotice - 新建申请通知审批者")
    void sendCarApplyNotice_newApply_notifiesAdmins() {
        Long applyId = 100L;
        String applicantLoginName = "zhangsan";
        String applicantDisplay = "张三";

        noticeMsgService.sendCarApplyNotice(applyId, applicantLoginName, applicantDisplay);

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }

    @Test
    @DisplayName("sendCarApplyNotice - 兼容旧调用（无 loginName）")
    void sendCarApplyNotice_oldCall_notifiesAdmins() {
        noticeMsgService.sendCarApplyNotice(100L, "张三");

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }

    @Test
    @DisplayName("sendCarApplyAuditNotice - 审批通过通知申请人")
    void sendCarApplyAuditNotice_approved_notifiesApplicant() {
        Long applyId = 100L;
        String applicantName = "张三";
        String createBy = "zhangsan";
        String status = "1";
        String auditRemark = "同意";

        noticeMsgService.sendCarApplyAuditNotice(applyId, applicantName, createBy, status, auditRemark);

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }

    @Test
    @DisplayName("sendCarApplyAuditNotice - 审批拒绝通知申请人")
    void sendCarApplyAuditNotice_rejected_notifiesApplicant() {
        Long applyId = 100L;
        String applicantName = "张三";
        String createBy = "zhangsan";
        String status = "2";
        String auditRemark = "车辆不足";

        noticeMsgService.sendCarApplyAuditNotice(applyId, applicantName, createBy, status, auditRemark);

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }

    @Test
    @DisplayName("sendCarApplyAuditNotice - 无审批意见时默认文案")
    void sendCarApplyAuditNotice_noRemark_defaultText() {
        noticeMsgService.sendCarApplyAuditNotice(100L, "张三", "zhangsan", "1", null);

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }

    @Test
    @DisplayName("sendCarApplyAuditNotice - createBy 为空时不发 WebSocket（但入库仍执行）")
    void sendCarApplyAuditNotice_emptyCreateBy_noWebSocket() {
        noticeMsgService.sendCarApplyAuditNotice(100L, "张三", "", "1", "同意");

        verify(sysNoticeMsgService).insertNoticeMsg(any(SysNoticeMsg.class));
    }
}