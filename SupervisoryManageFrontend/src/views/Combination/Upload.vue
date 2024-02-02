<template>
  <div class="container">
    <div class="answer-container"></div>
    <div class="panel-container">


      <!-- 右边 -->
      <div class="panel right-panel">
        <div class="right-content">
          <div v-for="(item, index) in cardList">
            <div class="card" @click="showDialog(item)">
              <div class="card-header">Combination: {{ index }}</div>
<!--              <div class="card-body">{{ item }}</div>-->
            </div>
          </div>
        </div>
      </div>



      <div class="panel left-panel">
        <div class="left-content">
          <h2>BPMN Combiner</h2>
          <p>supported by SOARING LAB</p>
        </div>
        <div class="input-container">

          <el-form>
            <el-upload
              class="upload-demo"
              ref="upload"
              name="files"
              action="http://localhost:8080/upload"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              :on-success="successUpload"
              :auto-upload="false"
              :multiple="true"
            >

              <el-button slot="trigger" size="small" type="primary">Choose BPMN</el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">Upload</el-button>
            </el-upload >
<!--            <el-button style="margin-left: 10px;" size="small" type="success" @click="getCombinerFile">Get combinations</el-button>-->
            <el-button style="margin-left: 10px;" size="small" type="success" @click="getCombinationList">Get combinations</el-button>

          </el-form>
        </div>
        <img src="https://xianyue-1319816691.cos.ap-nanjing.myqcloud.com/log1.svg" class="image">
      </div>
    </div>
<!--    <div class="el-dialog" width="50%" v-if="showCardDialog">-->
<!--      <div class="dialog-content">{{ selectedCardContent }}</div>-->
<!--      <div class="dialog-buttons">-->
<!--        <button @click="closeDialog">取消</button>-->
<!--        <button>确定</button>-->
<!--      </div>-->
<!--    </div>-->

    <el-dialog
      title="The following business process can accomplish a collaboration."
      :visible.sync="showCardDialog"
      width="50%"
      >

      <div v-for="(item, index) in curBPMN">
        <div class="card">
          <div class="card-header">{{ item }}</div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button @click="closeDialog">Cancel</el-button>
    <el-button type="primary" @click="getCombinerFile">Download</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>


export default {
  name: 'UploadFiles',
  props: {
    msg: String
  },

  data() {
    return {

      showCardDialog: false,
      selectedCardContent: "",
      curBPMN: [],
      fileList: [],
      form: {
        question: '',
      },

      count: 8,
      cardList: [

      ],
      answer: '',
      questions: [],
      oldScrollTop: 0,
      scrollFlag: true,
      Interval: null,
      tempString: '',
      isEmpty: true,
      loading: false,
      copyValue: ''
    }
  },
  methods: {
    test(){
      console.log("test")
    },
    showDialog(item) {
      this.showCardDialog = true;
      this.selectedCardContent = item;
      console.log("this.item",item)
     let newArr = []
      for(let i = 0; i < item.length; i++){
        console.log("item.participant", item[i].participant)
        newArr.push(item[i].participant)
      }
      this.curBPMN = newArr
    },
    closeDialog() {
      this.showCardDialog = false;
    },
    // load () {
    //   this.count += 2
    // },

    successUpload(res, file, fileList) {

      console.log("测试",res)
      this.$message.success("文件上传成功 res:" + res + "file" + file + "fileList" + fileList)
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    async getCombinerFile(){
      let stringList = this.curBPMN
      const queryString = stringList.join(',');
      await axios.get("http://localhost:8080/getCombine",{
        params: {
          values: queryString // 这里的 'values' 是服务器期望接收的参数名称
        },
        responseType: 'blob',
      }).then(res => {
        console.log(res)

        let blob = new Blob([res.data]);
        let url = window.URL.createObjectURL(blob); // 创建 url 并指向 blob
        let a = document.createElement('a');
        a.href = url;
        a.download = (new Date().getTime().toString())+".bpmn";
        a.click();
        window.URL.revokeObjectURL(url); // 释放该 url

      })
    },

    async getCombinationList(){
      await axios.get("http://localhost:8080/getAllCombineList",{
      }).then(res => {
        console.log(res)

          this.cardList = res.data

      })
    },




    async uploadByAxios() {

      var param = new FormData()
      this.fileList.forEach((val, index) => {
        param.append('file', val.raw)
      })

      await axios('http://localhost:8080/upload', {
        headers: {
          'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
          'Content-Type': 'multipart/form-data'
        },

        method: 'post',
        data: param
      }).then(res => {
        console.log("res!!!!!!!!!!", res)

      })

    }

  },
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
.card-dialog {
  background-color: white;
  border: 1px solid #ccc;
  padding: 20px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  text-align: center;
}

.dialog-buttons button {
  margin: 10px;
}


.container {
  position: relative;
  width: 100%;
  background-color: #fff;
  min-height: 100vh;
  overflow: hidden;
}

.container::before {
  content: "";
  position: absolute;
  height: 2000px;
  width: 2000px;
  top: -10%;
  left: 48%;
  transform: translateY(-50%);
  background-image: linear-gradient(-45deg, #6b83ef 0%, #6b82ef 100%);
  transition: 1.8s ease-in-out;
  border-radius: 45%;
  z-index: 6;
}

.answer-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.image {
  width: 100%;
  transition: transform 1.1s ease-in-out;
  transition-delay: 0.4s;
}

.panel-container {
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
}

.panel {
  display: flex;
  flex-direction: column;
  /*align-items: flex-start;*/
  justify-content: space-around;
  z-index: 6;
}

.left-panel {
  pointer-events: all;
  padding: 3rem 12% 2rem 17%;
  text-align: center;
}

.left-panel .left-content {
  color: #fff;
  transition: transform 0.9s ease-in-out;
  transition-delay: 0.6s;
}

.left-panel h2 {
  font-weight: 600;
  line-height: 1;
  font-size: 1.5rem;
}

.left-panel p {
  font-size: 0.95rem;
  padding: 0.7rem 0;
}

.input-container {
  background-color: white;
  border-radius: 5px;
  width: 100%;
  padding: 25px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

/*.go-button {*/
/*  position: absolute;*/
/*  right: 0;*/
/*}*/

.right-panel {
  pointer-events: all;
  padding: 3rem 10% 3rem 13%;
}


.right-content {
  width: 500px;
  height: 100%;
  /*background-color: pink;*/
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  position: relative;
}


.right-data-box {
  position: relative;
}

.right-data-box-header {
  padding: 10px;
  border-bottom: 1px solid #ebeef4;
}

.right-data-box-question {
  width: 100%;
  overflow: scroll;
  overflow-x: hidden;
  max-height: 680px;
  padding: 10px 20px;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.popover {
  position: absolute;
  bottom: 20px;
  right: 20px;
}

</style>


<!--问题是超过边框不换行-->
