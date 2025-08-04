<template>
  <div class="gossip-container">
    <div class="gossip-field">
      <label>时期：</label>
      <div class="input-with-button">
        <input v-model="selectedPeriod" placeholder="请输入时间" />
      </div>
    </div>
    <div class="gossip-field">
      <label>地点：</label>
      <div class="input-with-button">
        <input v-model="inputLocation" placeholder="请输入地点" />
        <button class="confirm-button" @click="addLocation">√</button>
      </div>
      <div class="locations">
        <div v-for="(loc, index) in locations" :key="index" class="location-tag">
          {{ loc }}
          <span class="remove-location" @click="removeLocation(index)">×</span>
        </div>
      </div>
    </div>
    <div class="gossip-field">
      <label>人物：</label>
      <div class="input-with-button">
        <input v-model="inputCharacter" placeholder="请输入人物" />
        <button class="confirm-button" @click="addCharacter">√</button>
      </div>
      <div class="characters">
        <div v-for="(char, index) in characters" :key="index" class="character-tag">
          {{ char }}
          <span class="remove-character" @click="removeCharacter(index)">×</span>
        </div>
      </div>
    </div>
    <div class="gossip-field">
      <label>其他要素：</label>
      <div class="input-with-button">
        <input v-model="inputOther" placeholder="请输入要素" />
        <button class="confirm-button" @click="addOther">√</button>
      </div>
      <div class="others">
        <div v-for="(oth, index) in others" :key="index" class="other-tag">
          {{ oth }}
          <span class="remove-other" @click="removeOther(index)">×</span>
        </div>
      </div>
    </div>
    <div class="gossip-field">
      <label>类型：</label>
      <div class="story-types">
        <div
          v-for="type in storyTypes"
          :key="type"
          :class="{ 'selected': selectedTypes.includes(type) }"
          @click="toggleTypeSelection(type)"
        >
          {{ type }}
        </div>
      </div>
    </div>
    <!-- 使用图片作为生成故事的触发器 -->
    <img
      src="https://jlm-1321383016.cos.ap-shanghai.myqcloud.com/map/%E5%99%B6%E8%AE%AA%E8%83%A1%28%E5%B7%B2%E5%8E%BB%E5%BA%95%29.png"
      alt="嘎讪胡"
      class="gossip-image"
      @click="generateStory"
    />
  </div>
</template>

<script>
import axios from 'axios';
import config from "@/config";  // 请确保你已经在项目中配置了 config 文件

