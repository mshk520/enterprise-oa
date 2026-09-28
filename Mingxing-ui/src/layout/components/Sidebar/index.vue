<template>
    <div :class="['sidebar-theme-wrapper', {'has-logo':showLogo}, settings.sideTheme]" :style="{ 
    backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
    width: currentSidebarWidth 
  }">
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>
        <div v-if="!isCollapse" class="sidebar-resize-handle" @mousedown="onResizeStart"></div>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import Logo from "./Logo"
import SidebarItem from "./SidebarItem"
import variables from "@/assets/styles/variables.scss"

export default {
    components: { SidebarItem, Logo },
    data() {
        return {
            isResizing: false,
            startX: 0,
            startWidth: 0,
            currentSidebarWidth: localStorage.getItem('sidebar-width')
                ? parseInt(localStorage.getItem('sidebar-width')) + 'px'
                : variables.sideBarWidth
        }
    },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar"]),
        activeMenu() {
            const route = this.$route
            const { meta, path } = route
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu
            }
            return path
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo
        },
        variables() {
            return variables
        },
        isCollapse() {
            return !this.sidebar.opened
        }
    },
    watch: {
        isCollapse(val) {
            const mainContainer = document.querySelector('.main-container')
            const fixedHeader = document.querySelector('.fixed-header')
            if (val) {
                // Collapsing: clear inline styles so CSS .hideSidebar rules apply
                if (mainContainer) mainContainer.style.marginLeft = ''
                if (fixedHeader) fixedHeader.style.width = ''
            } else {
                // Expanding: if user had a custom width, reapply it
                const savedWidth = localStorage.getItem('sidebar-width')
                if (savedWidth) {
                    const w = parseInt(savedWidth)
                    if (!isNaN(w) && w >= 150 && w <= 400) {
                        this.currentSidebarWidth = w + 'px'
                        if (mainContainer) mainContainer.style.marginLeft = w + 'px'
                        if (fixedHeader) fixedHeader.style.width = `calc(100% - ${w}px)`
                    }
                }
            }
        }
    },
    mounted() {
        // Apply saved width on mount
        this.applySavedWidth()
        // Listen for escape key to cancel resizing
        document.addEventListener('keydown', this.onKeyDown)
    },
    beforeDestroy() {
        // Clean up event listeners
        document.removeEventListener('keydown', this.onKeyDown)
    },
    methods: {
        applySavedWidth() {
            const savedWidth = localStorage.getItem('sidebar-width')
            if (savedWidth) {
                const width = parseInt(savedWidth)
                if (!isNaN(width) && width >= 100) { // Min width 100px
                    this.updateSidebarWidth(width)
                }
            }
        },
        updateSidebarWidth(width) {
            // Update the reactive width property
            this.currentSidebarWidth = width + 'px'
            
            // Update the main container margin to accommodate the new width
            const mainContainer = document.querySelector('.main-container')
            if (mainContainer) {
                mainContainer.style.marginLeft = width + 'px'
            }
            
            // Update fixed header width if applicable
            const fixedHeader = document.querySelector('.fixed-header')
            if (fixedHeader) {
                fixedHeader.style.width = `calc(100% - ${width}px)`
            }
            
            // Save width to localStorage
            localStorage.setItem('sidebar-width', width.toString())
        },
        onResizeStart(e) {
            if (this.isCollapse) return
            
            this.isResizing = true
            this.startX = e.clientX
            const sidebarContainer = this.$el
            if (sidebarContainer) {
                const computedStyle = window.getComputedStyle(sidebarContainer)
                this.startWidth = parseInt(computedStyle.width)
            }
            
            // Add event listeners for dragging
            document.addEventListener('mousemove', this.onResizeMove)
            document.addEventListener('mouseup', this.onResizeEnd)
            
            // Change cursor to indicate resizing
            document.body.style.cursor = 'col-resize'
            e.preventDefault()
        },
        onResizeMove(e) {
            if (!this.isResizing) return
            
            const dx = e.clientX - this.startX
            let newWidth = this.startWidth + dx
            
            // Constrain width between 150px and 400px
            newWidth = Math.max(150, Math.min(400, newWidth))
            
            this.updateSidebarWidth(newWidth)
        },
        onResizeEnd() {
            this.isResizing = false
            document.removeEventListener('mousemove', this.onResizeMove)
            document.removeEventListener('mouseup', this.onResizeEnd)
            document.body.style.cursor = ''
        },
        onKeyDown(e) {
            if (e.key === 'Escape' && this.isResizing) {
                this.onResizeEnd()
            }
        }
    }
}
</script>
<style scoped>
    .sidebar-resize-handle {
        position: absolute;
        right: 0;
        top: 0;
        bottom: 0;
        width: 6px;
        cursor: col-resize;
        background-color: rgba(0, 0, 0, 0.1);
        z-index: 10;
    }
    
    .sidebar-resize-handle:hover {
        background-color: rgba(0, 0, 0, 0.2);
    }
</style>
