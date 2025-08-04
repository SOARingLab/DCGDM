<template>
  <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" @close="closeDialog">
    <div class="dialog-content">
      <p>著名建筑有：</p>
      <ul>
        <li v-for="item in dialogContent" :key="item.uri" style="display: flex; align-items: center;">
          <el-button class="item-button" type="text" @click="showDetail(item.uri, item)">{{ item.nameS }}</el-button>
          <el-button
            class="gossip-button"
            type="text"
            icon="el-icon-plus"
            size="mini"
            @click="addToGossip('location', item.nameS)"
            style="margin-left: 10px;"
          >
            加入噶讪胡
          </el-button>
        </li>
      </ul>
      <p>相关事件：</p>
      <ul>
        <li v-for="event in eventList" :key="event.uri">
          <el-button
            class="item-button"
            type="text"
            @click="showEventDetail(event.uri)"
            style="position: relative; z-index: 10;"
          >
            {{ event.title }} ({{ event.begin }} - {{ event.end }})
          </el-button>
        </li>
      </ul>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogTitle: String,
    dialogVisible: Boolean,
    dialogContent: Array,
    eventList: Array
  },
  methods: {
    closeDialog() {
      this.$emit('update:dialogVisible', false);
    },
    showDetail(uri, item) {
      this.$emit('show-detail', uri, item);
    },
    showEventDetail(uri) {
      this.$emit('show-event-detail', uri);
    },
    addToGossip(field, value) {
      this.$emit('add-to-gossip', { field, value });
    }
  }
};
</script>

<style scoped>
.dialog-content {
  background-image: url('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E4%B8%8A%E6%B5%B723.jpg');
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
  background-color: rgba(255, 255, 255, 0.65); /* 设置透明度 */
  z-index: 1;
}

.dialog-content > * {
  position: relative;
  z-index: 2; /* 确保内容在背景的上方显示 */
}

.item-button {
  border: 1px solid #007bff;
  color: #007bff;
  padding: 5px 10px;
  margin: 5px 0;
  display: inline-block;
  cursor: pointer;
  z-index: 2;
}

.item-button:hover {
  background-color: #007bff;
  color: #fff;
}

.gossip-button {
  margin-left: 10px;
  font-size: 12px;
  background-color: rgba(70, 130, 180, 0.3);
  color: #4682b4;
  padding: 3px 8px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.gossip-button:hover {
  background-color: rgba(70, 130, 180, 0.6);
  color: #ffffff;
}
</style>
