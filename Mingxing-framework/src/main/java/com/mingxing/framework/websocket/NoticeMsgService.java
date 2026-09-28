package com.mingxing.framework.websocket;

import com.mingxing.system.domain.SysNoticeMsg;
import com.mingxing.system.service.ISysNoticeMsgService;
import com.alibaba.fastjson2.JSONObject;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoticeMsgService {
    @Autowired
    private ISysNoticeMsgService sysNoticeMsgService;

    /**
     * 提交用车申请 → 通知审批者（管理员）
     *
     * @param applyId           申请ID
     * @param applicantLoginName 申请人真实用户名（loginName=SysUser.username），用于排除自己不收到审批弹窗
     * @param applicantDisplay  申请人展示姓名/昵称，用于消息正文显示
     */
    public void sendCarApplyNotice(Long applyId, String applicantLoginName, String applicantDisplay) {
        String display = applicantDisplay != null && !applicantDisplay.isEmpty()
                ? applicantDisplay
                : (applicantLoginName != null ? applicantLoginName : "员工");

        SysNoticeMsg msg = new SysNoticeMsg();
        msg.setMsgTitle("新的用车申请待审批");
        msg.setMsgContent("申请人：" + display + " 提交了用车申请，请及时审批");
        msg.setMsgType("1");
        msg.setNoticeType("1");
        msg.setBusinessId(applyId);
        msg.setBusinessType("car:apply");
        msg.setReceiverType("2");
        msg.setReceiver("car:apply:audit");
        msg.setCreateBy(applicantLoginName != null ? applicantLoginName : display);
        sysNoticeMsgService.insertNoticeMsg(msg);

        JSONObject payload = new JSONObject();
        payload.put("type", "carApply");
        payload.put("msgId", msg.getMsgId());
        payload.put("title", msg.getMsgTitle());
        payload.put("content", msg.getMsgContent());
        payload.put("applyId", applyId);
        payload.put("applicantName", display);
        payload.put("time", System.currentTimeMillis());

        // 只推给有车管申请审批权限的人（car:apply:audit），只有真正的审批者才能收到
        // 同时排除申请人本人，防止自己提交自己弹审核框
        NoticeWebSocket.broadcastByPermission(
                "car:apply:audit",
                payload.toJSONString(),
                applicantLoginName != null && !applicantLoginName.isEmpty()
                        ? Collections.singleton(applicantLoginName)
                        : Collections.emptySet()
        );
    }

    /** 兼容旧调用方：仅传展示名时无法精准排除 loginName，仅按权限推送给管理员 */
    public void sendCarApplyNotice(Long applyId, String applicantDisplay) {
        sendCarApplyNotice(applyId, null, applicantDisplay);
    }

    /**
     * 用车申请审批结果 → 只推给申请人本人
     *
     * @param applyId         申请ID
     * @param applicantName   申请人展示姓名
     * @param createBy        申请人 loginName（真实用户名，必填，用于精准推送）
     * @param status          1 通过 / 其他 驳回
     * @param auditRemark     审批意见
     */
    public void sendCarApplyAuditNotice(Long applyId, String applicantName, String createBy,
            String status, String auditRemark) {
        boolean approved = "1".equals(status);
        SysNoticeMsg msg = new SysNoticeMsg();
        msg.setMsgTitle(approved ? "用车申请已通过" : "用车申请已驳回");
        msg.setMsgContent(approved
                ? "您的用车申请已审批通过，" + (auditRemark != null && !auditRemark.isEmpty() ? "审批意见：" + auditRemark : "请查看详情")
                : "您的用车申请已被驳回，" + (auditRemark != null && !auditRemark.isEmpty() ? "驳回原因：" + auditRemark : "如有疑问请联系管理员"));
        msg.setMsgType("1");
        msg.setNoticeType("2");
        msg.setBusinessId(applyId);
        msg.setBusinessType("car:apply");
        msg.setReceiverType("1");
        msg.setReceiver(createBy);
        msg.setCreateBy("admin");
        sysNoticeMsgService.insertNoticeMsg(msg);

        JSONObject payload = new JSONObject();
        payload.put("type", "carApplyAudit");
        payload.put("msgId", msg.getMsgId());
        payload.put("title", msg.getMsgTitle());
        payload.put("content", msg.getMsgContent());
        payload.put("applyId", applyId);
        payload.put("applicantName", applicantName);
        payload.put("createBy", createBy);
        payload.put("status", status);
        payload.put("time", System.currentTimeMillis());

        // 只推给申请人本人（所有端：Web + App 同时在线都能收到），不再 broadcast 给所有人
        if (createBy != null && !createBy.isEmpty()) {
            NoticeWebSocket.sendToLoginName(createBy, payload.toJSONString());
        }
    }

    /**
     * 审批通过 → 通知保安（有 car:duty:operate 权限的人）
     *
     * @param applyId        申请ID
     * @param applicantName  申请人姓名
     * @param plateNumber    车牌号
     * @param driverName     司机姓名
     */
    public void sendCarApplyApprovedNotice(Long applyId, String applicantName,
            String plateNumber, String driverName) {
        String content = applicantName + " 的用车申请已审批通过";
        if (plateNumber != null && !plateNumber.isEmpty()) {
            content += "，车辆 " + plateNumber;
        }
        if (driverName != null && !driverName.isEmpty()) {
            content += "，司机 " + driverName;
        }
        content += "，请安排出厂";

        SysNoticeMsg msg = new SysNoticeMsg();
        msg.setMsgTitle("用车申请已审批 - 待出厂");
        msg.setMsgContent(content);
        msg.setMsgType("1");
        msg.setNoticeType("1");
        msg.setBusinessId(applyId);
        msg.setBusinessType("car:duty");
        msg.setReceiverType("2");
        msg.setReceiver("car:duty:operate");
        msg.setCreateBy("admin");
        sysNoticeMsgService.insertNoticeMsg(msg);

        JSONObject payload = new JSONObject();
        payload.put("type", "carApplyApproved");
        payload.put("msgId", msg.getMsgId());
        payload.put("title", msg.getMsgTitle());
        payload.put("content", msg.getMsgContent());
        payload.put("applyId", applyId);
        payload.put("applicantName", applicantName);
        payload.put("plateNumber", plateNumber);
        payload.put("time", System.currentTimeMillis());

        // 推给所有有 car:duty:operate 权限的在线用户（保安）
        NoticeWebSocket.broadcastByPermission(
                "car:duty:operate",
                payload.toJSONString(),
                null
        );
    }
}
