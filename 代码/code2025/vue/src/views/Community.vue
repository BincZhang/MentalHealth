<template>
  <div class="community-container">
    <div class="community-header">
      <h2>心理社区</h2>
      <p>在这里分享你的故事，倾听他人的心声</p>
    </div>

    <div class="community-content">
      <!-- 发帖区域 -->
      <div class="post-area">
        <el-card class="post-card">
          <div class="post-input">
            <el-input
              v-model="newPost.title"
              placeholder="标题"
              class="post-title"
            />
            <el-input
              v-model="newPost.content"
              type="textarea"
              :rows="4"
              placeholder="分享你的故事..."
              class="post-content"
            />
            <div class="post-options">
              <el-select v-model="newPost.category" placeholder="选择分类">
                <el-option label="心情分享" value="mood" />
                <el-option label="求助" value="help" />
                <el-option label="经验交流" value="experience" />
              </el-select>
              <el-switch
                v-model="newPost.isAnonymous"
                active-text="匿名发布"
              />
              <el-button type="primary" @click="submitPost">发布</el-button>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 帖子列表 -->
      <div class="posts-list">
        <el-card v-for="post in posts" :key="post.id" class="post-item">
          <div class="post-header">
            <div class="post-info">
              <span class="post-author" v-if="!post.isAnonymous">
                {{ post.userName }}
              </span>
              <span class="post-author" v-else>匿名用户</span>
              <span class="post-time">{{ formatTime(post.createTime) }}</span>
            </div>
            <div class="post-category">{{ getCategoryName(post.category) }}</div>
          </div>
          <h3 class="post-title">{{ post.title }}</h3>
          <p class="post-content">{{ post.content }}</p>
          <div class="post-actions">
            <el-button type="text" @click="likePost(post)">
              <el-icon><Star /></el-icon>
              {{ post.likes }} 点赞
            </el-button>
            <el-button type="text" @click="showComments(post)">
              <el-icon><ChatDotRound /></el-icon>
              {{ post.comments }} 评论
            </el-button>
          </div>

          <!-- 评论区域 -->
          <div v-if="post.showComments" class="comments-section">
            <div class="comment-input">
              <el-input
                v-model="newComment.content"
                type="textarea"
                :rows="2"
                placeholder="写下你的评论..."
              />
              <el-switch
                v-model="newComment.isAnonymous"
                active-text="匿名评论"
              />
              <el-button type="primary" @click="submitComment(post)">评论</el-button>
            </div>
            <div class="comments-list">
              <div v-for="comment in post.commentsList" :key="comment.id" class="comment-item">
                <div class="comment-header">
                  <span class="comment-author" v-if="!comment.isAnonymous">
                    {{ comment.userName }}
                  </span>
                  <span class="comment-author" v-else>匿名用户</span>
                  <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                </div>
                <p class="comment-content">{{ comment.content }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Star, ChatDotRound } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'Community',
  setup() {
    const posts = ref([])
    const newPost = ref({
      title: '',
      content: '',
      category: '',
      isAnonymous: false
    })
    const newComment = ref({
      content: '',
      isAnonymous: false
    })

    // 获取帖子列表
    const fetchPosts = async () => {
      try {
        const response = await axios.get('http://localhost:9999/api/community/posts')
        posts.value = response.data.map(post => ({
          ...post,
          showComments: false,
          commentsList: []
        }))
      } catch (error) {
        console.error('获取帖子列表失败:', error)
        ElMessage.error('获取帖子列表失败')
      }
    }

    // 发布新帖子
    const submitPost = async () => {
      if (!newPost.value.title || !newPost.value.content) {
        ElMessage.warning('请填写标题和内容')
        return
      }

      try {
        const user = JSON.parse(localStorage.getItem('code_user') || '{}')
        if (!user || !user.id) {
          ElMessage.warning('请先登录')
          return
        }

        const postData = {
          ...newPost.value,
          userId: user.id
        }

        await axios.post('http://localhost:9999/api/community/posts', postData, {
          headers: {
            'Authorization': `Bearer ${user.token}`
          }
        })

        ElMessage.success('发布成功')
        newPost.value = {
          title: '',
          content: '',
          category: '',
          isAnonymous: false
        }
        fetchPosts()
      } catch (error) {
        console.error('发布失败:', error)
        ElMessage.error('发布失败')
      }
    }

    // 点赞帖子
    const likePost = async (post) => {
      try {
        const user = JSON.parse(localStorage.getItem('code_user') || '{}')
        if (!user || !user.id) {
          ElMessage.warning('请先登录')
          return
        }

        await axios.post(`http://localhost:9999/api/community/posts/${post.id}/like`, null, {
          headers: {
            'Authorization': `Bearer ${user.token}`
          }
        })

        post.likes++
        ElMessage.success('点赞成功')
      } catch (error) {
        console.error('点赞失败:', error)
        ElMessage.error('点赞失败')
      }
    }

    // 显示评论
    const showComments = async (post) => {
      if (!post.showComments) {
        try {
          const response = await axios.get(`http://localhost:9999/api/community/posts/${post.id}/comments`)
          post.commentsList = response.data
        } catch (error) {
          console.error('获取评论失败:', error)
          ElMessage.error('获取评论失败')
        }
      }
      post.showComments = !post.showComments
    }

    // 提交评论
    const submitComment = async (post) => {
      if (!newComment.value.content) {
        ElMessage.warning('请输入评论内容')
        return
      }

      try {
        const user = JSON.parse(localStorage.getItem('code_user') || '{}')
        if (!user || !user.id) {
          ElMessage.warning('请先登录')
          return
        }

        const commentData = {
          ...newComment.value,
          postId: post.id,
          userId: user.id
        }

        await axios.post(`http://localhost:9999/api/community/comments`, commentData, {
          headers: {
            'Authorization': `Bearer ${user.token}`
          }
        })

        ElMessage.success('评论成功')
        newComment.value = {
          content: '',
          isAnonymous: false
        }
        showComments(post)
      } catch (error) {
        console.error('评论失败:', error)
        ElMessage.error('评论失败')
      }
    }

    // 格式化时间
    const formatTime = (time) => {
      const date = new Date(time)
      return date.toLocaleString()
    }

    // 获取分类名称
    const getCategoryName = (category) => {
      const categories = {
        mood: '心情分享',
        help: '求助',
        experience: '经验交流'
      }
      return categories[category] || category
    }

    onMounted(() => {
      fetchPosts()
    })

    return {
      posts,
      newPost,
      newComment,
      submitPost,
      likePost,
      showComments,
      submitComment,
      formatTime,
      getCategoryName
    }
  }
}
</script>

