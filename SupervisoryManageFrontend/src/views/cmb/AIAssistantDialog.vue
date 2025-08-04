<template>
  <el-dialog :visible.sync="dialogVisible" width="90%" @close="closeDialog">
    <div class="chat-container">
      <div class="messages-list">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="messageClass(message.sender)"
          v-html="renderMarkdown(message.content)">
        </div>
        <div v-if="isTyping" class="typing-indicator">对方正在输入...</div>
      </div>
      <div class="input-container">
        <el-input
          type="textarea"
          rows="2"
          placeholder="Type your message..."
          v-model="userMessage"
          @keyup.enter.native.prevent="handleEnterKey"
        ></el-input>
        <el-button type="primary" @click="sendMessage">发送</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import axios from 'axios';
import MarkdownIt from 'markdown-it';
import config from "@/config";

export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      userMessage: '',
      messages: [],
      isTyping: false
    };
  },
  watch: {
    visible(val) {
      this.dialogVisible = val;
    },
    dialogVisible(val) {
      this.$emit('update:visible', val);
    }
  },
  methods: {
    closeDialog() {
      this.dialogVisible = false;
    },
    handleEnterKey() {
      this.sendMessage();
    },
    sendMessage(messageContent = null) {
      if (messageContent instanceof Event) {
        messageContent = null;
      }
      const content = messageContent !== null ? String(messageContent).trim() : this.userMessage.trim();
      if (content) {
        const message = {
          sender: 'user',
          content: content
        };
        this.messages.push(message);
        if (!messageContent) {
          this.userMessage = '';
        }
        this.scrollToBottom();
        this.sendToApi(content);
      }
    },
    async sendToApi(prompt) {
      this.isTyping = true;
      try {
        const response = await axios.post(config.tongyiUrl + 'api/rag', {
          appId: "1260808bb23a4b4ea39e55b8e66c8480",
          prompt: prompt
        });
        const aiMessage = {
          sender: 'ai',
          content: response.data.data
        };
        this.messages.push(aiMessage);
      } catch (error) {
        console.error(error);
      } finally {
        this.isTyping = false;
        this.scrollToBottom();
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$el.querySelector('.messages-list');
        container.scrollTop = container.scrollHeight;
      });
    },
    messageClass(sender) {
      return sender === 'user' ? 'user-message' : 'ai-message';
    },
    renderMarkdown(content) {
      const md = new MarkdownIt();
      return md.render(content);
    }
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 500px;
  width: 1150px;
}

.messages-list {
  flex-grow: 1;
  overflow-y: auto;
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.input-container {
  display: flex;
  align-items: center;
}

.el-input {
  flex-grow: 1;
}

.user-message {
  align-self: flex-end;
  background-color: #1890ff;
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  margin: 5px 0;
  max-width: 60%;
  word-break: break-word;
}

.ai-message {
  align-self: flex-start;
  background-color: #e5e5e5;
  color: #000;
  padding: 10px;
  border-radius: 5px;
  margin: 5px 0;
  max-width: 60%;
  word-break: break-word;
}

.typing-indicator {
  align-self: flex-start;
  color: #888;
  font-style: italic;
  margin: 5px 0;
}
</style>
