<template>
  <div class="logistics-app">
    <h1 class="page-title">物流公司发货管理系统</h1>

    <!-- 创建发货单按钮 -->
    <el-button type="primary" icon="el-icon-edit" @click="openDialog" class="create-button">创建发货单</el-button>

    <!-- 创建发货单的对话框 -->
    <el-dialog title="创建发货单" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" ref="formRef" label-width="120px">
        <el-form-item label="业务编号" :rules="[{ required: true, message: '请联系购货单位获取业务编号', trigger: 'blur' }]">
          <el-input v-model="form.businessKey"></el-input>
        </el-form-item>

        <el-form-item label="供应商名称">
          <el-input v-model="form.supplierName"></el-input>
        </el-form-item>
        <!-- 供应商地址 -->
        <el-form-item label="供应商地址">
          <el-input v-model="form.supplierAddress.street" placeholder="街道"></el-input>
          <el-input v-model="form.supplierAddress.city" placeholder="城市"></el-input>
          <el-input v-model="form.supplierAddress.state" placeholder="省/州"></el-input>
          <el-input v-model="form.supplierAddress.postalCode" placeholder="邮政编码"></el-input>
          <el-input v-model="form.supplierAddress.country" placeholder="国家"></el-input>
        </el-form-item>

        <el-form-item label="购货单位名称">
          <el-input v-model="form.purchaseUnit"></el-input>
        </el-form-item>
        <!-- 购货单位地址 -->
        <el-form-item label="购货单位地址">
          <el-input v-model="form.purchaseUnitAddress.street" placeholder="街道"></el-input>
          <el-input v-model="form.purchaseUnitAddress.city" placeholder="城市"></el-input>
          <el-input v-model="form.purchaseUnitAddress.state" placeholder="省/州"></el-input>
          <el-input v-model="form.purchaseUnitAddress.postalCode" placeholder="邮政编码"></el-input>
          <el-input v-model="form.purchaseUnitAddress.country" placeholder="国家"></el-input>
        </el-form-item>

        <el-form-item label="总金额">
          <el-input type="number" v-model="form.totalCost"></el-input>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="form.createdBy"></el-input>
        </el-form-item>

        <!-- 商品列表 -->
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

    <!-- 展示出库单列表 -->
    <el-table :data="receipts" border style="width: 100%" class="receipts-table">
      <el-table-column prop="receiptId" label="单据编号" width="150"></el-table-column>
      <el-table-column prop="supplierName" label="供应商名称" width="180"></el-table-column>
      <el-table-column label="供应商地址" width="200">
        <template slot-scope="scope">
          <p>{{ scope.row.supplierAddress.street }}, {{ scope.row.supplierAddress.city }}, {{ scope.row.supplierAddress.state }}, {{ scope.row.supplierAddress.country }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="purchaseUnit" label="购货单位" width="180"></el-table-column>
      <el-table-column label="购货单位地址" width="200">
        <template slot-scope="scope">
          <p>{{ scope.row.purchaseUnitAddress.street }}, {{ scope.row.purchaseUnitAddress.city }}, {{ scope.row.purchaseUnitAddress.state }}, {{ scope.row.purchaseUnitAddress.country }}</p>
        </template>
      </el-table-column>
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

      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button type="danger" @click="confirmReceipt(scope.row)" :disabled="scope.row.status !== '处理中'">确认送达</el-button>
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
        messageName: "logistics_outbound",
        businessKey: "",  // 手动输入
        supplierName: "",
        supplierAddress: {
          street: "",
          city: "",
          state: "",
          postalCode: "",
          country: ""
        },
        purchaseUnit: "",
        purchaseUnitAddress: {
          street: "",
          city: "",
          state: "",
          postalCode: "",
          country: ""
        },
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
    // 打开创建发货单的对话框
    openDialog() {
      this.dialogVisible = true;
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

    // 提交表单，调用/logisticsOutbound接口
    async submitForm() {
      if (!this.form.businessKey) {
        this.$message.error('请联系购货单位获取业务编号');
        return;
      }

      try {
        const response = await axios.post(config.cbpmUrl_logisics + '/logisticsOutbound', this.form);
        this.dialogVisible = false;
        this.$message.success("出库单创建成功！");
        this.fetchReceipts(); // 重新获取最新列表
      } catch (error) {
        console.error(error);
        this.$message.error("创建发货单失败。");
      }
    },

    // 获取出库单列表
    async fetchReceipts() {
      try {
        const response = await axios.get(config.cbpmUrl_logisics + '/logisticsReceipts');
        this.receipts = response.data;
      } catch (error) {
        console.error(error);
        this.$message.error("获取出库单列表失败。");
      }
    },

    // 确认送达，调用/logisticsConfirm接口
    async confirmReceipt(row) {
      try {
        const params = {
          messageName: "logistics_confirm",
          businessKey: row.receiptId
        };
        await axios.post(config.cbpmUrl_logisics + '/logisticsConfirm', params);
        this.$message.success("确认送达成功！");
        this.fetchReceipts(); // 重新获取最新列表
      } catch (error) {
        console.error(error);
        this.$message.error("确认送达失败。");
      }
    }
  },
  mounted() {
    this.fetchReceipts(); // 页面加载时获取出库单列表
  }
};
</script>

<style scoped>
.logistics-app {
  padding: 30px;
  background-color: #f0c14b; /* 柔和的美团黄色 */
}

.page-title {
  color: #333333;
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.create-button {
  background-color: #333333;
  color: white;
  font-weight: bold;
  margin-bottom: 20px;
}

.item-info {
  margin-bottom: 10px;
  color: #333333;
}

.dialog-footer {
  text-align: right;
}

.item-form {
  margin-bottom: 10px;
  color: #333333;
}

.receipts-table {
  background-color: #fffbe6;
}

.el-button {
  margin-top: 10px;
}

.el-button[type="danger"] {
  background-color: #ff5252;
  border-color: #ff5252;
  color: white;
}

.el-button[disabled] {
  background-color: #a0a0a0;
  border-color: #a0a0a0;
  color: #707070;
}
</style>
