<template>
  <div class="login-wrapper">
    <div class="bg-logo-left">
      <img :src="bgLogoLeft" alt="logo" />
    </div>
    <div class="bg-p-right">
      <img :src="bgPRight" alt="P" />
    </div>
    <transition name="login-fade" appear>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <div class="login-header">
        <img v-if="logoImg" :src="logoImg" class="logo-img" />
        <h3 class="title">{{title}}</h3>
        <div class="lang-switch">
          <span :class="{ active: $i18n.locale === 'zh-TW' }" @click="setLang('zh-TW')">中</span>
          <span class="lang-divider">/</span>
          <span :class="{ active: $i18n.locale === 'en' }" @click="setLang('en')">EN</span>
        </div>
      </div>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          autocomplete="off"
          :placeholder="$t('login.username')"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          autocomplete="new-password"
          :placeholder="$t('login.password')"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item v-if="captchaEnabled">
        <SliderVerify ref="slider" @success="onSliderSuccess" />
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">{{ $t('login.rememberMe') }}</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">{{ $t('login.login') }}</span>
          <span v-else>{{ $t('login.logging') }}</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">{{ $t('login.register') }}</router-link>
        </div>
      </el-form-item>
    </el-form>
    </transition>
  </div>
</template>

<script>
import SliderVerify from '@/components/SliderVerify/index.vue'
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'
import logoImg from '@/assets/logo/logo.png'
import bgLogoLeft from '@/assets/images/login-logo-left.png'
import bgPRight from '@/assets/images/login-p-right.png'

export default {
  name: "Login",
  components: { SliderVerify },
  data() {
    return {
      sliderVerified: false,
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      logoImg: logoImg,
      bgLogoLeft: bgLogoLeft,
      bgPRight: bgPRight,
      loginForm: {
        username: "",
        password: "",
        rememberMe: false
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ]
      },
      loading: false,
      captchaEnabled: true,
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCookie()
  },
  methods: {
    onSliderSuccess() {
      this.sliderVerified = true
    },
    getCookie() {
      const username = Cookies.get("username")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: "",
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      if (this.captchaEnabled && !this.sliderVerified) {
        this.$message.warning(this.$t('login.slideHint') || 'Please slide to verify')
        return
      }
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          const formData = { ...this.loginForm }
          this.$store.dispatch("Login", formData).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            this.sliderVerified = false
            if (this.$refs.slider) {
              this.$refs.slider.reset()
            }
          })
        }
      })
    },
    setLang(lang) {
      this.$i18n.locale = lang
      localStorage.setItem('lang', lang)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  background: #19365d;
  position: relative;
  overflow: hidden;
}
.bg-logo-left {
  position: absolute;
  left: 40px;
  top: 30px;
  z-index: 1;
  img {
    height: 50px;
    width: auto;
    display: block;
  }
}
.bg-p-right {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 55%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  z-index: 1;
  overflow: hidden;
  img {
    width: auto;
    height: 85%;
    max-height: 700px;
    display: block;
    filter: brightness(1.5) contrast(0.8);
  }
}
.login-form {
  position: relative;
  z-index: 10;
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 36px 32px 12px 32px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.2);
  .login-header {
    text-align: center;
    margin-bottom: 8px;
    .logo-img {
      height: 40px;
      margin-bottom: 16px;
    }
  }
  .lang-switch {
    margin-bottom: 30px;
    font-size: 13px;
    color: #acb7b7;
    cursor: default;
    span {
      cursor: pointer;
      transition: color 0.2s;
      &:hover { color: #5777ba; }
    }
    .active {
      color: #5777ba;
      font-weight: 600;
      cursor: default;
    }
    .lang-divider {
      margin: 0 6px;
      cursor: default;
      color: #d1d5db;
      &:hover { color: #d1d5db; }
    }
  }
  .title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #19365d;
    font-size: 18px;
    font-weight: 700;
    letter-spacing: 1px;
  }
  .el-input {
    height: 40px;
    input {
      height: 40px;
      border: 1px solid #d1d5db;
      border-radius: 4px;
      &:focus {
        border-color: #5777ba;
        box-shadow: 0 0 0 3px rgba(87, 119, 186, 0.15);
      }
    }
  }
  .input-icon {
    height: 41px;
    width: 14px;
    margin-left: 2px;
    color: #9ca3af;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #a0aec0;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
    border-radius: 4px;
    border: 1px solid #d1d5db;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
}

.login-fade-enter-active {
  transition: opacity 0.6s cubic-bezier(0.22, 1, 0.36, 1), transform 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}
.login-fade-enter {
  opacity: 0;
  transform: translateY(30px);
}
</style>