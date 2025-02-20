import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './plugins/router.ts';
import { apiClient } from './logic/api-client.ts';

(async () => {
    await apiClient.initialize();

    const app = createApp(App);
    app.use(router);
    app.mount('#app');
})();