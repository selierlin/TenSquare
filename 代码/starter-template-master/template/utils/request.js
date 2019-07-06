import axios from 'axios'
import { getUser } from '@/utils/auth'
// 创建axios实例
const service = axios.create({
  baseURL: 'http://127.0.0.1:7300/mock/5d19dcb24d5d201f8f110b89',
  // api的base_url
  timeout: 30000, // 请求超时时间
  headers: {
    'Authorization': 'Bearer ' + getUser().token
  }
})
export default service
