package com.mingxing.web.controller.system;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.core.domain.model.LoginUser;
import com.mingxing.common.utils.SecurityUtils;
import com.mingxing.system.domain.SysNoticeMsg;
import com.mingxing.system.service.ISysNoticeMsgService;
@RestController
@RequestMapping("/system/noticeMsg")
public class SysNoticeMsgController extends BaseController {
    @Autowired
    private ISysNoticeMsgService sysNoticeMsgService;

    /** 本系统中作为消息接收者的权限字符串 */
    private static final String[] MSG_RECEIVER_PERMS = {
        "car:apply:audit", "car:duty:operate"
    };

    private List<String> buildReceivers() {
        LoginUser user = SecurityUtils.getLoginUser();
        String username = user.getUsername();
        List<String> receivers = new ArrayList<>();
        receivers.add(username);
        for (String perm : MSG_RECEIVER_PERMS) {
            if (user.getPermissions() != null && user.getPermissions().contains(perm)) {
                receivers.add(perm);
            }
        }
        return receivers;
    }

    @GetMapping("/list")
    public AjaxResult list() {
        List<SysNoticeMsg> list = sysNoticeMsgService.selectNoticeMsgListByReceivers(buildReceivers());
        return success(list);
    }
    @GetMapping("/count")
    public AjaxResult unreadCount() {
        return success(sysNoticeMsgService.selectUnreadCountByReceivers(buildReceivers()));
    }
    @PutMapping("/read/{msgId}")
    public AjaxResult read(@PathVariable Long msgId) {
        SysNoticeMsg msg = new SysNoticeMsg();
        msg.setMsgId(msgId);
        msg.setIsRead("1");
        msg.setUpdateBy(getUsername());
        return toAjax(sysNoticeMsgService.updateNoticeMsg(msg));
    }
    @PutMapping("/readAll")
    public AjaxResult readAll() {
        return toAjax(sysNoticeMsgService.markAllAsReadByReceivers(buildReceivers()));
    }
}