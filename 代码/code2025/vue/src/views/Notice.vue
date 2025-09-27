<template>
  <div class="admin-container">
    <div class="search-card">
      <el-input clearable @clear="load" class="search-input" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" class="search-btn" @click="load">查询</el-button>
      <el-button class="reset-btn" @click="reset">重置</el-button>
    </div>

    <div class="action-card">
      <el-button type="primary" class="action-btn" @click="handleAdd">
        <el-icon><Plus /></el-icon> 新增公告
      </el-button>
    </div>

    <div class="table-card">
      <el-table 
        :data="data.tableData" 
        class="custom-table"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle"
        :row-class-name="tableRowClassName">
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="content" label="公告内容" :show-overflow-tooltip="true"/>
        <el-table-column prop="time" label="发布时间"/>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <div class="action-buttons">
              <el-button type="primary" size="small" icon="Edit" circle @click="handleEdit(scope.row)" class="edit-btn"></el-button>
              <el-button type="danger" size="small" icon="Delete" circle @click="del(scope.row.id)" class="delete-btn"></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5,10,20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
          background
          class="custom-pagination"
      />
    </div>

    <el-dialog 
      title="公告信息" 
      v-model="data.formVisible" 
      width="30%" 
      destroy-on-close
      center
      class="custom-dialog">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" class="custom-form">
        <el-form-item prop="title" label="公告标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入公告标题"/>
        </el-form-item>
        <el-form-item prop="content" label="公告内容">
          <el-input type="textarea" :rows="5" v-model="data.form.content" autocomplete="off" placeholder="请输入公告内容"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {Search, Plus} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";


const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  title:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  rules:{
    title:[{ required:true,message :'请填写公告标题',trigger:'blur'}
    ],
    content:[{ required:true,message :'请填写公告内容',trigger:'blur'}
    ],
  }
})

// 表格样式函数
const headerCellStyle = () => {
  return {
    backgroundColor: '#3a456b',
    color: '#ffffff',
    fontWeight: 'bold',
    fontSize: '16px',
    padding: '12px 0',
    textAlign: 'center'
  }
}

const cellStyle = () => {
  return {
    fontSize: '15px',
    padding: '10px 0',
    textAlign: 'center'
  }
}

const tableRowClassName = ({rowIndex}) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

const load = () => {
  request.get('/api/notice/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title
    }
  }).then(res =>{
    if(res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else{
      ElMessage.error(res.msg)
    }
  })
}
load()

const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/api/notice/add',data.form).then(res=>{
    if(res.code === '200'){
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    }else{
      ElMessage(res.msg)
    }
  })
}

const update = () =>{
  request.put('/api/notice/update',data.form).then(res=>{
    if(res.code === '200'){
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    }else{
      ElMessage(res.msg)
    }
  })
}

const save = () =>{
  formRef.value.validate(valid =>{
    if(valid){
      data.form.id? update():add()
    }
  })
}

const del = (id) =>{
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(res =>{
    request.delete('/api/notice/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err=>{})
}

const reset = ()=>{
  data.title=null
  load()
}
</script>

<style>
.admin-container {
  padding: 15px;
}

/* 卡片样式 */
.search-card, .action-card, .table-card, .pagination-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin-bottom: 15px;
  transition: box-shadow 0.3s ease;
}

.search-card:hover, .action-card:hover, .table-card:hover, .pagination-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

/* 搜索区域样式 */
.search-card {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  width: 260px;
  margin-right: 15px;
  margin-bottom: 10px;
}

.search-btn, .reset-btn {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 8px 16px;
  border-radius: 6px;
  transition: transform 0.2s ease;
}

.search-btn:hover, .reset-btn:hover {
  transform: translateY(-2px);
}

/* 操作按钮区域样式 */
.action-card {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.action-btn {
  margin-right: 15px;
  margin-bottom: 10px;
  padding: 8px 16px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  opacity: 0.9;
}

.action-btn .el-icon {
  margin-right: 5px;
}

/* 表格样式 */
.custom-table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  border: none;
}

.even-row {
  background-color: #f8f9fe;
}

.odd-row {
  background-color: #ffffff;
}

.even-row:hover, .odd-row:hover {
  background-color: #eaecff !important;
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.edit-btn, .delete-btn {
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.edit-btn:hover, .delete-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 分页样式 */
.custom-pagination {
  margin-top: 5px;
  justify-content: center;
}

/* 对话框样式 */
.custom-dialog .el-dialog__header {
  background-color: #3a456b;
  color: white;
  padding: 15px 20px;
  border-radius: 8px 8px 0 0;
}

.custom-dialog .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.custom-dialog .el-dialog__body {
  padding: 20px;
}

.custom-form {
  padding: 15px 20px 5px 0;
}

.custom-form .el-form-item {
  margin-bottom: 20px;
}
</style>