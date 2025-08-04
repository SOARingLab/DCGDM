<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>Engine</el-breadcrumb-item>
        <el-breadcrumb-item>Engine Manage</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--卡片视图-->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-select v-model="inputEngineType" placeholder="请选择引擎类型" clearable>
            <el-option v-for="item in engineTypes" :key="item.value" :label="item.label" :value="item.value">
              <img :src="item.image" :alt="item.label" class="option-image" /> {{ item.label }}
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input placeholder="请输入URL" v-model="inputUrl" clearable />
        </el-col>
        <el-col :span="4">
          <el-button icon="el-icon-search" @click="getEngineList">Query</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddDialog">Add Engine</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="danger" @click="multipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <el-table :data="engineList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>

        <el-table-column label="type" width="170px">
          <template slot-scope="scope">
            <img :src="getEngineTypeImage(scope.row.engineType)" :alt="scope.row.engineType" class="engine-type-image"/>
            <span>{{ scope.row.engineType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="name">
          <template slot-scope="scope">
            <el-tag style="background-color: rgba(64, 158, 255, 0.1); border-color: #409EFF; color: #409EFF;">
              {{ scope.row.name }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="url" label="URL" width="260px"></el-table-column>
        <el-table-column prop="status" label="state" width="80px">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? 'online' : 'offline' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="PubSub名称" prop="pubSubName"></el-table-column>
        <el-table-column label="Topics">
          <template slot-scope="scope">
            <div v-for="topic in scope.row.topics" :key="topic.id" class="topic-tag">
              {{ topic.topic }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Operate" width="180">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改Engine信息" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini"
                         @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除Engine" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini"
                         @click="deleteEngineById(scope.row.id)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!--分页区域-->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <!--添加Engine对话框-->
    <el-dialog title="添加Engine" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="引擎类型" prop="engineType">
          <el-select v-model="addForm.engineType" placeholder="请选择引擎类型">
            <el-option v-for="item in engineTypes" :key="item.value" :label="item.label" :value="item.value">
              <img :src="item.image" :alt="item.label" class="option-image"/> {{ item.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="addForm.url"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>

        <el-form-item label="PubSub">
          <el-select v-model="addForm.pubSubId" placeholder="请选择 PubSub">
            <el-option v-for="pubSub in pubSubList" :key="pubSub.id" :label="pubSub.name" :value="pubSub.id">
              {{ pubSub.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Topics">
          <div>
            <el-tag
              v-for="(topic, index) in selectedTopics"
              :key="topic.id"
              closable
              @close="removeTopic(index)">
              {{ topic.topic }}
            </el-tag>
          </div>
          <el-select v-model="topicToAdd" placeholder="请选择Topic" clearable @change="addTopic">
            <el-option
              v-for="topic in allTopics"
              :key="topic.id"
              :label="topic.topic"
              :value="topic">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addEngine">确定</el-button>
      </span>
    </el-dialog>

    <!--修改Engine对话框-->
    <el-dialog title="修改Engine信息" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="引擎类型" prop="engineType">
          <el-select v-model="editForm.engineType" placeholder="请选择引擎类型">
            <el-option v-for="item in engineTypes" :key="item.value" :label="item.label" :value="item.value">
              <img :src="item.image" :alt="item.label" class="option-image"/> {{ item.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="editForm.url"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>

        <el-form-item label="PubSub">
          <el-select v-model="editForm.pubSubId" placeholder="请选择 PubSub">
            <el-option v-for="pubSub in pubSubList" :key="pubSub.id" :label="pubSub.name" :value="pubSub.id">
              {{ pubSub.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Topics">
          <div>
            <el-tag
              v-for="(topic, index) in selectedEditTopics"
              :key="topic.id"
              closable
              @close="removeEditTopic(index)">
              {{ topic.topic }}
            </el-tag>
          </div>
          <el-select v-model="topicToAddEdit" placeholder="请选择Topic" clearable @change="addEditTopic">
            <el-option
              v-for="topic in allTopics"
              :key="topic.id"
              :label="topic.topic"
              :value="topic">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="editEngineInfo">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      queryInfo: {
        engineType: '',
        url: '',
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      addDialogVisible: false,
      addForm: {
        engineType: '',
        url: '',
        name: '',
        pubSubId: '',
        topicIdList: []
      },
      addFormRules: {
        engineType: [
          {required: true, message: '请输入引擎类型', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '请输入 URL', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ]
      },
      editDialogVisible: false,
      editForm: {
        topicIdList: [],
      },
      editFormRules: {
        engineType: [
          {required: true, message: '请输入引擎类型', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '请输入 URL', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ]
      },
      inputEngineType: '',
      inputUrl: '',
      engineList: [],
      multipleSelection: [],
      engineTypes: [
        {value: 'camunda7', label: 'Camunda 7', image: require('@/assets/img/camunda.png')},
        {value: 'activiti7', label: 'Activiti 7', image: require('@/assets/img/activiti.png')},
        {value: 'flowable6', label: 'Flowable 6', image: require('@/assets/img/flowable.png')}
      ],
      allTopics: [],
      selectedTopics: [],
      topicToAdd: null,
      selectedEditTopics: [],
      topicToAddEdit: null,
      pubSubList: []
    }
  },
  created() {
    this.getEngineList();
    this.getAllTopics();
    this.getAllPubSubs();
  },
  methods: {
    getAllPubSubs() {
      axios.post('/pubSub/all', { pageNum: 1, pageSize: 10000 }).then(resp => {
        this.pubSubList = resp.data.data;
      }).catch(error => {
        console.error(error);
      });
    },
    getEngineList() {
      this.queryInfo.engineType = this.inputEngineType;
      this.queryInfo.url = this.inputUrl;
      axios.post('/engine/all', this.queryInfo).then(resp => {
        this.engineList = resp.data.data.list;
        this.total = resp.data.data.total;
        this.queryInfo.pageSize = resp.data.data.pageSize;
        this.queryInfo.pageNum = resp.data.data.pageNum;
      }).catch(error => {
        console.error(error);
      });
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getEngineList();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getEngineList();
    },
    showAddDialog() {
      this.addDialogVisible = true;
    },
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
      this.selectedTopics = [];
      this.topicToAdd = null;
    },
    addEngine() {
      this.addForm.topicIdList = this.selectedTopics.map(topic => topic.id);
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return;
        axios.post('/engine/add', this.addForm).then(resp => {
          this.getEngineList();
          this.addDialogVisible = false;
        }).catch(error => {
          console.error(error);
        });
      });
    },
    showEditDialog(id) {
      axios.get(`/engine/${id}`).then(resp => {
        this.editForm = resp.data.data;
        this.selectedEditTopics = this.editForm.topics || [];
        this.editDialogVisible = true;
      }).catch(error => {
        console.error(error);
      });
    },
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
      this.selectedEditTopics = [];
      this.topicToAddEdit = null;
    },
    cancelEdit() {
      this.editDialogVisible = false;
    },
    editEngineInfo() {
      this.editForm.topicIdList = this.selectedEditTopics.map(topic => topic.id);
      this.$refs.editFormRef.validate(valid => {
        if (!valid) return;
        axios.put('/engine/update', this.editForm).then(resp => {
          this.getEngineList();
          this.editDialogVisible = false;
        }).catch(error => {
          console.error(error);
        });
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    multipleDelete() {
      const ids = this.multipleSelection.map(item => item.id);
      axios.delete('/engine/delete', {data: ids}).then(resp => {
        this.getEngineList();
      }).catch(error => {
        console.error(error);
      });
    },
    deleteEngineById(id) {
      axios.delete(`/engine/delete/${id}`).then(resp => {
        this.getEngineList();
      }).catch(error => {
        console.error(error);
      });
    },
    getEngineTypeImage(engineType) {
      const engine = this.engineTypes.find(item => item.value === engineType);
      return engine ? engine.image : '';
    },
    getAllTopics() {
      axios.post('/topic/all', {pageNum: 1, pageSize: 10000}).then(resp => {
        this.allTopics = resp.data.data.list;
      }).catch(error => {
        console.error(error);
      });
    },
    addTopic() {
      if (this.topicToAdd && !this.selectedTopics.some(topic => topic.id === this.topicToAdd.id)) {
        this.selectedTopics.push(this.topicToAdd);
      }
      this.topicToAdd = null;
    },
    removeTopic(index) {
      this.selectedTopics.splice(index, 1);
    },
    addEditTopic() {
      if (this.topicToAddEdit && !this.selectedEditTopics.some(topic => topic.id === this.topicToAddEdit.id)) {
        this.selectedEditTopics.push(this.topicToAddEdit);
      }
      this.topicToAddEdit = null;
    },
    removeEditTopic(index) {
      this.selectedEditTopics.splice(index, 1);
    }
  }
}
</script>

<style scoped>
.row {
  white-space: nowrap;
  margin-top: 10px;
  padding: 0 10px;
  text-align: center;
  display: flex;
  justify-content: space-between;
}
.topic-tag {
  display: inline-block;
  padding: 2px 6px;
  margin: 2px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: rgba(0, 0, 0, 0.1);
}
.engine-type-image {
  width: 70px;
  height: 30px;
  vertical-align: middle;
  margin-right: 5px;
}
.option-image {
  width: 60px;
  height: 20px;
  vertical-align: middle;
  margin-right: 5px;
}
</style>