<style scoped>
.community-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.community-header {
  text-align: center;
  margin-bottom: 40px;
}

.community-header h2 {
  font-size: 32px;
  color: var(--text-dark);
  margin-bottom: 10px;
}

.community-header p {
  font-size: 16px;
  color: var(--text-light);
}

.post-area {
  margin-bottom: 30px;
}

.post-card {
  margin-bottom: 20px;
}

.post-input {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-options {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: 10px;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-item {
  margin-bottom: 20px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.post-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.post-author {
  font-weight: bold;
  color: var(--text-dark);
}

.post-time {
  color: var(--text-light);
  font-size: 14px;
}

.post-category {
  padding: 4px 8px;
  background: var(--primary-light);
  color: var(--primary);
  border-radius: 4px;
  font-size: 14px;
}

.post-title {
  font-size: 20px;
  margin-bottom: 10px;
  color: var(--text-dark);
}

.post-content {
  color: var(--text-light);
  line-height: 1.6;
  margin-bottom: 15px;
}

.post-actions {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}

.comments-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

.comment-input {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  padding: 10px;
  background: var(--bg-light);
  border-radius: 4px;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 5px;
}

.comment-author {
  font-weight: bold;
  color: var(--text-dark);
}

.comment-time {
  color: var(--text-light);
  font-size: 12px;
}

.comment-content {
  color: var(--text-light);
  line-height: 1.6;
}
</style> 