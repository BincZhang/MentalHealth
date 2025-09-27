import axios from 'axios'

const API_BASE_URL = 'http://localhost:9999'

export const chatWithAI = async (messages) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/api/chat`, {
      messages: messages
    })
    return response.data
  } catch (error) {
    console.error('Error in chatWithAI:', error)
    throw error
  }
} 