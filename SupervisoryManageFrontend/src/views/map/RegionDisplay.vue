<template>
  <transition name="slide">
    <div v-if="visible" :class="['region-display', { 'collapsed': isCollapsed }]">
      <div class="toggle-button" @click="togglePanel"></div>
      <div v-if="!isCollapsed" class="content-wrapper">
        <div class="region-name">{{ regionName }}</div>
        <video ref="videoPlayer" controls autoplay muted class="video-player">
          <source :src="videoUrl" type="video/mp4">
          Your browser does not support the video tag.
        </video>
        <div v-if="eventList.length > 0" class="event-list">
          <p>相关事件：</p>
          <ul>
            <li v-for="(event, index) in eventList.slice(0, 5)" :key="event.uri" class="event-item">
              <el-button
                class="event-button"
                type="text"
                @click="showEventDetail(event.uri)"
                style="position: relative; z-index: 10;"
              >
                {{ event.title }}
              </el-button>
              <el-button
                class="gossip-button"
                type="text"
                icon="el-icon-plus"
                size="mini"
                @click="addToGossip('other', event.title)"
                style="margin-left: 10px;"
              >
                加入噶讪胡
              </el-button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import axios from 'axios';
import config from "@/config";

export default {
  props: {
    regionName: {
      type: String,
      required: true
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      videoUrls: [],
      videoUrl: "",
      eventList: [],  // 用于存储事件列表
      isCollapsed: false  // 控制展示牌是否收起
    };
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.chooseRandomVideo();
        this.loadEventList();
      }
    }
  },
  mounted() {
    this.loadVideoUrls();
  },
  methods: {
    togglePanel() {
      this.isCollapsed = !this.isCollapsed;
    },
    loadVideoUrls() {
      axios.get('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/video.json')
        .then(response => {
          this.videoUrls = response.data.videoUrls;
        })
        .catch(error => {
          console.error("Error loading video URLs:", error);
        });
    },
    chooseRandomVideo() {
      if (this.videoUrls.length > 0) {
        const randomIndex = Math.floor(Math.random() * this.videoUrls.length);
        this.videoUrl = this.videoUrls[randomIndex];
        this.$nextTick(() => {
          const videoPlayer = this.$refs.videoPlayer;
          videoPlayer.load();
          videoPlayer.onloadedmetadata = () => {
            const startTime = Math.min(60, videoPlayer.duration);  // 如果视频长度小于60秒，则从视频结束前开始播放
            videoPlayer.currentTime = startTime;
          };
        });
      }
    },
    loadEventList() {
      const randomYear = Math.floor(Math.random() * (1980 - 1840 + 1)) + 1840;  // 生成1840~1980之间的随机年份
      axios.get(config.tongyiUrl + `api/eventListByText?eventFreeText=${randomYear}`)
        .then(resp => {
          if (resp.data.result === "0") {
            this.eventList = resp.data.data;
          }
        })
        .catch(error => {
          console.error("Error loading event list:", error);
        });
    },
    showEventDetail(uri) {
      console.log('test showEventDetail', uri)
      window.open(uri, '_blank');

      this.$emit('show-event-detail', uri);
    },
    addToGossip(field, value) {
      this.$emit('add-to-gossip', {field, value});
    }
  }
};
</script>

<style scoped>
.region-display {
  position: fixed;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  width: 400px;
  height: auto;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 10px;
  border-radius: 5px;
  color: white;
  font-size: 14px;
  z-index: 2000;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.5s ease-in-out;
}

.region-display.collapsed {
  transform: translateY(-50%) translateX(100%);
}

.toggle-button {
  position: absolute;
  top: 50%;
  left: -25px;
  transform: translateY(-50%);
  background-color: rgba(173, 216, 230, 0.5); /* 淡蓝色半透明 */
  width: 20px;
  height: 20px;
  border-radius: 50%;
  cursor: pointer;
  z-index: 3000;
  transition: left 0.5s ease-in-out;
}

.region-display.collapsed .toggle-button {
  left: -10px;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
}

.region-name {
  font-size: 18px;
  margin-bottom: 10px;
}

.video-player {
  width: 100%;
  height: auto;
  margin-top: 10px;
}

.event-list {
  margin-top: 10px;
}

.event-list p {
  font-weight: bold;
}

.event-list ul {
  list-style-type: none;
  padding: 0;
}

.event-item {
  margin: 5px 0;
  padding: 5px;
  display: flex;
  align-items: center;
}

.event-button {
  padding: 5px 10px;
  border: 1px solid #007bff;
  color: #007bff;
  cursor: pointer;
  border-radius: 5px;
}

.event-button:hover {
  background-color: #007bff;
  color: #fff;
}

.gossip-button {
  margin-left: 10px;
  font-size: 12px;
  background-color: rgba(70, 130, 180, 0.3);
  color: #4682b4;
  padding: 2px 5px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.gossip-button:hover {
  background-color: rgba(70, 130, 180, 0.6);
  color: #ffffff;
}

/* 添加淡入淡出的动画效果 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */
{
  opacity: 0;
}

/* 添加滑动的动画效果 */
.slide-enter-active, .slide-leave-active {
  transition: all 0.5s ease;
}

.slide-enter, .slide-leave-to /* .slide-leave-active in <2.1.8 */
{
  transform: translateX(100%);
  opacity: 0;
}
</style>
