<template>
  <el-dialog :title="title" :visible.sync="visible" @close="closeDialog">
    <div v-if="displayContent">
      <div class="introduction-content">
        {{ displayContent }}
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    content: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      displayContent: '',
      typingTimer: null
    };
  },
  watch: {
    content(newContent) {
      this.startTypingEffect(newContent);
    }
  },
  methods: {
    closeDialog() {
      this.$emit('update:visible', false);
      this.resetTypingEffect();
    },
    startTypingEffect(text) {
      this.resetTypingEffect();
      let index = 0;
      this.typingTimer = setInterval(() => {
        if (index < text.length) {
          this.displayContent += text[index];
          index++;
        } else {
          clearInterval(this.typingTimer);
        }
      }, 50); // Adjust speed here (in milliseconds)
    },
    resetTypingEffect() {
      clearInterval(this.typingTimer);
      this.displayContent = '';
    }
  },
  mounted() {
    this.startTypingEffect(this.content);
  },
  beforeDestroy() {
    this.resetTypingEffect();
  }
};
</script>

<style scoped>
.introduction-content {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
  white-space: pre-wrap; /* 保持换行 */
}
</style>
