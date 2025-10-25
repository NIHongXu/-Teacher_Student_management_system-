import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';

axios.defaults.baseURL = '/api';

Vue.prototype.$axios = axios;

Vue.config.productionTip = false

Vue.use(ElementUI, { size: "small" });

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
