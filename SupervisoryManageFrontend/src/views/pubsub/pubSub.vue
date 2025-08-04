<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>PubSub管理</el-breadcrumb-item>
        <el-breadcrumb-item>PubSub信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!--卡片视图-->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input placeholder="请输入名称" v-model="inputName" clearable />
        </el-col>
        <el-col :span="4">
          <el-input placeholder="请输入URL" v-model="inputUrl" clearable />
        </el-col>
        <el-col :span="4">
          <el-button icon="el-icon-search" @click="getPubSubList">搜索</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddDialog">添加PubSub</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="danger" @click="multipleDelete">批量删除</el-button>
        </el-col>
      </el-row>

      <el-table :data="pubSubList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="name" label="名称" width="240px"></el-table-column>
        <el-table-column prop="url" label="URL"></el-table-column>

        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改PubSub信息" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除PubSub" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deletePubSubById(scope.row.id)"></el-button>
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

    <!--添加PubSub对话框-->
    <el-dialog title="添加PubSub" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="addForm.url"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addPubSub">确定</el-button>
      </span>
    </el-dialog>

    <!--修改PubSub对话框-->
    <el-dialog title="修改PubSub信息" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="editForm.url"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="editPubSubInfo">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      queryInfo: {
        name: '',
        url: '',
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      addDialogVisible: false,
      addForm: {
        name: '',
        url: ''
      },
      addFormRules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入 URL', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入 URL', trigger: 'blur' }
        ]
      },
      inputName: '',
      inputUrl: '',
      pubSubList: [],
      multipleSelection: []
    }
  },
  created() {
    this.getPubSubList();
  },
  methods: {
    getPubSubList() {
      this.queryInfo.name = this.inputName;
      this.queryInfo.url = this.inputUrl;
      axios.post('/pubSub/all', this.queryInfo).then(resp => {
        this.pubSubList = resp.data.data;
        console.log("this.pubSubList", this.pubSubList)
        this.total = resp.data.total;
        this.queryInfo.pageSize = resp.data.pageSize;
        this.queryInfo.pageNum = resp.data.pageNum;
      }).catch(error => {
        console.error(error);
      });
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getPubSubList();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getPubSubList();
    },
    showAddDialog() {
      this.addDialogVisible = true;
    },
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    addPubSub() {
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return;
        axios.post('/pubSub/add', this.addForm).then(resp => {
          this.getPubSubList();
          this.addDialogVisible = false;
        }).catch(error => {
          console.error(error);
        });
      });
    },
    showEditDialog(id) {
      axios.get(`/pubSub/${id}`).then(resp => {
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
    editPubSubInfo() {
      this.$refs.editFormRef.validate(valid => {
        if (!valid) return;
        axios.put('/pubSub/update', this.editForm).then(resp => {
          this.getPubSubList();
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
      axios.delete('/pubSub/delete', { data: ids }).then(resp => {
        this.getPubSubList();
      }).catch(error => {
        console.error(error);
      });
    },
    deletePubSubById(id) {
      axios.delete(`/pubSub/delete/${id}`).then(resp => {
        this.getPubSubList();
      }).catch(error => {
        console.error(error);
      });
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
</style>
