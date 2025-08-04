<template>
  <el-dialog :visible.sync="dialogVisible" width="85%" @close="closeDialog">
    <div class="chat-container">
      <div class="messages-list">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="messageClass(message.sender)"
        >
          <div v-if="message.type === 'text'" v-html="renderMarkdown(message.content)"></div>
          <div v-else-if="message.type === 'video'">
            <video controls :src="message.content" width="100%"></video>
          </div>
        </div>
        <div v-if="isTyping" class="typing-indicator">对方正在输入...</div>
      </div>
      <div class="input-container">
        <el-input
          type="textarea"
          rows="2"
          placeholder="Type your message..."
          v-model="userMessage"
          @keypress.enter.native.prevent="sendMessage"
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
      isTyping: false,
      markdown: new MarkdownIt()
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
    sendMessage(messageContent = null) {
      if (messageContent instanceof Event) {
        messageContent = null;
      }
      const content = messageContent !== null ? String(messageContent).trim() : this.userMessage.trim();
      if (content) {
        const message = {
          sender: 'user',
          content: content,
          type: 'text'
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
          appId: "404bf78d45714e4a818dce493509a4a8",
          prompt: prompt
        });
        let content = response.data.data;

        // Find all .mp4 URLs
        const mp4Urls = content.match(/https?:\/\/\S+\.mp4/g) || [];
        mp4Urls.forEach((url, index) => {
          // Replace the Markdown link with a placeholder text like "第x个视频"
          content = content.replace(new RegExp(`\\[.*?\\]\\(${url}\\)`, 'g'), `第${index + 1}个视频`);
        });

        // Push the text message with the replaced content
        const aiTextMessage = {
          sender: 'ai',
          content: content,
          type: 'text'
        };
        this.messages.push(aiTextMessage);

        // Push each video link as a separate video message
        mp4Urls.forEach((url) => {
          const aiVideoMessage = {
            sender: 'ai',
            content: url,
            type: 'video'
          };
          this.messages.push(aiVideoMessage);
        });
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
      return this.markdown.render(content);
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
