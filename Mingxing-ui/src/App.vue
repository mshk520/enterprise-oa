<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker"
import noticeSocket from "@/utils/noticeSocket"

export default {
  name: "App",
  components: { ThemePicker },
  watch: {
    // 登录状态变化时全局连接/断开通知推送（覆盖包括 404 在内的所有页面）
    "$store.getters.token": {
      immediate: true,
      handler(token) {
        if (token) {
          noticeSocket.connect()
        } else {
          noticeSocket.close()
        }
      }
    }
  }
}
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
