// src/api/request.js
import axios from 'axios'

const service = axios.create({
    baseURL: 'http://localhost:8080/api', // 基础URL
    timeout: 5000    // 超时时间
})

export default service