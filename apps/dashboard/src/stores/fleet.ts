import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import {
  getApi,
  type Health,
  type Robot,
  type SimulationStatus,
} from '../fleet'

export const useFleet = defineStore('fleet', () => {
  const robots = ref<Robot[]>([])
  const simulation = ref<SimulationStatus | null>(null)
  const health = ref<Health | null>(null)
  const loading = ref(false)
  const error = ref('')
  const selectedId = ref<string | null>(null)
  const selected = computed(() =>
    robots.value.find((robot) => robot.id === selectedId.value),
  )

  function select(id: string) {
    if (robots.value.some((robot) => robot.id === id)) selectedId.value = id
  }

  async function refresh() {
    if (loading.value) return
    loading.value = true
    error.value = ''
    try {
      const [healthResult, robotsResult, statusResult] =
        await Promise.allSettled([
          getApi<Health>('health'),
          getApi<Robot[]>('robots'),
          getApi<SimulationStatus>('simulation/status'),
        ])
      health.value =
        healthResult.status === 'fulfilled' ? healthResult.value : null
      simulation.value =
        statusResult.status === 'fulfilled' ? statusResult.value : null
      robots.value =
        robotsResult.status === 'fulfilled' ? robotsResult.value : []
      if (!selected.value) selectedId.value = robots.value[0]?.id ?? null

      if (!health.value?.apiOperational) {
        error.value =
          'The Fleet API is unreachable. Check the connection and retry.'
      } else if (!health.value.simulationReachable) {
        error.value =
          'The simulation engine is unavailable. Check the connection and retry.'
      } else if (
        robotsResult.status === 'rejected' ||
        statusResult.status === 'rejected'
      ) {
        error.value =
          'Fleet state could not be loaded. Try refreshing the snapshot.'
      }
    } finally {
      loading.value = false
    }
  }

  return {
    robots,
    simulation,
    health,
    loading,
    error,
    selected,
    selectedId,
    select,
    refresh,
  }
})
