<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>API管理</el-breadcrumb-item>
        <el-breadcrumb-item>API信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>


    <!-- 卡片视图 -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="2.5">
          <el-button type="primary" @click="addDialogVisible = true">添加API</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="isAbleMultipleDelete">批量删除</el-button>
        </el-col>
      </el-row>

      <el-table :data="roleList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="apiId" label="API编号" width="145"></el-table-column>
        <el-table-column prop="modelName" label="模型名称" width="180"></el-table-column>
        <el-table-column prop="apiKey" label="API KEY" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改API信息" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini"
                         @click="showEditDialog(scope.row.apiId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除API" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini"
                         @click="isAbleDelete(scope.row.apiId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="选中API" placement="top" :enterable="false" :open-delay="500">
              <el-button :type="scope.row.apiId === apiId ? 'success' : 'info' " icon="el-icon-s-opportunity" size="mini"
                         @click="getApi(scope.row.apiId)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!--分页区域-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[5, 7, 9]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

    </el-card>

    <!--添加API对话框-->
    <el-dialog title="添加API" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!--内容主题区-->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="addForm.modelName"></el-input>
        </el-form-item>
        <el-form-item label="API KEY" prop="apiKey">
          <el-input v-model="addForm.apiKey"></el-input>
        </el-form-item>
      </el-form>
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="addRole">确 定</el-button>
      </span>
    </el-dialog>

    <!--修改API对话框-->
    <el-dialog title="修改API" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="API编号" prop="apiId">
          <el-input v-model="editForm.apiId" disabled></el-input>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="editForm.modelName"></el-input>
        </el-form-item>
        <el-form-item label="API KEY" prop="apiKey">
          <el-input v-model="editForm.apiKey"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole">确 定</el-button>
      </span>
    </el-dialog>



  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "ApiInfo",
  data() {
    return {





      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      roleList: [],
      resourceList: [],
      //树形控件的属性对象
      treeProps: {
        children: 'children',
        label: 'name'
      },
      //默认勾选的权限列表
      defKeys: [],
      total: 0,

      addDialogVisible: false,
      //添加API的表单数据
      addForm: {
        modelName: '',
        apiKey: ''
      },
      //验证表单规则对象
      addFormRules: {
        modelName: [
          {required: true, message: '请输入模型名称', trigger: 'blur'}
        ],
        apiKey: [
          {required: true, message: '请输入API KEY', trigger: 'blur'}
        ]
      },
      editDialogVisible: false,
      editForm: {
        apiId: null,
        modelName: '',
        apiKey: ''
      },
      checkAbleId: {},
      editFormRules: {
        modelName: [
          {required: true, message: '请输入模型名称', trigger: 'blur'}
        ],
        apiKey: [
          {required: true, message: '请输入API KEY', trigger: 'blur'}
        ]
      },
      multipleSelection: [],
      //分配权限对话框的显示与隐藏
      setRightDialogVisible: false,

      apiId: 0,
      modelName: "",
      apiKey: ""



    }
  },
  created() {
    this.getRoleList()
    this.getApi(this.$store.state.apiId)
  },
  methods: {


    async getApi(id){
      this.apiId = id
      await axios.get('sysApi/' + id).then(resp => {
        console.log(resp)


        console.log("resp.data.data.modelName",resp.data.data.modelName)
        this.modelName =  resp.data.data.modelName
        this.apiKey = resp.data.data.apiKey
        this.CommitToStore(this.apiId, this.modelName, this.apiKey)
      })
    },

    CommitToStore(apiId, modelName, apiKey){
      this.$store.commit('setApiId', apiId)
      this.$store.commit('setModelName', modelName)
      this.$store.commit('setApiKey', apiKey)
    },

    async getRoleList() {
      const {data: res} = await axios.get('sysApi', {params: this.queryInfo})
      this.roleList = res.data;
      this.total = res.total
      this.queryInfo.pageNum = res.pageNum
      this.queryInfo.pageSize = res.pageSize
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getRoleList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getRoleList()
    },
    // 监听添加对话框的关闭事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    // 监听添加按钮
    addRole() {
      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //预校验成功，发网络请求

        axios.defaults.headers.post['Content-Type'] = 'application/json'
        const {data: res} = await axios.post('sysApi', JSON.stringify(_this.addForm))
        if (res.code !== 200) return this.$message.error(res.msg)
        //隐藏添加对话框
        this.addDialogVisible = false
        //重新加载列表
        await this.getRoleList()
        this.$message.success('添加API信息成功！')
      })
    },
    // 显示修改对话框，回显数据
    async showEditDialog(id) {
      const _this = this
      await axios.get('sysApi/' + id).then(resp => {
        console.log(resp)
        _this.editForm = resp.data.data
      })
      this.editDialogVisible = true
    },
    // 监听修改对话框的关闭事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    // 修改API信息并提交
    editRole() {
      this.$refs.editFormRef.validate(async valid => {
        const _this = this
        if (!valid) return
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        const {data: res} = await axios.put('sysApi/', JSON.stringify(_this.editForm))
        if (res.code !== 200) return this.$message.error(res.msg)

        this.editDialogVisible = false
        await this.getRoleList()
        this.$message.success('修改API信息成功！')
      })
    },
    // 监听多选框变化
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除前校验
    async isAbleMultipleDelete() {
      await this.multipleDelete()
    },
    async multipleDelete() {
      const _this = this
      //询问用户是否确认删除
      const resp = await this.$confirm('此操作将永久删除这些条目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err)

      // 用户确认删除, resp为字符串"confirm"
      // 用户取消删除，resp为字符串"cancel"
      if (resp === 'cancel') {
        return _this.$message.info('已取消删除')
      }

      let ids = []
      this.multipleSelection.forEach(data => {
        ids.push(data.apiId)
      })
      let success = true
      await axios.delete('sysApi/' + ids).then(resp => {
        if (resp.data.code !== 200) {
          success = false;
        }
      })
      if (!success) return this.$message.error('批量删除资源信息失败！')
      await this.getRoleList()
      this.$message.success('批量删除资源信息成功！')
    },
    async isAbleDelete(id) {
      await this.deleteRoleById(id)
    },
    //根据id删除对应的资源信息
    async deleteRoleById(apiId) {
      const _this = this
      //询问用户是否确认删除
      const resp = await this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err)

      // 用户确认删除, resp为字符串"confirm"
      // 用户取消删除，resp为字符串"cancel"
      console.log(resp)
      if (resp === 'cancel') {
        return _this.$message.info('已取消删除')
      }

      let success = true
      await axios.delete('sysApi/' + apiId).then(resp => {
        if (resp.data.code !== 200) {
          success = false
        }
      })
      if (!success) return _this.$message.error('删除API信息失败！')
      await this.getRoleList()
      this.$message.success('删除API信息成功！')
    },




  }
}
</script>

<style scoped>

</style>
