<template>
  <div class="admin-container">
   <div class="search-card">
     <el-input clearable @clear="load" class="search-input" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
     <el-input clearable @clear="load" class="search-input" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
     <el-button type="primary" class="search-btn" @click="load">查询</el-button>
     <el-button class="reset-btn" @click="reset">重置</el-button>
   </div>
    <div class="action-card">
      <el-button type="primary" class="action-btn" @click="handleAdd">
        <el-icon><Plus /></el-icon> 新增
      </el-button>
      <el-button type="danger" class="action-btn" @click="deleteBatch">
        <el-icon><Delete /></el-icon> 批量删除
      </el-button>
      <el-button type="success" class="action-btn" @click="exportData">
        <el-icon><Download /></el-icon> 批量导出
      </el-button>
      <el-upload
          class="import-upload"
          action="http://localhost:9999/admin/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
      >
        <el-button type="warning" class="action-btn">
          <el-icon><Upload /></el-icon> 批量导入
        </el-button>
      </el-upload>
    </div>
    <div class="table-card">
      <el-table 
        :data="data.tableData" 
        @selection-change="handleSelectionChange"
        class="custom-table"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle"
        :row-class-name="tableRowClassName">
        <el-table-column type="selection" width="55" />
        <el-table-column label="头像" width="100">
          <template #default="scope">
            <el-image 
              v-if="scope.row.avatar" 
              :src="scope.row.avatar" 
              :preview-src-list="[scope.row.avatar]" 
              :preview-teleported="true"
              class="avatar-image"/>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
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
      title="管理员信息" 
      v-model="data.formVisible" 
      width="30%" 
      destroy-on-close
      center
      class="custom-dialog">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" class="custom-form">
        <el-form-item prop="username" label="账号" >
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="name" label="名称" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话" >
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱" >
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item prop="avatar" label="头像" >
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token}"
              :on-success="handleFileSuccess"
              list-type="picture"
              class="avatar-upload"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
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
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Search, Plus, Delete, Download, Upload} from "@element-plus/icons-vue";

const data= reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  username:null,
  name:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  formVisible: false,
  form:{},
  rules:{
    username:[
      {
        required:true,message:'请填写账号',trigger:'blur'}
    ],
    name:[
      {
        required:true,message:'请填写名称',trigger:'blur'}
    ],
    phone:[
      {
        required:true,message:'请填写电话',trigger:'blur'}
    ],
    email:[
      {
        required:true,message:'请填写邮箱',trigger:'blur'}
    ]
  },
  rows:[],
  ids: []
})

const formRef = ref()

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

const load=()=>{
  request.get('/admin/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      username:data.username,
      name:data.name
    }
  }).then(res=>{
    if(res.code==='200'){
      data.tableData=res.data.list
      data.total=res.data.total
    }else{
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset =()=>{
  data.username=''
  data.name=''
  load()
}

const handleAdd = ()=>{
  data.formVisible = true
  data.form = {}
}

const add =()=>{
  formRef.value.validate((valid)=>{
    if(valid){  //验证通过的情况下
      request.post('/admin/add',data.form).then(res =>{
        if(res.code==='200'){
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row)=>{
  data.form = JSON.parse(JSON.stringify(row)) //深度拷贝数据
  data.formVisible = true
}

const update =()=>{
  //formRef 是表单的引用
  formRef.value.validate((valid)=>{
    if(valid){  //验证通过的情况下
      request.put('/admin/update',data.form).then(res =>{
        if(res.code==='200'){
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save =()=>{
  data.form.id?update():add()
}

const del =(id)=>{
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(res =>{
    request.delete('/admin/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err=>{})
}

const handleSelectionChange =(rows)=>{ // rows 就是实际选择的数组
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch =()=>{
  if(data.rows.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(res =>{
    request.delete('/admin/deleteBatch',{data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    } )
  }).catch(err=>{})
}

const exportData = () => {
  let idsStr = data.ids.join(",")
  let url = `http://localhost:9999/admin/export?username=${data.username}`
  + `&name=${data.name}`
  + `&ids=${idsStr}`
  + `&token=${data.user.token}`
  window.open(url)
}

const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success("导入成功")
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

const handleFileSuccess = (res) => {
  if (res.code === '200') {
    data.form.avatar = res.data
    ElMessage.success("上传成功")
  } else {
    ElMessage.error(res.msg)
  }
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

.import-upload {
  display: inline-block;
  margin-bottom: 10px;
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

/* 头像样式 */
.avatar-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: block;
  margin: 0 auto;
  transition: transform 0.3s ease;
}

.avatar-image:hover {
  transform: scale(1.1);
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

/* 表单上传组件样式 */
.avatar-upload .el-upload-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.avatar-upload .el-upload-list .el-upload-list__item {
  margin: 0;
}
</style>
