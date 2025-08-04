<template>
  <div class="year-slider-container" @click="hideTooltipCompletely">
    <div class="year-slider" ref="slider" @click.stop="onSliderClick($event)">
      <div
        class="year-marker"
        v-for="year in years"
        :key="year"
        :style="{ left: `${(year - 1840) * stepWidth}px` }"
        @click="fixTooltipForYear($event, year)"
      >
        <span v-if="year % 10 === 0">{{ year }}</span>
        <span v-else class="tooltip">{{ year }}</span>
      </div>
      <div
        class="year-ball"
        :style="{ left: `${(currentYear - 1840) * stepWidth}px` }"
        @mousedown.stop="startDrag($event)"
      >
        <div class="ball-tooltip">{{ currentYear }}</div>
      </div>
      <div
        v-if="showTooltip"
        :style="tooltipStyle"
        class="tooltip-box"
        @click.stop
      >
        <div
          v-for="period in tooltipContent"
          :key="period"
          class="tooltip-period"
          @click="selectPeriod(period)"
        >
          {{ period }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    value: Number // 接收父组件传递的年份
  },
  data() {
    return {
      currentYear: this.value || 1840, // 初始化为父组件传递的年份
      isDragging: false,
      years: Array.from({ length: 2020 - 1840 + 1 }, (_, i) => 1840 + i),
      stepWidth: 5, // 每年的宽度
      periods: {}, // 用于存储从服务器获取的时期数据
      showTooltip: false, // 控制弹窗的显示
      tooltipContent: [], // 存储当前年份的时期列表
      tooltipStyle: { top: '0px', left: '0px' }, // 弹窗位置
      tooltipFixed: false // 控制弹窗是否固定
    };
  },
  mounted() {
    this.fetchPeriods();
  },
  watch: {
    value(newYear) {
      this.currentYear = newYear;
    }
  },
  methods: {
    fetchPeriods() {
      axios
        .get('https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/times.json')
        .then(response => {
          this.periods = response.data;
        })
        .catch(error => {
          console.error('Failed to fetch periods:', error);
        });
    },
    startDrag(event) {
      this.isDragging = true;
      document.addEventListener('mousemove', this.onDrag);
      document.addEventListener('mouseup', this.stopDrag);
    },
    onDrag(event) {
      if (this.isDragging) {
        const newYear = this.getYearFromPosition(event.clientX);
        this.currentYear = newYear;
        this.$emit('input', newYear); // 通知父组件年份变化
      }
    },
    stopDrag() {
      this.isDragging = false;
      document.removeEventListener('mousemove', this.onDrag);
      document.removeEventListener('mouseup', this.stopDrag);
    },
    getYearFromPosition(position) {
      const sliderRect = this.$refs.slider.getBoundingClientRect();
      const relativePosition = position - sliderRect.left;
      let year = Math.round(relativePosition / this.stepWidth) + 1840;
      year = Math.max(1840, Math.min(2020, year));
      return year;
    },
    onSliderClick(event) {
      const newYear = this.getYearFromPosition(event.clientX);
      this.currentYear = newYear;
      this.$emit('input', newYear); // 通知父组件年份变化
    },
    fixTooltipForYear(event, year) {
      if (this.periods[year]) {
        this.tooltipContent = this.periods[year];
        this.showTooltip = true;
        this.tooltipFixed = true;
        this.tooltipStyle = {
          top: `${event.clientY + 10}px`,
          left: `${event.clientX + 10}px`
        };
      }
    },
    selectPeriod(period) {
      console.log("test period", period)
      this.$emit('select-period', period); // 将选择的时期传递给父组件
    },
    hideTooltipCompletely() {
      if (!this.tooltipFixed) return;
      this.tooltipFixed = false;
      this.showTooltip = false;
      this.tooltipContent = [];
    }
  }
};
</script>


<style scoped>
.year-slider-container {
  position: fixed;
  top: 10px;
  right: 10px;
  width: 1000px; /* 调整宽度以适应新大小 */
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  padding: 5px;
  display: flex;
  align-items: center;
  overflow-x: hidden;
  overflow-y: hidden;
}

.year-slider {
  position: relative;
  width: 950px; /* 调整宽度以适应所有年份 */
  height: 20px;
  background-color: transparent;
  border-radius: 10px;
  cursor: pointer;
}

.year-marker {
  position: absolute;
  top: 0;
  width: 5px;
  height: 15px;
  background-color: transparent;
  color: #fff;
  text-align: center;
  line-height: 15px;
  font-size: 10px; /* 调整字体大小 */
}

.year-marker .tooltip {
  display: none;
}

.year-marker:hover .tooltip {
  display: block;
  position: absolute;
  top: -20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #000;
  color: #fff;
  padding: 3px;
  border-radius: 3px;
  white-space: nowrap;
  font-size: 10px; /* 调整字体大小 */
}

.year-ball {
  position: absolute;
  top: 2px; /* 调整小球的位置 */
  width: 8px;
  height: 8px;
  background-color: #ff5722;
  border-radius: 50%;
  cursor: pointer;
}

.year-ball .ball-tooltip {
  position: absolute;
  top: 15px; /* 调整小球提示的位置 */
  left: 50%;
  transform: translateX(-50%);
  background-color: #000;
  color: #fff;
  padding: 3px;
  border-radius: 3px;
  white-space: nowrap;
  font-size: 10px; /* 调整字体大小 */
}

.tooltip-box {
  position: fixed;
  background-color: rgba(0, 123, 255, 0.9);
  color: white;
  padding: 5px;
  border-radius: 5px;
  font-size: 12px;
  z-index: 1000;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tooltip-period {
  padding: 3px 8px;
  border: 1px solid rgba(255, 255, 255, 0.7);
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.tooltip-period:hover {
  background-color: rgba(255, 255, 255, 0.3);
  border-color: white;
}
</style>
