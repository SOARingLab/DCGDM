<template>
  <div class="ai-assistant-container">
    <img src="../../assets/img/解说员.png" class="ai-assistant-icon" @click="emitShowDialog" />
  </div>
</template>

<script>
export default {
  methods: {
    emitShowDialog() {
      this.$emit('show-dialog');
    }
  }
};
</script>

<style scoped>
.ai-assistant-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  cursor: pointer;
  z-index: 1000;
}

.ai-assistant-icon {
  width: 50px;
  height: 50px;
  transition: transform 0.3s;
}

.ai-assistant-icon:hover {
  transform: scale(1.2);
}
</style>
