import { Message, MessageBox, Notification, Loading } from 'element-ui'
import i18n from '@/lang'

let loadingInstance

function t(key) {
  return i18n.t(key)
}

export default {
  // 消息提示
  msg(content) {
    Message.info(content)
  },
  // 错误消息
  msgError(content) {
    Message.error(content)
  },
  // 成功消息
  msgSuccess(content) {
    Message.success(content)
  },
  // 警告消息
  msgWarning(content) {
    Message.warning(content)
  },
  // 弹出提示
  alert(content) {
    MessageBox.alert(content, t('modal.title'))
  },
  // 错误提示
  alertError(content) {
    MessageBox.alert(content, t('modal.title'), { type: 'error' })
  },
  // 成功提示
  alertSuccess(content) {
    MessageBox.alert(content, t('modal.title'), { type: 'success' })
  },
  // 警告提示
  alertWarning(content) {
    MessageBox.alert(content, t('modal.title'), { type: 'warning' })
  },
  // 通知提示
  notify(content) {
    Notification.info(content)
  },
  // 错误通知
  notifyError(content) {
    Notification.error(content)
  },
  // 成功通知
  notifySuccess(content) {
    Notification.success(content)
  },
  // 警告通知
  notifyWarning(content) {
    Notification.warning(content)
  },
  // 确认窗体
  confirm(content) {
    return MessageBox.confirm(content, t('modal.title'), {
      confirmButtonText: t('modal.confirm'),
      cancelButtonText: t('modal.cancel'),
      type: "warning",
    })
  },
  // 提交内容
  prompt(content) {
    return MessageBox.prompt(content, t('modal.title'), {
      confirmButtonText: t('modal.confirm'),
      cancelButtonText: t('modal.cancel'),
      type: "warning",
    })
  },
  // 打开遮罩层
  loading(content) {
    loadingInstance = Loading.service({
      lock: true,
      text: content,
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    })
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.close()
  }
}
