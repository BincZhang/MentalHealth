<template>
  <div class="admin-container">
    <div class="search-card">
      <el-input clearable @clear="load" class="search-input" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" class="search-btn" @click="load">查询</el-button>
      <el-button class="reset-btn" @click="reset">重置</el-button>
    </div>
    <div class="action-card">
      <el-button type="primary" class="action-btn" @click="handleAdd">
        <el-icon><Plus /></el-icon> 新增案例
      </el-button>
    </div>
    <div class="table-card">
      <el-table 
        :data="data.tableData" 
        class="custom-table"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle"
        :row-class-name="tableRowClassName">
        <el-table-column label="案例图片" width="100">
          <template #default="scope">
            <el-image 
              v-if="scope.row.img" 
              :src="scope.row.img" 
              :preview-src-list="[scope.row.img]" 
              :preview-teleported="true"
              class="case-image"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="categoryTitle" label="分类"/>
        <el-table-column prop="userName" label="发布用户"/>
        <el-table-column prop="content" label="内容">
          <template v-slot="scope">
            <el-button type="primary" size="small" class="view-btn" @click="viewContent(scope.row.content)">
              <el-icon><View /></el-icon> 查看内容
            </el-button>
          </template>
        </el-table-column>
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
      title="治疗案例信息" 
      v-model="data.formVisible" 
      width="60%" 
      destroy-on-close
      center
      class="custom-dialog">
      <el-form ref="formRef" :model="data.form" label-width="80px" class="custom-form">
        <el-form-item prop="img" label="案例主图">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token}"
              :on-success="handleFileSuccess"
              list-type="picture"
              class="image-upload"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="案例标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入案例标题"/>
        </el-form-item>
        <el-form-item prop="categoryId" label="所属分类">
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择案例分类"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="案例内容">
          <div class="editor-container">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog 
      title="案例详情"
      v-model="data.viewVisible"
      width="60%"
      destroy-on-close
      center
      class="custom-dialog content-dialog">
      <div class="content-viewer" v-html="data.content"></div>
    </el-dialog>
  </div>
</template>
<script setup>
import {Search, Plus, View} from "@element-plus/icons-vue";
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import { Editor, Toolbar} from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  title:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  content:null,
  viewVisible:false,
  categoryData:[],
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

const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage' ] = {
  headers: {
    token: data. user. token,
  },
    server: 'http://localhost:9999/files/wang/upload', //服务端图片上传接口
    fieldName: 'file' //服务端图片上传接口参数
}
//组件销毁时,也及时销毁毁编辑器。否则可能会适成内存泄漏
onBeforeUnmount (() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例,重要!
const handleCreated = (editor) => {
  editorRef.value = editor
}
const load = () => {
  request.get('/introduction/selectPage',{
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
const viewContent = (content) =>{
  data.content=content
  data.viewVisible=true
}
const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const handleFileSuccess = (res) => {
  data.form.img=res.data
}
const add = () => {
  request.post('/introduction/add',data.form).then(res=>{
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
  request.put('/introduction/update',data.form).then(res=>{
    if(res.code === '200'){
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    }else{
      ElMessage(res.msg)
    }
  })
}
const del = (id) =>{
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(res =>{
    request.delete('/introduction/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err=>{})
}
const save = () =>{
  data.form.id? update():add()
}

const reset = ()=>{
  data.title=null
  load()
}
const loadCategory =()=>{
  request.get("/category/selectAll").then(res=>{
    if(res.code==='200'){
      data.categoryData=res.data
  }else{
    ElMessage.error(res.msg)
  }
  })
}
loadCategory()
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

/* 图片样式 */
.case-image {
  width: 50px;
  height: 50px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: block;
  margin: 0 auto;
  transition: transform 0.3s ease;
}

.case-image:hover {
  transform: scale(1.1);
}

/* 查看内容按钮 */
.view-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.view-btn .el-icon {
  margin-right: 5px;
}

.view-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
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

/* 编辑器样式 */
.editor-container {
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  overflow: hidden;
}

/* 内容查看器样式 */
.content-dialog .content-viewer {
  max-height: 600px;
  overflow-y: auto;
  padding: 15px;
  line-height: 1.6;
}

/* 上传组件样式 */
.image-upload .el-upload-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.image-upload .el-upload-list .el-upload-list__item {
  margin: 0;
}
</style>