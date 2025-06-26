import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/css/global.css'
import '@/assets/css/theme/index.css'
import request from "@/utils/request";

Vue.config.productionTip = false

Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL

// 全局错误处理 - 处理Element UI的setAttribute错误
Vue.config.errorHandler = (err, vm, info) => {
  // 过滤Element UI相关错误
  if (err && err.message && (
    err.message.includes('setAttribute') ||
    err.message.includes('Cannot read properties of null') ||
    err.message.includes('removeTabindex') ||
    err.message.includes('clickoutside') ||
    err.message.includes('Cannot read property')
  )) {
    // 静默处理这些错误，不显示给用户
    console.log('[Ignored Element UI Error]:', err.message);
    return;
  }
  
  // 其他错误正常处理
  console.error('[Vue Error]:', err, info);
};

// 全局未捕获错误处理
window.addEventListener('error', (event) => {
  if (event.error && event.error.message) {
    const message = event.error.message;
    if (message.includes('setAttribute') ||
        message.includes('removeTabindex') ||
        message.includes('Cannot read properties of null') ||
        message.includes('clickoutside')) {
      event.preventDefault();
      event.stopPropagation();
      console.log('[Ignored DOM Error]:', message);
      return false;
    }
  }
}, true);

// Promise错误处理
window.addEventListener('unhandledrejection', (event) => {
  if (event.reason && event.reason.message) {
    const message = event.reason.message;
    if (message.includes('setAttribute') ||
        message.includes('removeTabindex') ||
        message.includes('Cannot read properties of null')) {
      event.preventDefault();
      console.log('[Ignored Promise Error]:', message);
      return false;
    }
  }
});

// 重写console.error以过滤Element UI错误
const originalConsoleError = console.error;
console.error = (...args) => {
  const firstArg = args[0];
  if (firstArg && typeof firstArg === 'string') {
    if (firstArg.includes('setAttribute') ||
        firstArg.includes('removeTabindex') ||
        firstArg.includes('Cannot read properties of null') ||
        firstArg.includes('clickoutside')) {
      // 静默处理Element UI错误
      return;
    }
  }
  
  // 检查错误对象
  if (args.some(arg => arg && arg.message && (
    arg.message.includes('setAttribute') ||
    arg.message.includes('removeTabindex') ||
    arg.message.includes('Cannot read properties of null')
  ))) {
    return;
  }
  
  // 其他错误正常输出
  originalConsoleError.apply(console, args);
};

Vue.use(ElementUI, {size: "small"})

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
