<template>
  <el-dialog :title="title" :visible.sync="visible" @close="closeDialog">
    <div v-if="content" class="dialog-content">
      <div class="detail-item">
        <span class="label">日期:</span>
        <span class="value">{{ content.dateLabel }}</span>
      </div>
      <div class="detail-item" style="display: flex; align-items: center;">
        <span class="label">标题:</span>
        <span class="value">{{ content.title }}</span>
        <el-button
          class="gossip-button"
          type="text"
          icon="el-icon-plus"
          size="mini"
          @click="addToGossip('other', content.title)"
          style="margin-left: 10px;"
        >
          加入噶讪胡
        </el-button>
      </div>
      <div class="detail-item">
        <span class="label">描述:</span>
        <span class="value">{{ content.description }}</span>
      </div>
      <div v-if="content.imageList.length">
        <div v-for="image in content.imageList" :key="image.eventImage">
          <img :src="image.eventImagePath" :alt="image.des" class="event-image" />
          <div>{{ image.des }}</div>
        </div>
      </div>
      <div class="person-list">
        <span class="label">人物:</span>
        <div v-for="person in content.personList" :key="person.uri" class="person-item-container">
          <div class="person-item" @click="handlePersonClick(person.uri, person.label)">
            {{ person.label }}
          </div>
          <el-button
            class="gossip-button"
            type="text"
            icon="el-icon-plus"
            size="mini"
            @click="addToGossip('character', person.label)"
            style="margin-left: 10px;"
          >
            加入噶讪胡
          </el-button>
        </div>
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
      type: Object,
      required: true
    }
  },
  methods: {
    closeDialog() {
      this.$emit('update:visible', false);
    },
    handlePersonClick(uri, label) {
      this.$emit('show-person-introduction', uri, label);
    },
    addToGossip(field, value) {
      this.$emit('add-to-gossip', { field, value });
    }
  }
};
</script>

<style scoped>
.dialog-content {
  background-image: url('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E6%B0%91%E5%9B%BD%E4%B8%8A%E6%B5%B7.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

.dialog-content::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.6); /* 设置透明度 */
  z-index: 1;
}

.dialog-content > * {
  position: relative;
  z-index: 2; /* 确保内容在背景的上方显示 */
}

.detail-item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.label {
  font-weight: bold;
  color: #333;
}

.value {
  color: #555;
}

.event-image {
  width: 100%;
  max-width: 500px;
  margin: 10px 0;
}

.person-list {
  margin: 10px 0;
}

.person-item-container {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.person-item {
  padding: 5px 10px;
  border: 1px solid #007bff;
  color: #007bff;
  cursor: pointer;
  border-radius: 5px;
}

.person-item:hover {
  background-color: #007bff;
  color: #fff;
}

.gossip-button {
  margin-left: 10px;
  font-size: 12px; /* 小字体 */
  background-color: rgba(70, 130, 180, 0.3); /* 淡蓝色半透明背景 */
  color: #4682b4; /* 淡蓝色字体 */
  padding: 2px 5px; /* 调整按钮大小 */
  border: none;
  border-radius: 3px; /* 添加圆角 */
  cursor: pointer;
}

.gossip-button:hover {
  background-color: rgba(70, 130, 180, 0.6); /* 鼠标悬停时背景颜色变深 */
  color: #ffffff; /* 鼠标悬停时字体变为白色 */
}
</style>
