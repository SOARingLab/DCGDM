<template>
  <div class="supermarket-app">
    <h1 class="page-title">超市进货管理系统</h1>

    <!-- 创建进货单按钮 -->
    <el-button type="danger" icon="el-icon-edit" @click="openDialog" class="create-button">创建进货单</el-button>

    <!-- 创建进货单的对话框 -->
    <el-dialog title="创建进货单" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="供应商名称">
          <el-input v-model="form.supplierName"></el-input>
        </el-form-item>
        <el-form-item label="总金额">
          <el-input type="number" v-model="form.totalCost"></el-input>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="form.createdBy"></el-input>
        </el-form-item>
        <el-form-item label="商品列表">
          <el-button type="primary" @click="openItemDialog">添加商品</el-button>
          <div v-for="(item, index) in form.items" :key="index" class="item-form">
            <p><strong>商品名称：</strong>{{ item.productName }} | <strong>数量：</strong>{{ item.quantity }} | <strong>单价：</strong>{{ item.unitPrice }} | <strong>商品编号：</strong>{{ item.productCode }}</p>
            <el-button type="danger" @click="removeItem(index)">删除商品</el-button>
          </div>
        </el-form-item>
      </el-form>

      <!-- 弹窗底部的提交和取消按钮 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </el-dialog>

    <!-- 输入商品信息的对话框 -->
    <el-dialog title="输入商品信息" :visible.sync="itemDialogVisible" width="30%">
      <el-form :model="currentItem" label-width="120px">
        <el-form-item label="商品名称">
          <el-input v-model="currentItem.productName"></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input type="number" v-model="currentItem.quantity"></el-input>
        </el-form-item>
        <el-form-item label="单价">
          <el-input type="number" v-model="currentItem.unitPrice"></el-input>
        </el-form-item>
        <el-form-item label="商品编号">
          <el-input v-model="currentItem.productCode"></el-input>
        </el-form-item>
      </el-form>

      <!-- 商品弹窗的底部按钮 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addItem">添加</el-button>
      </div>
    </el-dialog>

    <!-- 展示进货单列表 -->
    <el-table :data="receipts" border style="width: 100%" class="receipts-table">
      <el-table-column prop="receiptId" label="单据编号" width="150"></el-table-column>
      <el-table-column prop="supplierName" label="供应商" width="180"></el-table-column>
      <el-table-column prop="totalCost" label="总金额" width="120"></el-table-column>
      <el-table-column prop="createdBy" label="创建人" width="150"></el-table-column>
      <el-table-column prop="status" label="状态" width="120"></el-table-column>
      <!-- 渲染商品列表 -->
      <el-table-column label="商品列表" width="400">
        <template slot-scope="scope">
          <div v-for="(item, index) in scope.row.items" :key="index" class="item-info">
            <p><strong>商品名称：</strong>{{ item.productName }} | <strong>数量：</strong>{{ item.quantity }} | <strong>单价：</strong>{{ item.unitPrice }} | <strong>商品编号：</strong>{{ item.productCode }}</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="danger" @click="confirmReceipt(scope.row)" :disabled="scope.row.status !== '处理中'">确认收货</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios';
import config from '@/config'; // 假设有配置文件

export default {
  data() {
    return {
      dialogVisible: false,
      itemDialogVisible: false, // 控制商品弹窗的显示
      form: {
        messageName: "supermarket_need",
        businessKey: "",  // 自动生成或手动设置
        supplierName: "",
        totalCost: 0,
        createdBy: "",
        items: []
      },
      currentItem: { // 用于存储当前商品信息的对象
        productName: "",
        quantity: 0,
        unitPrice: 0,
        productCode: ""
      },
      receipts: []
    };
  },
  methods: {
    // 打开创建进货单的对话框
    openDialog() {
      this.dialogVisible = true;
      this.form.businessKey = `R${Math.floor(Math.random() * 1000) + 1}`; // 简单生成业务键
    },

    // 打开商品输入弹窗
    openItemDialog() {
      this.itemDialogVisible = true;
      this.currentItem = { productName: "", quantity: 0, unitPrice: 0, productCode: "" }; // 重置当前商品信息
    },

    // 添加商品项到列表
    addItem() {
      this.form.items.push({ ...this.currentItem });
      this.itemDialogVisible = false; // 关闭商品弹窗
    },

    // 删除商品项
    removeItem(index) {
      this.form.items.splice(index, 1);
    },

    // 提交表单，调用/supermarketNeed接口
    async submitForm() {
      try {
        const response = await axios.post(config.cbpmUrl_supermarket + '/supermarketNeed', this.form);
        this.dialogVisible = false;
        this.$message.success("进货单创建成功！");
        this.fetchReceipts(); // 重新获取最新列表
      } catch (error) {
        console.error(error);
        this.$message.error("创建进货单失败。");
      }
    },

    // 获取进货单列表
    async fetchReceipts() {
      try {
        const response = await axios.get(config.cbpmUrl_supermarket + '/receipts');
        this.receipts = response.data;
      } catch (error) {
        console.error(error);
        this.$message.error("获取进货单列表失败。");
      }
    },

    // 确认收货，调用/supermarketReceiving接口
    async confirmReceipt(row) {
      try {
        const params = {
          messageName: "supermarket_receiving",
          businessKey: row.receiptId
        };
        await axios.post(config.cbpmUrl_supermarket + '/supermarketReceiving', params);
        this.$message.success("收货成功！");
        this.fetchReceipts(); // 重新获取最新列表
      } catch (error) {
        console.error(error);
        this.$message.error("收货失败。");
      }
    }
  },
  mounted() {
    this.fetchReceipts(); // 页面加载时获取进货单列表
  }
};
</script>

<style scoped>
.supermarket-app {
  padding: 30px;
  background-color: #fdf0f0;
}

.page-title {
  color: #c62828;
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.create-button {
  background-color: #c62828;
  color: white;
  font-weight: bold;
  margin-bottom: 20px;
}

.item-info {
  margin-bottom: 10px;
}

.dialog-footer {
  text-align: right;
}

.item-form {
  margin-bottom: 10px;
}

.receipts-table {
  background-color: #fff0f0;
}

.el-button {
  margin-top: 10px;
}

.el-button[type="danger"] {
  background-color: #c62828;
  border-color: #c62828;
  color: white;
}

.el-button[disabled] {
  background-color: #e0e0e0;
  border-color: #e0e0e0;
  color: #9e9e9e;
}
</style>
