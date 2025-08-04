<template>
  <el-dialog :title="detailDialogTitle" :visible.sync="detailDialogVisible" @close="closeDetailDialog">
    <div v-if="detailContent">
      <div class="detail-item">
        <span class="label">建筑名称:</span>
        <el-button type="text" class="value" @click="sendMessageToAIAssistant(detailContent.nameS)">
          {{ detailContent.nameS }}
        </el-button>
      </div>
      <div class="detail-item">
        <span class="label">地址:</span>
        <el-button type="text" class="value" @click="sendMessageToAIAssistant(detailContent.address)">
          {{ detailContent.address }}
        </el-button>
      </div>
      <div class="detail-item">
        <span class="label">类型:</span>
        <el-button type="text" class="value" @click="sendMessageToAIAssistant(detailContent.type)">
          {{ detailContent.type }}
        </el-button>
      </div>
      <div class="detail-item">
        <span class="label">描述:</span>
        <el-button type="text" class="value" @click="sendMessageToAIAssistant(currentItem.des)">
          {{ currentItem.des }}
        </el-button>
      </div>
      <div class="detail-item">
        <span class="label">地区:</span>
        <el-button type="text" class="value" @click="sendMessageToAIAssistant(currentItem.placeValue)">
          {{ currentItem.placeValue }}
        </el-button>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDetailDialog">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    detailDialogTitle: String,
    detailDialogVisible: Boolean,
    detailContent: Object,
    currentItem: Object
  },
  methods: {
    closeDetailDialog() {
      this.$emit('update:detailDialogVisible', false);
    },
    sendMessageToAIAssistant(value) {
      this.$emit('send-message-to-ai', `关于${value}`);
    }
  }
};
</script>

<style scoped>
.detail-item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
  overflow: hidden;
}

.label {
  font-weight: bold;
  color: #333;
  margin-right: 10px;
}

.value {
  color: #1890ff;
  cursor: pointer;
  text-decoration: underline;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-all;
  display: inline-block;
  max-width: 70%;
  vertical-align: middle;
}

.value:hover {
  color: #40a9ff;
}
</style>
