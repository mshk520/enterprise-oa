import Vue from 'vue'
import VueI18n from 'vue-i18n'
import ElementLocale from 'element-ui/lib/locale'
import elementEnLocale from 'element-ui/lib/locale/lang/en'
import elementZhTWLocale from 'element-ui/lib/locale/lang/zh-TW'
// 使用 require 避免 webpack tree-shaking 丢失翻译 key
const zhTW = require('./zh-TW').default
const en = require('./en').default

Vue.use(VueI18n)

// 清洗消息对象：确保所有翻译值为字符串，防止 null/undefined 导致 _interpolate 报错
function sanitizeMessages(obj, prefix = '') {
  const result = {}
  for (const key of Object.keys(obj)) {
    const val = obj[key]
    const fullKey = prefix ? `${prefix}.${key}` : key
    if (val === null || val === undefined) {
      console.warn(`[i18n] Sanitizing null/undefined value for key: ${fullKey}`)
      result[key] = ''
    } else if (typeof val === 'object' && val !== null) {
      result[key] = sanitizeMessages(val, fullKey)
    } else if (typeof val === 'string') {
      result[key] = val
    } else {
      console.warn(`[i18n] Converting non-string value to string for key: ${fullKey}`)
      result[key] = String(val)
    }
  }
  return result
}

// 把 menu 命名空间提升到顶层，兼容 $t(title) 直接查找
// 注意：menu 里的 key（如 fixedBooking/roomManage/deviceManage/serviceManage）
// 与顶层业务命名空间对象重名，若直接展开会用菜单标题字符串覆盖整个命名空间，
// 导致这些页面 $t('xxx.xxx') 全部 miss。因此只提升顶层不存在的菜单 key。
function mergeMenuToTop(dict) {
  const merged = { ...dict }
  if (dict.menu && typeof dict.menu === 'object') {
    Object.keys(dict.menu).forEach(k => {
      if (!Object.prototype.hasOwnProperty.call(merged, k)) {
        merged[k] = dict.menu[k]
      }
    })
  }
  return merged
}

const messages = {
  'zh-TW': sanitizeMessages({
    ...mergeMenuToTop(zhTW),
    el: elementZhTWLocale.el
  }),
  'en': sanitizeMessages({
    ...mergeMenuToTop(en),
    el: elementEnLocale.el
  })
}

const i18n = new VueI18n({
  locale: 'zh-TW', // 强制默认中文，避免 localStorage 干扰
  messages,
  silentTranslationWarn: true,
  // 防止翻译值为 null/undefined 导致 _interpolate 报错
  missing: (locale, key) => {
    console.warn(`[i18n] Missing translation key: ${key} (locale: ${locale})`)
    return key // 返回 key 本身，保证是字符串
  }
})

// 让 Element UI 组件（分页、日期选择器、表格空数据等）跟随 vue-i18n 语言切换
ElementLocale.i18n((key, value) => i18n.t(key, value))

// 调试：监听 locale 变化
i18n.watchLocale(() => {
  console.log(`[i18n] Locale changed to: ${i18n.locale}`)
  localStorage.setItem('lang', i18n.locale)
})

// 终极保护：重写 Vue.prototype.$t 确保永远返回字符串
const originalT = Vue.prototype.$t
Vue.prototype.$t = function(key, values) {
  try {
    const result = originalT.apply(this, arguments)
    if (result === null || result === undefined) return key
    if (typeof result !== 'string') return String(result)
    return result
  } catch (e) {
    console.warn(`[i18n] $t error for key: ${key}`, e)
    return key
  }
}

// 保活：运行时读取所有翻译 key，防止 webpack/terser 静态分析误删（项目大量使用动态 $t(key)）
function keepAllKeys() {
  let count = 0
  Object.keys(messages).forEach(locale => {
    Object.keys(messages[locale]).forEach(ns => {
      if (ns === 'el') return
      Object.keys(messages[locale][ns]).forEach(k => {
        const v = messages[locale][ns][k]
        if (typeof v === 'string') count += v.length
      })
    })
  })
  return count
}
window.__i18nKeysKept = keepAllKeys()

export default i18n
