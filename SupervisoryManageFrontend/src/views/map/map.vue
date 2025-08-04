<template>
  <div>

    <!-- 应用的标题图片 -->
    <img src="https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E4%B8%8A%E6%B5%B7%E4%B8%87%E8%B1%A1logo%28%E5%B7%B2%E5%8E%BB%E5%BA%95%29.png" alt="上海万象Logo" class="app-logo">


    <!-- 显示当前鼠标放置的地区名称 -->
    <div class="region-name" v-if="currentRegionName">
      {{ currentRegionName }}
    </div>
    <!-- 地图视图 -->
    <el-card class="box-card">
      <div class="echarts" ref="echarts"></div>
    </el-card>

    <!-- 年份进度条 -->
    <YearSlider v-model="selectedYear" @select-period="handleSelectPeriod" />

    <!-- 留声机图标 -->
    <Gramophone @show-video="showVideo" />

    <!-- 实验室 Logo 组件 -->
    <LabLogo />
    <!-- 电视机样式的视频弹窗 -->
    <TVFrame :visible.sync="videoVisible" />

<!--    &lt;!&ndash; AI助手图标 &ndash;&gt;-->
<!--    <AIAssistant @show-dialog="showAIDialog" />-->
    <!-- AI助手图标  现在是打开历史噶讪胡-->
    <AIAssistant @show-dialog="openStoryDialog" />

    <!-- AI助手对话框 -->
    <AIAssistantDialog ref="aiDialog" :visible.sync="aiDialogVisible" />

    <!-- 展示牌组件 -->
    <RegionDisplay :regionName="displayRegionName" :visible="displayVisible"  @add-to-gossip="handleAddToGossip"/>
    <!-- 建筑弹窗 -->
    <ArchitectureDialog
      :dialogTitle="dialogTitle"
      :dialogVisible.sync="dialogVisible"
      :dialogContent="dialogContent"
      :eventList="eventList"
      @show-detail="showDetail"
      @show-event-detail="showEventDetail"
      @add-to-gossip="handleAddToGossip"
    />


    <!-- 建筑详细信息弹窗 -->
    <ArchitectureDetailDialog
      :detailDialogTitle="detailDialogTitle"
      :detailDialogVisible.sync="detailDialogVisible"
      :detailContent="detailContent"
      :currentItem="currentItem"
      @send-message-to-ai="sendMessageToAIAssistant"
    />

    <!-- 事件详细信息弹窗 -->
    <EventDetailDialog
      :visible.sync="eventDetailDialogVisible"
      :title="eventDetailDialogTitle"
      :content="eventDetailContent"
      @show-person-introduction="showPersonIntroduction"
      @add-to-gossip="handleAddToGossip"
    />


    <!-- 人物介绍弹窗 -->
    <PersonIntroduction
      :visible.sync="personIntroductionVisible"
      :title="personIntroductionTitle"
      :content="personIntroductionContent"
    />


    <!-- 讲故事智能助手对话框 -->
    <AIAssistantForStoryDialog ref="storyDialog" :visible.sync="storyDialogVisible" />
    <!-- Gossip 组件 -->
    <Gossip
      :selectedPeriod="selectedPeriod"
      :location="gossipLocation"
      :character="gossipCharacter"
      :other="gossipOther"
      @generate-story="sendStoryToAssistant"
      @just-open-story="justOpenStoryDialog"
    />
    <!-- 监听 generate-story 事件 -->

  </div>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts';
