<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>Topic管理</el-breadcrumb-item>
        <el-breadcrumb-item>Topic信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--卡片视图-->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input placeholder="请输入Topic" v-model="inputTopic" clearable />
        </el-col>
        <el-col :span="4">
          <el-button icon="el-icon-search" @click="getTopicList">搜索</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddDialog">添加Topic</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="danger" @click="multipleDelete">批量删除</el-button>
        </el-col>
      </el-row>

      <el-table :data="topicList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="id" label="ID" width="360px"></el-table-column>
        <el-table-column prop="topic" label="Topic" width="480px"></el-table-column>

        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改Topic信息" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除Topic" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteTopicById(scope.row.id)"></el-button>
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

    <!--添加Topic对话框-->
    <el-dialog title="添加Topic" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="Topic" prop="topic">
          <el-input v-model="addForm.topic"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchPossibleTopics">查找</el-button>
        </el-form-item>
        <el-form-item v-if="possibleTopics.length">
          <div>
            <el-button
              v-for="(topic, index) in possibleTopics"
              :key="index"
              type="text"
              @click="selectTopic(topic)">
              {{ topic }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>

    <!--修改Topic对话框-->
    <el-dialog title="修改Topic信息" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="Topic" prop="topic">
          <el-input v-model="editForm.topic"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="editTopicInfo">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/config";

export default {
  data() {
    return {
      queryInfo: {
        topic: '',
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      addDialogVisible: false,
      addForm: {
        topic: ''
      },
      addFormRules: {
        topic: [
          { required: true, message: '请输入Topic', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        topic: [
          { required: true, message: '请输入Topic', trigger: 'blur' }
        ]
      },
      inputTopic: '',
      topicList: [],
      multipleSelection: [],
      possibleTopics: []
    };
  },
  created() {
    this.getTopicList();
  },
  methods: {
    getTopicList() {
      this.queryInfo.topic = this.inputTopic;
      axios.post(config.apiBaseUrl + 'topic/all', this.queryInfo).then(resp => {
        this.topicList = resp.data.data.list;
        this.total = resp.data.data.total;
        this.queryInfo.pageSize = resp.data.data.pageSize;
        this.queryInfo.pageNum = resp.data.data.pageNum;
      }).catch(error => {
        console.error(error);
      });
    },
    searchPossibleTopics() {
      const prompt = this.addForm.topic;
      axios.post(config.tongyiUrl + 'api/rag', {
        appId: "614c78e6ca8d44369958d13640511287",
        prompt: prompt
      }).then(resp => {
        const data = resp.data.data;
        this.possibleTopics = data.slice(1, -1).split(",").map(item => item.trim());
      }).catch(error => {
        console.error(error);
      });
    },
    selectTopic(topic) {
      this.addForm.topic = topic;
      this.addTopic();
    },
    addTopic() {
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return;
        axios.post(config.apiBaseUrl + 'topic/add', this.addForm).then(() => {
          this.getTopicList();
          this.addDialogVisible = false;
          this.possibleTopics = [];
        }).catch(error => {
          console.error(error);
        });
      });
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getTopicList();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getTopicList();
    },
    showAddDialog() {
      this.addDialogVisible = true;
    },
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
      this.possibleTopics = [];
    },
    showEditDialog(id) {
      axios.get(config.apiBaseUrl + `topic/${id}`).then(resp => {
        this.editForm = resp.data.data;
        this.editDialogVisible = true;
      }).catch(error => {
        console.error(error);
      });
    },
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    cancelEdit() {
      this.editDialogVisible = false;
    },
    editTopicInfo() {
      this.$refs.editFormRef.validate(valid => {
        if (!valid) return;
        axios.put(config.apiBaseUrl + 'topic/update', this.editForm).then(() => {
          this.getTopicList();
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
      axios.delete(config.apiBaseUrl + 'topic/delete', { data: ids }).then(() => {
        this.getTopicList();
      }).catch(error => {
        console.error(error);
      });
    },
    deleteTopicById(id) {
      axios.delete(config.apiBaseUrl + `topic/delete/${id}`).then(() => {
        this.getTopicList();
      }).catch(error => {
        console.error(error);
      });
    }
  }
};
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
</style>
