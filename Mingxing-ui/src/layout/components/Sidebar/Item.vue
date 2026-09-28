<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const { icon, title } = context.props
    const vnodes = []

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon}/>)
    }

    if (title) {
      const i18n = context.parent.$i18n
      let displayTitle = title
      if (i18n && i18n.te('menu.' + title)) {
        displayTitle = i18n.t('menu.' + title)
      }
      if (displayTitle.length > 5) {
        vnodes.push(<span slot='title' title={(displayTitle)}>{(displayTitle)}</span>)
      } else {
        vnodes.push(<span slot='title'>{(displayTitle)}</span>)
      }
    }
    return vnodes
  }
}
</script>