export default {
  props: {
    selectedPeriod: String,
    location: String,
    character: String,
    other: String
  },
  data() {
    return {
      lastPrompt: null, // 记录上一次的生成故事的要素组合
      inputLocation: '',
      inputCharacter: '',
      inputOther: '',
      locations: [],
      characters: [],
      others: [],
      storyTypes: [
        '冒险',
        '爱情',
        '科幻',
        '奇幻',
        '感动',
        '历史',
        '人文',
        '喜剧',
        '悲剧',
        '成长',
        '传记',
        '家庭'
      ],
      selectedTypes: []
    };
  },
  watch: {
    location(newLocation) {
      if (newLocation && !this.locations.includes(newLocation)) {
        this.locations.push(newLocation);
      } else if (newLocation) {
        this.$message.warning('该地点已存在');
      }
    },
    character(newCharacter) {
      if (newCharacter && !this.characters.includes(newCharacter)) {
        this.characters.push(newCharacter);
      } else if (newCharacter) {
        this.$message.warning('该人物已存在');
      }
    },
    other(newOther) {
      if (newOther && !this.others.includes(newOther)) {
        this.others.push(newOther);
      } else if (newOther) {
        this.$message.warning('该要素已存在');
      }
    }
  },
  methods: {
    addLocation() {
      if (this.inputLocation && !this.locations.includes(this.inputLocation)) {
        this.locations.push(this.inputLocation);
        this.inputLocation = '';
      } else if (this.inputLocation) {
        this.$message.warning('该地点已存在');
      }
    },
    addCharacter() {
      if (this.inputCharacter && !this.characters.includes(this.inputCharacter)) {
        this.characters.push(this.inputCharacter);
        this.inputCharacter = '';
      } else if (this.inputCharacter) {
        this.$message.warning('该人物已存在');
      }
    },
    addOther() {
      if (this.inputOther && !this.others.includes(this.inputOther)) {
        this.others.push(this.inputOther);
        this.inputOther = '';
      } else if (this.inputOther) {
        this.$message.warning('该要素已存在');
      }
    },
    async generateStory() {
      const period = this.selectedPeriod ? `时间：${this.selectedPeriod}` : '';
      const locationStr = this.locations.length > 0 ? `地点：${this.locations.join('、')}` : '';
      const charactersStr = this.characters.length > 0 ? `人物：${this.characters.join('、')}` : '';
      const othersStr = this.others.length > 0 ? `其他要素：${this.others.join('、')}` : '';
      const storyTypesStr = this.selectedTypes.length > 0 ? `类型：${this.selectedTypes.join('和')}` : '';

      const keywords = [period, locationStr, charactersStr, othersStr, storyTypesStr].filter(Boolean).join(', ');
      if(keywords === this.lastPrompt){
        this.$emit('just-open-story');
        return ;
      }
      this.lastPrompt = keywords

      try {
        const response = await axios.post(config.tongyiUrl + 'api/rag', {
          appId: "0580cb8b5e564b7ca0e53958131da205",
          prompt: keywords
        });

        const questionForStory = response.data.data;

        this.$emit('generate-story', questionForStory);
      } catch (error) {
        console.error("Error generating story:", error);
      }
    },
    removeLocation(index) {
      this.locations.splice(index, 1);
    },
    removeCharacter(index) {
      this.characters.splice(index, 1);
    },
    removeOther(index) {
      this.others.splice(index, 1);
    },
    toggleTypeSelection(type) {
      if (this.selectedTypes.includes(type)) {
        this.selectedTypes = this.selectedTypes.filter(t => t !== type);
      } else if (this.selectedTypes.length < 2) {
        this.selectedTypes.push(type);
      } else {
        this.$message.warning('最多只能选择两个类型');
      }
    }
  }
};
</script>

<style scoped>
.gossip-container {
  position: fixed;
  left: 10px;
  top: 100px;
  width: 300px;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 10px;
  border-radius: 5px;
  color: white;
  font-size: 14px;
}

.gossip-field {
  margin-bottom: 10px;
}

.gossip-field label {
  width: 80px;
  display: inline-block;
}

.input-with-button {
  display: flex;
  align-items:center;
}

.input-with-button input {
  width: calc(100% - 40px);
  padding: 3px;
  background-color: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 3px;
  color: white;
}

.confirm-button {
  width: 30px;
  padding: 3px;
  background-color: rgba(33, 150, 243, 0.7);
  border: none;
  border-radius: 3px;
  color: white;
  cursor: pointer;
  margin-left: 5px;
}

.locations, .characters, .others {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-top: 5px;
}

.location-tag, .character-tag, .other-tag {
  background-color: rgba(33, 150, 243, 0.7);
  border-radius: 3px;
  padding: 5px 10px;
  color: white;
  position: relative;
}

.remove-location, .remove-character, .remove-other {
  position:absolute;
  top: 2px;
  right: 5px;
  cursor: pointer;
}

.story-types {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.story-types div {
  padding: 5px 10px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  cursor: pointer;
  border: 1px solid transparent;
  color: white;
  transition: background-color 0.2s ease, border-color 0.2s ease;
}

.story-types div.selected {
  background-color: rgba(33, 150, 243, 0.7);
  border-color: #2196f3;
}

.gossip-image {
  display: block;
  width: 50%;
  cursor: pointer;
  transition: opacity 0.3s ease, box-shadow 0.3s ease;
  margin-left: 25%; /* 将图片向右移动25% */
}

.gossip-image:hover {
  opacity: 0.8;
  box-shadow: 0 0 8px 2px rgba(33, 150, 243, 0.8); /* 蓝色光晕效果 */
}

</style>
