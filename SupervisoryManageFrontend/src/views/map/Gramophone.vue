<template>
  <div class="gramophone-container">
    <img src="../../assets/img/留声机.png" class="gramophone-icon" @click="emitShowVideo" />
  </div>
</template>

<script>
export default {
  methods: {
    emitShowVideo() {
      this.$emit('show-video');
    }
  }
};
</script>

<style scoped>
.gramophone-container {
  position: fixed;
  bottom: 20px;
  left: 20px;
  cursor: pointer;
  z-index: 1000;
}

.gramophone-icon {
  width: 50px;
  height: 50px;
  transition: transform 0.3s;
}

.gramophone-icon:hover {
  transform: scale(1.2);
}
</style>
