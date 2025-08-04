<template>
  <div>
    <img
      class="lab-logo"
      src="https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/SOARingLab.png"
      alt="SOARing Lab Logo"
      @click="showCarousel"
    />
    <el-dialog :visible.sync="carouselVisible" width="50%" :show-close="false">
      <el-carousel :interval="5000" arrow="always" height="400px">
        <el-carousel-item v-for="(image, index) in images" :key="index">
          <div class="carousel-image-wrapper">
            <img :src="image" class="carousel-image" />
          </div>
        </el-carousel-item>
      </el-carousel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="carouselVisible = false">Close</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      carouselVisible: false,
      images: [
        "https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E5%AE%9E%E9%AA%8C%E5%AE%A4%E7%AE%80%E4%BB%8B1%EF%BC%88%E6%97%A0%E8%81%94%E7%B3%BB.png",
        // "https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E5%AE%9E%E9%AA%8C%E5%AE%A4%E7%AE%80%E4%BB%8B2.png",
        // "https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E5%AE%9E%E9%AA%8C%E5%AE%A4%E7%AE%80%E4%BB%8B3.png"
      ]
    };
  },
  methods: {
    showCarousel() {
      this.carouselVisible = true;
    }
  }
};
</script>

<style scoped>
.lab-logo {
  position: fixed;
  bottom: 20px;  /* 距离底部一定距离 */
  left: 90px;   /* 在留声机图标的右边 */
  width: 50px;  /* 适当的宽度，缩小尺寸 */
  height: auto; /* 自动调整高度，保持比例 */
  z-index: 1000; /* 提升层级，确保显示在前面 */
  cursor: pointer; /* 鼠标悬停时显示手型 */
  transition: transform 0.3s ease; /* 添加过渡效果 */
}

.lab-logo:hover {
  transform: scale(1.1); /* 鼠标悬停时放大 */
}

.carousel-image-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  overflow: auto; /* 启用滚动条 */
}

.carousel-image {
  max-width: 90%;  /* 最大宽度限制为90%，进一步缩小 */
  max-height: 90%; /* 最大高度限制为90%，进一步缩小 */
  object-fit: contain; /* 保持图片比例缩放 */
}
</style>
