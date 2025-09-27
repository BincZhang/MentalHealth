<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <CommonHeader />
  
  <div class="ai-chat-container page-container bg-chat">
    <div class="chat-header">
      <h2>心语愿小助手</h2>
      <p>随时为您提供专业的心理支持和咨询服务</p>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" 
           :class="['message', message.role === 'user' ? 'user-message' : 'ai-message']">
        <div class="avatar">
          <img v-if="message.role === 'user'" src="@/assets/img/user-avatar.jpg" alt="用户头像">
          <img v-else src="@/assets/img/机器人头像.png" alt="AI头像">
        </div>
        <div class="message-content">
          <p>{{ message.content }}</p>
        </div>
      </div>
      <div v-if="isLoading" class="message ai-message">
        <div class="avatar">
          <img src="@/assets/img/机器人头像.png" alt="AI头像">
        </div>
        <div class="message-content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <textarea 
        v-model="userInput" 
        placeholder="请输入您的问题..."
        @keydown.enter.prevent="sendMessage"
        :disabled="isLoading"
      ></textarea>
      <button @click="sendMessage" :disabled="isLoading || !userInput.trim()">
        <i class="fas fa-paper-plane"></i>
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import CommonHeader from '@/components/CommonHeader.vue'

export default {
  name: 'AIChat',
  components: {
    CommonHeader
  },
  setup() {
    const messages = ref([])
    const userInput = ref('')
    const isLoading = ref(false)
    const messagesContainer = ref(null)

    const scrollToBottom = async () => {
      await nextTick()
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
      }
    }

    const sendMessage = async () => {
      if (!userInput.value.trim() || isLoading.value) return

      const userMessage = userInput.value.trim()
      messages.value.push({
        role: 'user',
        content: userMessage
      })
      userInput.value = ''
      isLoading.value = true
      await scrollToBottom()

      try {
        const user = JSON.parse(localStorage.getItem('code_user') || '{}')
        const response = await axios.post('http://localhost:9999/api/chat', {
          messages: [
            {
              role: 'user',
              content: userMessage
            }
          ]
        }, {
          headers: {
            'Authorization': `Bearer ${user.token}`
          }
        })

        if (response.data.error) {
          throw new Error(response.data.error)
        }

        messages.value.push({
          role: 'assistant',
          content: response.data.choices[0].message.content
        })
      } catch (error) {
        console.error('Error sending message:', error)
        messages.value.push({
          role: 'assistant',
          content: '抱歉，我遇到了一些问题，请稍后再试。错误信息：' + (error.response?.data?.error || error.message)
        })
      } finally {
        isLoading.value = false
        await scrollToBottom()
      }
    }

    onMounted(() => {
      // 检查是否存在心理测评结果
      const testResultData = localStorage.getItem('aiAssistantContext')
      
      if (testResultData) {
        try {
          const testResult = JSON.parse(testResultData)
          if (testResult.type === 'psychological_test_result') {
            // 有心理测评结果，生成初始化消息
            const { data } = testResult
            let initialMessage = '你好！我是你的小愿助手。'
            
            // 根据测评结果生成个性化问候
            initialMessage += `我看到你刚刚完成了"${data.testType}"测试，测试结果显示你属于"${data.resultLevel}"类型。`
            initialMessage += `这意味着${data.resultDescription} `
            initialMessage += `基于这个结果，我可以为你提供一些个性化的建议和支持。有什么我可以帮助你的吗？`
            
            messages.value.push({
              role: 'assistant',
              content: initialMessage
            })
            
            // 将测评结果发送给后端，更新AI上下文
            sendTestResultToBackend(data)
          } else {
            // 没有心理测评结果或格式不正确，显示默认问候语
            defaultGreeting()
          }
        } catch (error) {
          console.error('解析测评结果出错:', error)
          defaultGreeting()
        }
        
        // 读取后清除，避免重复使用
        localStorage.removeItem('aiAssistantContext')
      } else {
        // 没有心理测评结果，显示默认问候语
        defaultGreeting()
      }
    })
    
    // 默认问候语
    const defaultGreeting = () => {
      messages.value.push({
        role: 'assistant',
        content: '你好！我是你的AI心理咨询助手，有什么我可以帮你的吗？'
      })
    }
    
    // 将测评结果发送给后端，更新AI上下文
    const sendTestResultToBackend = async (testResult) => {
      try {
        const user = JSON.parse(localStorage.getItem('code_user') || '{}')
        await axios.post('http://localhost:9999/api/chat/context', {
          context_type: 'psychological_test',
          context_data: testResult
        }, {
          headers: {
            'Authorization': `Bearer ${user.token}`
          }
        })
        console.log('测评结果已发送到后端，AI上下文已更新')
      } catch (error) {
        console.error('发送测评结果到后端出错:', error)
      }
    }

    return {
      messages,
      userInput,
      isLoading,
      messagesContainer,
      sendMessage
    }
  }
}
</script>

<style scoped>
.ai-chat-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
}

.chat-header {
  text-align: center;
  margin-bottom: 20px;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.chat-header h2 {
  margin: 0;
  font-size: 24px;
}

.chat-header p {
  margin: 10px 0 0;
  font-size: 14px;
}

.chat-messages {
  height: 500px;
  overflow-y: auto;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.message {
  display: flex;
  gap: 10px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  margin-left: 150px;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  background-color: white;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 16px;
}

.user-message .message-content {
  background-color: #9bcfba;
  color: white;
  font-size: 16px;
}

.chat-input {
  display: flex;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.chat-input textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: none;
  height: 50px;
  font-size: 14px;
}

.chat-input button {
  width: 50px;
  height: 50px;
  border: none;
  border-radius: 8px;
  background-color: #9bcfba;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-input button:hover {
  background-color: #8abba7;
}

.chat-input button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 8px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background-color: #9bcfba;
  border-radius: 50%;
  animation: typing 1s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}
</style> 