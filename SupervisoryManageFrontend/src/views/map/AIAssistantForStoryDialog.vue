<template>
  <el-dialog :visible.sync="dialogVisible" @close="closeDialog" width="85%">
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
        <div v-if="isTyping" class="typing-indicator">{{ typingMessage }}</div>
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
      typingMessages: [
        '对方正在输入...',
        '请稍等片刻...',
        '正在思考中...',
        '快好了，请稍后...',
        '正在整理回复...',
      ],
      typingMessage: '',
      typingInterval: null,
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
      clearInterval(this.typingInterval);
    },
    startTypingIndicator() {
      let index = 0;
      this.typingMessage = this.typingMessages[index];
      this.typingInterval = setInterval(() => {
        index = (index + 1) % this.typingMessages.length;
        this.typingMessage = this.typingMessages[index];
      }, 2000);
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
      this.startTypingIndicator();
      try {
        const response = await axios.post(config.tongyiUrl + 'api/rag', {
          appId: "75f8192819964922ba0b41f2c15d762f",
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
        clearInterval(this.typingInterval);
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
  width: 100%;
  max-width: 1400px;
  background-image: url('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E6%B0%91%E5%9B%BD%E4%B8%8A%E6%B5%B7.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  background-color: rgba(255, 255, 255, 0.3);
  padding: 10px;
  box-sizing: border-box;
  border-radius: 8px;
  border: none;
}
.typing-indicator {
  font-style: italic;
  color: #888;
  margin: 5px 0;
}
.messages-list {
  flex-grow: 1;
  overflow-y: auto;
  margin-bottom: 10px;
  padding: 10px;
  background-color: rgba(245, 245, 245, 0.5);
  display: flex;
  flex-direction: column;
  border-radius: 8px;
  box-sizing: border-box;
  border: none;
}

.input-container {
  display: flex;
  align-items: center;
  margin-top: 10px;
  border: none;
}

.el-input {
  flex-grow: 1;
  margin-right: 10px;
  box-sizing: border-box;
  background-color: rgba(255, 248, 220, 0.3); /* Increased transparency */
  color: #4b2e05; /* Dark brown text color for readability */
  border-radius: 5px;
  border: 1px solid rgba(139, 69, 19, 0.5); /* More transparent brown border */
}

.el-input::placeholder {
  color: rgba(139, 69, 19, 0.8); /* Match placeholder text color with theme */
}

.el-button {
  background-color: rgba(139, 69, 19, 0.8); /* Warm brown background */
  color: #f5f5dc; /* Light beige text color */
  border: none;
  border-radius: 5px;
}

.el-button:hover {
  background-color: rgba(139, 69, 19, 0.9); /* Slightly darker on hover */
}

.user-message {
  align-self: flex-end;
  background-color: rgba(139, 69, 19, 0.8); /* Warm brown for vintage feel */
  color: #f5f5dc; /* Light beige font color */
  padding: 10px;
  border-radius: 5px;
  margin: 5px 0;
  max-width: 60%;
  word-break: break-word;
  text-shadow: 1px 1px 2px #000; /* Subtle text shadow for clarity */
}

.ai-message {
  align-self: flex-start;
  background-color: rgba(245, 222, 179, 0.8); /* Lighter parchment color */
  color: #4b2e05; /* Dark brown font color */
  padding: 10px;
  border-radius: 5px;
  margin: 5px 0;
  max-width: 60%;
  word-break: break-word;
  text-shadow: 0.5px 0.5px 2px #000; /* Subtle text shadow for clarity */
}

.typing-indicator {
  align-self: flex-start;
  color: #4b2e05; /* Darkened text color */
  font-style: italic;
  margin: 5px 0;
}
</style>



