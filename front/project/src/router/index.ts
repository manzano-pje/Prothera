import { createRouter, createWebHistory } from 'vue-router'
import FuncionariosView from '../views/FuncionariosView.vue'
import ConsultasView from '../views/ConsultasView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/funcionarios' },
    { path: '/funcionarios', name: 'funcionarios', component: FuncionariosView },
    { path: '/consultas', name: 'consultas', component: ConsultasView },
  ],
})

export default router