import 'echarts-gl';
import YearSlider from './YearSlider.vue';
import ArchitectureDialog from './ArchitectureDialog.vue';
import ArchitectureDetailDialog from './ArchitectureDetailDialog.vue';
import EventDetailDialog from './EventDetailDialog.vue';
import Gramophone from './Gramophone.vue';
import TVFrame from './TVFrame.vue';
import AIAssistant from './AIAssistant.vue';
import AIAssistantDialog from './AIAssistantDialog.vue';
import PersonIntroduction from './PersonIntroduction.vue';
// 导入新增的 Gossip 组件
import Gossip from './Gossip.vue';
import AIAssistantForStoryDialog from './AIAssistantForStoryDialog.vue';
import config from "@/config";
import LabLogo from './LabLogo.vue';  // 引入LabLogo组件
import RegionDisplay from './RegionDisplay.vue';  // 引入RegionDisplay组件
import debounce from 'lodash/debounce';
export default {
  components: {
    YearSlider,
    ArchitectureDialog,
    ArchitectureDetailDialog,
    EventDetailDialog,
    Gramophone,
    TVFrame,
    AIAssistant,
    AIAssistantDialog,
    PersonIntroduction,
    Gossip,
    AIAssistantForStoryDialog,
    LabLogo,  // 注册LabLogo组件
    RegionDisplay
  },
  data() {
    return {
      myChart: null,  // 在 data 中定义 myChart
      mapData: null, // 存储地图数据
      selectedYear: 1800,
      selectedPeriod: '',
      gossipLocation: '',
      gossipCharacter: '',
      gossipOther: '',
      dialogVisible: false,
      dialogTitle: '',
      dialogContent: [],
      eventList: [],
      detailDialogVisible: false,
      detailDialogTitle: '',
      detailContent: null,
      currentItem: null,
      currentRegionName: '',
      eventDetailDialogVisible: false,
      eventDetailDialogTitle: '',
      eventDetailContent: null,
      videoVisible: false,
      aiDialogVisible: false,
      personIntroductionVisible: false,
      personIntroductionTitle: '',
      personIntroductionContent: '',
      storyDialogVisible: false,
      displayRegionName: '',  // 展示牌显示的地区名称
      displayVisible: false,  // 控制展示牌的显示与隐藏
    };
  },
  mounted() {

    // 只在页面加载时请求一次地图数据
    axios.get('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/data.json')
      .then(resp => {
        this.mapData = resp.data;
        this.initEcharts();
      })
      .catch(error => {
        console.error(error);
      });


    // 每3秒刷新一次高亮地点
    this.refreshInterval = setInterval(() => {
      this.initEcharts();
    }, 16000);
  },
  beforeDestroy() {
    // 清除定时器，防止内存泄漏
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },
  methods: {
    // handleMouseMove: debounce(function(event) {
    //   // 处理鼠标移动的逻辑
    //   this.currentRegionName = event.name;
    // }, 150), // 100毫秒的防抖时间
    openStoryDialog() {
      this.storyDialogVisible = true;
    },
    sendStoryToAssistant(prompt) {
      if (this.$refs.storyDialog && typeof this.$refs.storyDialog.sendMessage === 'function') {
        this.storyDialogVisible = true;
        this.$refs.storyDialog.sendMessage(prompt);
      } else {
        console.error("无法找到 AIAssistantForStoryDialog 组件或 sendMessage 方法未定义。");
      }
    },
    justOpenStoryDialog(){
      this.storyDialogVisible = true;
    },
    handleAddToGossip({ field, value }) {
      if (field === 'location') {
        this.gossipLocation = value;
      } else if (field === 'character') {
        this.gossipCharacter = value;
      } else if (field === 'other') {
        this.gossipOther = value;
      }
    },
    handleSelectPeriod(period) {
      this.selectedPeriod = period;
    },
    initEcharts() {
      // 只需使用已获取的 mapData
      if (!this.mapData) return;

      this.renderEcharts(this.mapData);
    },
    renderEcharts(data) {
      // 检查是否已经存在一个 myChart 实例
      if (this.myChart) {
        this.myChart.dispose();  // 销毁已有的图表实例
      }

      // 获取图表 DOM 容器
      var chartDom = this.$refs.echarts;
      // 重新初始化 echarts 实例，并存储到 this.myChart
      this.myChart = echarts.init(chartDom);  // 这里确保 myChart 是通过 this 引用的
      var nameMap = '地图数据';
      const mapData = [];

      echarts.registerMap(nameMap, data);
      this.myChart.showLoading();
      var mapFeatures = echarts.getMap(nameMap).geoJson.features;
      this.myChart.hideLoading();

      const randomIndex = Math.floor(Math.random() * mapFeatures.length);
      const randomRegion = mapFeatures[randomIndex].properties.name;

      mapFeatures.forEach(function (v, index) {
        const regionName = v.properties.name;
        let color = "rgba(0, 101, 163, 0.6)";

        if (regionName === randomRegion) {
          color = "rgba(255,80,100,0.9)";
        }

        mapData.push({
          name: regionName,
          value: Math.random() * 100,
          itemStyle: {
            color: color
          }
        });
      });

      var optionMap = {
        backgroundColor: 'rgba(0,0,0,0)',
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {
              show: true,
              excludeComponents: ['toolbox'],
              pixelRatio: 1
            }
          }
        },
        series: [{
          type: 'map3D',
          map: nameMap,
          data: mapData,
          itemStyle: {
            color: function (params) {
              return params.data.itemStyle.color;
            },
            opacity: 1,
            borderColor: "rgba(255, 255, 255, 1)",
            borderWidth: 1
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true
            },
            itemStyle: {
              color: "rgba(255, 127, 80, 0.9)",
              opacity: 0.9
            }
          },
          zlevel: 1,
          viewControl: {			// 用于鼠标的旋转，缩放等视角控制。
            projection: 'perspective',		// 投影方式，默认为透视投影'perspective'，也支持设置为正交投影'orthographic'。
            autoRotate: false,				// 是否开启视角绕物体的自动旋转查看。[ default: false ]
            autoRotateDirection: 'cw',		// 物体自传的方向。默认是 'cw' 也就是从上往下看是顺时针方向，也可以取 'ccw'，既从上往下看为逆时针方向。
            autoRotateSpeed: 10,			// 物体自传的速度。单位为角度 / 秒，默认为10 ，也就是36秒转一圈。
            autoRotateAfterStill: 3,		// 在鼠标静止操作后恢复自动旋转的时间间隔。在开启 autoRotate 后有效。[ default: 3 ]
            damping: 0,						// 鼠标进行旋转，缩放等操作时的迟滞因子，在大于等于 1 的时候鼠标在停止操作后，视角仍会因为一定的惯性继续运动（旋转和缩放）。[ default: 0.8 ]
            // rotateSensitivity: 1,			// 旋转操作的灵敏度，值越大越灵敏。支持使用数组分别设置横向和纵向的旋转灵敏度。默认为1, 设置为0后无法旋转。	rotateSensitivity: [1, 0]——只能横向旋转； rotateSensitivity: [0, 1]——只能纵向旋转。
            // zoomSensitivity: 1,				// 缩放操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法缩放。
            // panSensitivity: 1,				// 平移操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法平移。支持使用数组分别设置横向和纵向的平移灵敏度
            panMouseButton: 'left',			// 平移操作使用的鼠标按键，支持：'left' 鼠标左键（默认）;'middle' 鼠标中键 ;'right' 鼠标右键(注意：如果设置为鼠标右键则会阻止默认的右键菜单。)
            rotateMouseButton: 'left',		// 旋转操作使用的鼠标按键，支持：'left' 鼠标左键;'middle' 鼠标中键（默认）;'right' 鼠标右键(注意：如果设置为鼠标右键则会阻止默认的右键菜单。)

            distance: 150,					// [ default: 100 ] 默认视角距离主体的距离，对于 grid3D 和 geo3D 等其它组件来说是距离中心原点的距离,对于 globe 来说是距离地球表面的距离。在 projection 为'perspective'的时候有效。
            minDistance: 40,				// [ default: 40 ] 视角通过鼠标控制能拉近到主体的最小距离。在 projection 为'perspective'的时候有效。
            maxDistance: 400,				// [ default: 400 ] 视角通过鼠标控制能拉远到主体的最大距离。在 projection 为'perspective'的时候有效。

            alpha: 40, 						// 视角绕 x 轴，即上下旋转的角度。配合 beta 可以控制视角的方向。[ default: 40 ]
            beta: 15,						// 视角绕 y 轴，即左右旋转的角度。[ default: 0 ]
            minAlpha: -360,					// 上下旋转的最小 alpha 值。即视角能旋转到达最上面的角度。[ default: 5 ]
            maxAlpha: 360,					// 上下旋转的最大 alpha 值。即视角能旋转到达最下面的角度。[ default: 90 ]
            minBeta: -360,					// 左右旋转的最小 beta 值。即视角能旋转到达最左的角度。[ default: -80 ]
            maxBeta: 360,					// 左右旋转的最大 beta 值。即视角能旋转到达最右的角度。[ default: 80 ]

            center: [0,0,0],				// 视角中心点，旋转也会围绕这个中心点旋转，默认为[0,0,0]。

            animation: true,				// 是否开启动画。[ default: true ]
            animationDurationUpdate: 1000,	// 过渡动画的时长。[ default: 1000 ]
            animationEasingUpdate: 'cubicInOut'		// 过渡动画的缓动效果。[ default: cubicInOut ]
          }
        }]
      };
      this.displayRegionName = randomRegion;
      this.displayVisible = true;
      setTimeout(() => {
        this.displayVisible = false;
      }, 15000); // 展示牌显示5秒钟后消失

      this.myChart.setOption(optionMap);

      this.myChart.on('click', params => {
        if (params.componentType === 'series') {
          axios.get(config.tongyiUrl + `api/architecture?freetext=${params.name}`)
            .then(resp => {
              if (resp.data.result === "0" || resp.data.result === "1") {
                this.dialogTitle = params.name;
                this.dialogContent = resp.data.data;
                this.dialogVisible = true;
                this.fetchEvents(params.name);
              }
            })
            .catch(error => {
              console.error(error);
            });
        }
      });

      this.myChart.on('mousemove', params => {
        if (params.componentType === 'series') {
          this.currentRegionName = params.name;
        } else {
          this.currentRegionName = '';
        }
      });
    },

    fetchEvents: debounce(function(regionName) {
      this.eventList = [];
      axios.get(config.tongyiUrl + `api/eventListByText?eventFreeText=${regionName}`)
        .then(resp => {
          if (resp.data.result === "0") {
            this.eventList = resp.data.data;
          }
        })
        .catch(error => {
          console.error(error);
        });

      axios.get(config.tongyiUrl + `api/eventListByDate?eventDate=${this.selectedYear}`)
        .then(resp => {
          if (resp.data.result === "0") {
            this.eventList = this.eventList.concat(resp.data.data);
          }
        })
        .catch(error => {
          console.error(error);
        });
    }, 300),  // 300 毫秒的防抖延迟
    showEventDetail(uri) {
      axios.get(config.tongyiUrl + `api/eventDetail?uri=${uri}`)
        .then(resp => {
          if (resp.data.result === "0") {
            this.eventDetailDialogTitle = "事件详细信息";
            this.eventDetailContent = resp.data.data[0];
            this.eventDetailDialogVisible = true;
          }
        })
        .catch(error => {
          console.error(error);
        });
    },
    showDetail(uri, item) {
      axios.get(config.tongyiUrl + `architectureDetail?uri=${uri}`)
        .then(resp => {
          if (resp.data.result === "0" || resp.data.result === "1") {
            this.detailDialogTitle = "详细信息";
            this.detailContent = resp.data.data[0];
            this.detailDialogVisible = true;
            this.currentItem = item;
          }
        })
        .catch(error => {
          console.error(error);
        });
    },
    showVideo() {
      this.videoVisible = true;
    },
    showAIDialog() {
      this.aiDialogVisible = true;
    },
    sendMessageToAIAssistant(message) {
      if (this.$refs.aiDialog && typeof this.$refs.aiDialog.sendMessage === 'function') {
        this.aiDialogVisible = true;
        this.$refs.aiDialog.sendMessage(message);
      } else {
        console.error("无法找到 AIAssistantDialog 组件或 sendMessage 方法未定义。");
      }
    },

    showPersonIntroduction(personUri, personLabel) {
      this.personIntroductionTitle = personLabel;
      this.personIntroductionContent = '大模型正在生成人物介绍...';
      this.personIntroductionVisible = true;

      axios.post(config.tongyiUrl + 'api/rag', {
        appId: '404bf78d45714e4a818dce493509a4a8',
        prompt: `请你介绍一下${personLabel}这个人物`
      })
        .then(resp => {
          if (resp.data.code === 200) {
            this.personIntroductionContent = resp.data.data;
          } else {
            this.personIntroductionContent = '获取人物介绍失败，请稍后再试。';
          }
        })
        .catch(error => {
          console.error(error);
          this.personIntroductionContent = '获取人物介绍失败，请稍后再试。';
        });
    }
  }
};
</script>

<style scoped>
.echarts {
  width: 100%;
  height: 100%;
  background: url('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E4%B8%87%E8%B1%A1%E4%B8%8A%E6%B5%B73.png') no-repeat center center;
  background-size: cover;
  position: fixed;
}

.echarts::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6); /* 调整透明度 */
  pointer-events: none;
}

.region-name {
  position: fixed;
  top: 90%;
  left: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 10px 20px; /* 调整 padding 使其更大 */
  border-radius: 5px;
  z-index: 1000;
  font-size: 20px; /* 调整字体大小 */
}

.app-logo {
  position: fixed;
  top: -15px;
  left: 20px;
  width: 250px; /* 可根据需要调整图片大小 */
  height: auto;
  z-index: 1001; /* 确保在最上层 */
}
</style>
