<template>
  <div class="container">
    <div class="answer-container"></div>
    <div class="panel-container">


      <!-- 右边 -->
      <div class="panel right-panel">
        <div class="right-content">
          <el-empty v-if="isEmpty"></el-empty>
          <div v-else class="right-data-box">
            <div class="right-data-box-header">
              <span>该业务流程描述了如下内容：</span>
            </div>
            <div class="right-data-box-question" ref="box">
              <div v-for="(item, index) in questions" :key="index" class="text item">
                {{item}}
              </div>
            </div>

          </div>
          <div class="popover" v-if="!isEmpty">
            <el-popover
                placement="top-start"
                :width="200"
                trigger="hover"
                content="Click to Copy"
            >
              <template #reference>
                <el-button
                    @click="onCopy"
                           circle>
                  <Edit style="width: 1em; height: 1em;" />
                </el-button>
              </template>
            </el-popover>
          </div>
        </div>
      </div>
      <div class="panel left-panel">
        <div class="left-content">
          <h2>Business Process Model GPT</h2>
          <p>supported by SOARING LAB</p>
        </div>
        <div class="input-container">

          <el-form>
          <el-form-item label="copy and paste the bpmn file content">
            <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 4}" v-model="form.question" placeholder="write down the xml"></el-input>
          </el-form-item>
          <el-form-item></el-form-item>
          <el-form-item>
            <el-button
                v-loading.fullscreen.lock="loading"
                class="go-button" type="primary" @click="onSubmit">Go ！</el-button>
            <el-button @click="onClear">RESET</el-button>
          </el-form-item>
          </el-form>
        </div>
        <img src="https://xianyue-1319816691.cos.ap-nanjing.myqcloud.com/log1.svg" class="image">
      </div>
    </div>
  </div>
</template>

<script>




export default {
  name: 'ProcessDesc',
  data() {
    return {

      form: {
        question: '',
      },
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
    onSubmit() {
      if (this.form.question.trim() == '') {
        this.$message({
          showClose: true,
          message: '问题不能为空！',
          type: 'error'
        })
        return
      }
      this.loading = true
      this.isEmpty = false
      this.questions = []
      this.getResponse()
    },
    onClear() {
      this.form.question = ''
    },
    onCopy() {
      this.$copyText(this.copyValue).then((e) => {
        this.$message({
          showClose: true,
          message: 'Copy Success!',
          type: 'success'
        })
        console.log(e)
      }, (err) => {
        this.$message({
          showClose: true,
          message: 'Copy Fail',
          type: 'error'
        })
        console.log(err)
      })
    },
    async getResponse() {
      try {
        const prompt = this.promptEngineering(this.form.question);
        const completion = await this.getCompletion(prompt)
        this.loading = false
        this.$refs.box.addEventListener('scroll',this.scrolling)
        this.scrollToBottom();
        // this.displayLikeHumanWriting(completion)
      } catch (err) {
        this.$message({
          showClose: true,
          message: err.message,
          type: 'error'
        })
      }
    },
    async getCompletion(prompt) {
      //API key:
      const apiKey = this.$store.state.apiKey
      //Url:
      const url = "https://api.openai.com/v1/chat/completions"
      //Request body:
      // const body = {
      //   prompt,
      //   model: this.$store.state.modelName,
      //   max_tokens: 2500 // Max tokens in completion (returned answer)
      // }

      const body = {
        "model": this.$store.state.modelName,
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.7
      }
      const options = {
        method: "Post",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json",
          "Authorization": "Bearer " + apiKey
        },
        body: JSON.stringify(body)
      }
      //Get response:
      const response = await fetch(url, options);

      if(response.status >= 400) throw json.error

      //Extract JSON:
      const json = await response.json()

      console.log(json.choices[0].message.content)

      const completion = json.choices[0].message.content;

      this.questions.push(completion)
      this.copyValue=completion


      return completion
    },
    async displayLikeHumanWriting(completion) {
      console.log(completion)
      this.copyValue = completion
      for(let i = 0; i < completion.length; i++) {
        if (completion[i] == '\n' && this.tempString != '') {
          this.tempString = ''   // 某个问题结束
          continue
        }
        this.tempString += completion[i]
        if (this.tempString.length == 1) {
          this.questions.push(this.tempString)
        } else {
          let len = this.questions.length
          this.questions[len - 1] = this.tempString
        }
        if (this.scrollFlag) this.scrollToBottom()
        await this.delay(30);
      }
      this.$refs.box.removeEventListener('scroll',this.scrolling)
    },
    delay(ms) {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve()
        }, ms)
      })
    },
    promptEngineering(question) {

      let prompt = `这是一段xml代码，请详细解释业务流程，包括协作者之间的协作关系。下面是代码："${question}"`
      return prompt
    },
    scrolling() {
      let scrollTop = this.$refs.box.scrollTop
      // 更新——滚动前，滚动条距文档顶部的距离
      let scrollStep = scrollTop - this.oldScrollTop;
      this.oldScrollTop = scrollTop;
      //判断当前是向上or向下滚动
      if (scrollStep < 0) {
        //向上
        console.log("正在向上滚动")
        this.scrollFlag=false
      }else{
        console.log("正在向下滚动")
        this.scrollFlag=true
      }
    },
    scrollToBottom () {
      this.$nextTick(() => {
        let container = this.$refs.box
        container.scrollTop = container.scrollHeight;
      })
    },
  },
}
</script>

<style scoped>
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
