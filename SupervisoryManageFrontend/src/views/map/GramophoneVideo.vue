<template>
  <el-dialog :visible.sync="videoVisible" width="60%" @close="closeVideo">
    <video controls width="100%">
      <source src="https://1321383016.vod-qcloud.com/0fa1cf45vodsh1321383016/9ed0097e1253642701679423830/GipMGC4b9AMA.mp4" type="video/mp4">
      Your browser does not support the video tag.
    </video>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      videoVisible: this.visible
    };
  },
  watch: {
    visible(val) {
      this.videoVisible = val;
    },
    videoVisible(val) {
      this.$emit('update:visible', val);
    }
  },
  methods: {
    closeVideo() {
      this.videoVisible = false;
    }
  }
};
</script>
