<template>
  <div class="slider-verify" :class="{ 'verify-success': verified }">
    <div class="slider-track" ref="track">
      <div class="slider-progress" :style="{ width: progress + '%' }"></div>
      <div class="slider-hint" v-if="!verified && !sliding">
        <i class="el-icon-d-arrow-right"></i>
        <span>{{ $t('login.slideToVerify') }}</span>
      </div>
      <div class="slider-hint sliding-hint" v-if="!verified && sliding">
        <span>{{ $t('login.keepSliding') }}</span>
      </div>
      <div class="slider-hint success-hint" v-if="verified">
        <i class="el-icon-circle-check"></i>
        <span>{{ $t('login.verifySuccess') }}</span>
      </div>
      <div
        class="slider-btn"
        ref="btn"
        :class="{ sliding: sliding, success: verified }"
        @mousedown="onMouseDown"
        @touchstart.prevent="onMouseDown"
      >
        <i v-if="!verified" class="el-icon-d-arrow-right"></i>
        <i v-else class="el-icon-check"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SliderVerify',
  data() {
    return {
      sliding: false,
      verified: false,
      progress: 0,
      startX: 0,
      btnLeft: 0,
      maxMove: 0
    }
  },
  methods: {
    onMouseDown(e) {
      if (this.verified) return
      this.sliding = true
      const clientX = e.touches ? e.touches[0].clientX : e.clientX
      this.startX = clientX
      this.btnLeft = this.$refs.btn.getBoundingClientRect().left
      this.maxMove = this.$refs.track.offsetWidth - this.$refs.btn.offsetWidth - 4
      window.addEventListener('mousemove', this.onMouseMove)
      window.addEventListener('mouseup', this.onMouseUp)
      window.addEventListener('touchmove', this.onMouseMove)
      window.addEventListener('touchend', this.onMouseUp)
    },
    onMouseMove(e) {
      if (!this.sliding) return
      const clientX = e.touches ? e.touches[0].clientX : e.clientX
      let offset = clientX - this.startX
      if (offset < 0) offset = 0
      if (offset > this.maxMove) {
        offset = this.maxMove
        this.onVerifySuccess()
      }
      this.progress = Math.round((offset / this.maxMove) * 100)
    },
    onMouseUp() {
      if (!this.verified && this.sliding) {
        this.progress = 0
      }
      this.sliding = false
      window.removeEventListener('mousemove', this.onMouseMove)
      window.removeEventListener('mouseup', this.onMouseUp)
      window.removeEventListener('touchmove', this.onMouseMove)
      window.removeEventListener('touchend', this.onMouseUp)
    },
    onVerifySuccess() {
      this.verified = true
      this.progress = 100
      this.$emit('success')
    },
    reset() {
      this.verified = false
      this.progress = 0
      this.sliding = false
    }
  }
}
</script>

<style scoped>
.slider-verify {
  width: 100%;
  margin-top: 4px;
}
.slider-track {
  position: relative;
  width: 100%;
  height: 42px;
  background: #e8ecef;
  border-radius: 21px;
  overflow: hidden;
  user-select: none;
}
.slider-progress {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  background: linear-gradient(90deg, #5777ba, #00b1eb);
  border-radius: 21px 0 0 21px;
  transition: width 0.05s linear;
}
.verify-success .slider-progress {
  background: linear-gradient(90deg, #67c23a, #85ce61);
}
.slider-hint {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 13px;
  pointer-events: none;
  z-index: 0;
}
.slider-hint i {
  margin-right: 6px;
  font-size: 14px;
}
.sliding-hint span {
  color: #5777ba;
}
.success-hint {
  color: #2e7d32;
  font-weight: 600;
}
.success-hint i {
  color: #2e7d32;
  font-size: 16px;
}
.slider-btn {
  position: absolute;
  left: 2px;
  top: 2px;
  width: 38px;
  height: 38px;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: background 0.3s, box-shadow 0.3s;
}
.slider-btn i {
  color: #5777ba;
  font-size: 16px;
}
.slider-btn.sliding {
  box-shadow: 0 2px 12px rgba(25, 54, 93, 0.3);
}
.slider-btn.success {
  background: #67c23a;
  box-shadow: 0 2px 8px rgba(103, 194, 58, 0.3);
}
.slider-btn.success i {
  color: #fff;
}
.verify-success .slider-track {
  background: #f0f9eb;
  border: 1px solid #c2e7b0;
}
.verify-success .slider-progress {
  background: linear-gradient(90deg, #67c23a, #85ce61);
  border-radius: 19px;
}
</style>