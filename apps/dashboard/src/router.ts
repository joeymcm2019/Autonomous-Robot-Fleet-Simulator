import { createRouter, createWebHistory } from 'vue-router'
import FleetView from './views/FleetView.vue'
import PlaceholderView from './views/PlaceholderView.vue'

export const navigation = [
  { label: 'Overview', path: '/', icon: 'overview' },
  { label: 'Simulation', path: '/simulation', icon: 'simulation' },
  { label: 'Robots', path: '/robots', icon: 'robots' },
  { label: 'Tasks', path: '/tasks', icon: 'tasks' },
  { label: 'Analytics', path: '/analytics', icon: 'analytics' },
  { label: 'Settings', path: '/settings', icon: 'settings' },
] as const

export const router = createRouter({
  history: createWebHistory(),
  routes: navigation.map(({ label, path }, index) => ({
    path,
    component: index < 2 ? FleetView : PlaceholderView,
    props: { title: label },
  })),
})
