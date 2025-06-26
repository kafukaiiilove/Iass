import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'

// 创建axios实例
const service = axios.create({
    baseURL: '/api',
    timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(config => {
    console.log('【请求拦截器】开始处理请求:', {
        url: config.url,
        method: config.method,
        baseURL: config.baseURL,
        fullURL: config.baseURL + config.url,
        headers: config.headers
    });
    
    // 处理请求路径
    if (config.url.startsWith('/')) {
        // 如果url以斜杠开头，移除斜杠
        config.url = config.url.substring(1);
        console.log('【请求拦截器】移除开头的斜杠，新url:', config.url);
    }
    
    // 如果是文件上传请求，不设置Content-Type，让浏览器自动设置
    if (config.headers['Content-Type'] === 'multipart/form-data') {
        delete config.headers['Content-Type'];
    } else {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
    }
    
    // 获取缓存的用户信息，并设置 token
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
    const token = user.token;

    if (token) {
        config.headers['token'] = token;
        console.log('【请求拦截器】添加token:', token);
    } else {
        console.log('【请求拦截器】未找到token');
    }

    return config;
}, error => {
    console.error('【请求拦截器】请求错误:', error);
    return Promise.reject(error);
});

// 响应拦截器
service.interceptors.response.use(response => {
    console.log('【响应拦截器】收到响应:', {
        status: response.status,
        statusText: response.statusText,
        data: response.data,
        headers: response.headers
    });
    
    let res = response.data;

    // 兼容服务端返回的字符串数据
    if (typeof res === 'string') {
        res = res ? JSON.parse(res) : res;
    }

    // 判断接口返回状态码
    if (res.code === '401' || res.code === 401) {
        console.log('【响应拦截器】未授权，跳转到登录页面');
        // 清除用户信息
        localStorage.removeItem('xm-user');
        // 跳转到登录页
        router.push('/login');
        Message.error('token验证失败，请重新登录');
        return Promise.reject(res);
    } else if (res.code !== '200' && res.code !== 200) {
        console.log('【响应拦截器】请求失败:', res.msg || '系统错误');
        Message.error(res.msg || '系统错误');
        return Promise.reject(res);
    }

    return res;
}, error => {
    console.error('【响应拦截器】响应错误:', {
        message: error.message,
        response: error.response,
        config: error.config
    });
    Message.error(error.message);
    return Promise.reject(error);
});

export default service;
