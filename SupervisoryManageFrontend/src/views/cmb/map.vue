<template>
  <div>
    <!-- 显示当前鼠标放置的地区名称 -->
    <div class="region-name" v-if="currentRegionName">
      {{ currentRegionName }}
    </div>
    <!-- 地图视图 -->
    <el-card class="box-card">
      <div class="echarts" ref="echarts"></div>
    </el-card>

    <!-- AI助手图标 -->
    <AIAssistant @show-dialog="showAIDialog" />

    <!-- AI助手对话框 -->
    <AIAssistantDialog :visible.sync="aiDialogVisible" ref="aiDialog" />
  </div>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts';
import 'echarts-gl';
import AIAssistant from './AIAssistant.vue';
import AIAssistantDialog from './AIAssistantDialog.vue';

export default {
  components: {
    AIAssistant,
    AIAssistantDialog,
  },
  data() {
    return {
      currentRegionName: '',
      aiDialogVisible: false, // AI Assistant dialog visibility
    };
  },
  mounted() {
    this.initEcharts();
  },
  methods: {
    initEcharts() {
      axios.get('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/data.json')
        .then(resp => {
          this.renderEcharts(resp.data);
        })
        .catch(error => {
          console.error(error);
        });
    },
    renderEcharts(data) {
      var chartDom = this.$refs.echarts;
      var myChart = echarts.init(chartDom);
      var nameMap = '地图数据';
      var mapData = [];
      echarts.registerMap(nameMap, data);
      myChart.showLoading();
      var mapFeatures = echarts.getMap(nameMap).geoJson.features;
      myChart.hideLoading();
      mapFeatures.forEach(function (v, index) {
        mapData.push({
          name: v.properties.name,
          value: Math.random() * 100
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
        // geo3D: {
        //   map: nameMap,
        //   itemStyle: {
        //     color: "rgba(0, 101, 163, 0.7)",
        //     opacity: 0.8,
        //     borderColor: "rgba(255, 255, 255, 1)",
        //     borderWidth: 1
        //   },
        //   roam: true,
        //   label: {
        //     show: false,
        //     textStyle: {
        //       color: '#fff',
        //       fontSize: 16,
        //       opacity: 1,
        //       backgroundColor: 'rgba(0,0,0,0)'
        //     }
        //   },
        //   emphasis: {
        //     label: {
        //       show: true,
        //       textStyle: {
        //         color: '#ffeb3b',
        //         fontSize: 16,
        //         backgroundColor: 'rgba(0,23,11,0)'
        //       }
        //     },
        //     itemStyle: {
        //       color: "#ff7f50",
        //       opacity: 1
        //     }
        //   },
        //   light: {
        //     main: {
        //       color: '#fff',
        //       intensity: 1.2,
        //       alpha: 55,
        //       beta: 10
        //     },
        //     ambient: {
        //       intensity: 0.3
        //     }
        //   }
        // },
        series: [{
          type: 'map3D',
          map: nameMap,
          itemStyle: {
            color: "rgba(0, 101, 163, 0.7)",
            opacity: 0.8,
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
              color: "#ff7f50",
              opacity: 1
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
            rotateSensitivity: 1,			// 旋转操作的灵敏度，值越大越灵敏。支持使用数组分别设置横向和纵向的旋转灵敏度。默认为1, 设置为0后无法旋转。	rotateSensitivity: [1, 0]——只能横向旋转； rotateSensitivity: [0, 1]——只能纵向旋转。
            zoomSensitivity: 1,				// 缩放操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法缩放。
            panSensitivity: 1,				// 平移操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法平移。支持使用数组分别设置横向和纵向的平移灵敏度
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
      myChart.setOption(optionMap);

      myChart.on('click', params => {
        if (params.componentType === 'geo3D' || params.componentType === 'series') {
          this.sendMessageAndShowDialog(params.name);
        }
      });

      myChart.on('mousemove', params => {
        if (params.componentType === 'geo3D' || params.componentType === 'series') {
          this.currentRegionName = params.name;
        } else {
          this.currentRegionName = '';
        }
      });
    },
    sendMessageAndShowDialog(regionName) {
      this.aiDialogVisible = true;
      this.$refs.aiDialog.sendMessage(`上海"${regionName}"附近有招商银行网点吗？`);
    },
    showAIDialog() {
      this.aiDialogVisible = true;
    },
  }
};
</script>

<style scoped>
.echarts {
  width: 100%;
  height: 100%;
  background: url('../../assets/img/招商银行背景图.jpg') no-repeat center center;
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
  top: 10px;
  left: 10px;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 20px 40px; /* 调整 padding 使其更大 */
  border-radius: 5px;
  z-index: 1000;
  font-size: 30px; /* 调整字体大小 */
}
</style>
