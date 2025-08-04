<template>
  <div v-if="visible" class="tv-dialog">
    <div class="tv-frame">
      <div class="tv-screen">
        <video controls width="100%">
          <source src="https://1321383016.vod-qcloud.com/0fa1cf45vodsh1321383016/9ed0097e1253642701679423830/GipMGC4b9AMA.mp4" type="video/mp4">
          Your browser does not support the video tag.
        </video>
      </div>
    </div>
    <div class="close-button" @click="closeDialog">×</div>
  </div>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    closeDialog() {
      this.$emit('update:visible', false);
    }
  }
};
</script>

<style scoped>
.tv-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60%;
  max-width: 800px;
  z-index: 2000;
  background: transparent;
  border: none;
  outline: none;
}

.tv-frame {
  position: relative;
  width: 100%;
  padding-bottom: 56.25%; /* 16:9 aspect ratio */
  background: #333;
  border: 20px solid #000; /* outer frame */
  border-radius: 15px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.tv-screen {
  position: absolute;
  top: 5%;
  left: 5%;
  width: 90%;
  height: 90%;
  background: #000;
  border: 5px solid #444; /* inner frame */
  border-radius: 10px;
  overflow: hidden;
}

video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.close-button {
  position: absolute;
  top: -30px;
  right: -30px;
  width: 30px;
  height: 30px;
  background: #fff;
  color: #000;
  border-radius: 50%;
  text-align: center;
  line-height: 30px;
  font-size: 20px;
  cursor: pointer;
}
</style>
