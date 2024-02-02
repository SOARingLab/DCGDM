<template>
  <div class="card">
    <div class="card-header">{{ item.title }}</div>
    <div class="card-body">{{ item.content }}</div>
  </div>
</template>

<script>
export default {
  name: "CardItem",
  props: {
    item: Object // 传递给组件的卡片数据
  }
}
</script>

<style scoped>
.card {
  background-color: #2e9df6; /* 蓝色背景 */
  color: white; /* 白色字体颜色 */
  border: 1px solid #ccc;
  margin: 10px;
  padding: 10px;
  text-align: center; /* 文字居中 */
  transition: transform 0.2s ease-in-out; /* 添加浮动的过渡效果 */
  cursor: pointer; /* 设置鼠标指针为点击手势 */
}
.card:hover {
  transform: scale(1.05); /* 鼠标悬停时放大卡片 */
}
</style>
