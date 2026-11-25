<script setup lang="ts">
import { computed, ref } from 'vue'
import Icon from '../components/Icon.vue'
import WarehouseScene from '../components/WarehouseScene.vue'
import RobotDetailsPanel from '../components/RobotDetailsPanel.vue'
import { useFleet } from '../stores/fleet'

const props = defineProps<{ title: string }>()
const fleet = useFleet()
const cameraVersion = ref(0)
const metrics = computed(() => [
  {
    name: 'Fleet size',
    value: fleet.error && !fleet.robots.length ? '—' : fleet.robots.length,
    icon: 'robots',
  },
  {
    name: 'Robots moving',
    value: fleet.robots.length
      ? fleet.robots.filter((robot) => robot.status === 'MOVING').length
      : '—',
    icon: 'moving',
  },
  {
    name: 'Average battery',
    value: fleet.robots.length
      ? Math.round(
          fleet.robots.reduce(
            (total, robot) => total + robot.batteryPercent,
            0,
          ) / fleet.robots.length,
        ) + '%'
      : '—',
    icon: 'battery',
  },
  {
    name: 'Simulation tick',
    value: fleet.simulation?.tick ?? '—',
    icon: 'grid',
  },
])
</script>

<template>
  <header class="page-heading">
    <div>
      <h1>
        {{ props.title === 'Overview' ? 'Fleet overview' : 'Simulation' }}
      </h1>
      <p>
        {{
          props.title === 'Overview'
            ? 'Warehouse operations and robot telemetry'
            : 'Inspect the warehouse grid and engine snapshot'
        }}
      </p>
    </div>
    <button
      class="action-button"
      type="button"
      :disabled="fleet.loading"
      @click="fleet.refresh"
    >
      <Icon name="refresh" :size="16" />{{
        fleet.loading ? 'Refreshing…' : 'Refresh snapshot'
      }}
    </button>
  </header>
  <div v-if="fleet.error" class="error-banner" role="alert">
    {{ fleet.error }}
  </div>
  <section class="metrics" aria-label="Fleet metrics">
    <article v-for="metric in metrics" :key="metric.name" class="metric">
      <div>
        <div class="metric-label">{{ metric.name }}</div>
        <strong>{{ metric.value }}</strong>
      </div>
      <span class="metric-icon"><Icon :name="metric.icon" :size="19" /></span>
    </article>
  </section>
  <div class="operations">
    <section class="panel viewport" aria-labelledby="warehouse-heading">
      <header class="panel-header">
        <div class="panel-title">
          <Icon name="simulation" />
          <h2 id="warehouse-heading">Warehouse viewport</h2>
        </div>
        <span class="view-label">{{
          fleet.simulation
            ? fleet.simulation.running
              ? 'RUNNING'
              : 'STATIONARY'
            : 'ENGINE STATUS UNKNOWN'
        }}</span>
      </header>
      <div class="scene-frame">
        <WarehouseScene
          :key="cameraVersion"
          :robots="fleet.robots"
          :selected-id="fleet.selectedId"
          :width="fleet.simulation?.gridWidth ?? 20"
          :height="fleet.simulation?.gridHeight ?? 16"
          @select="fleet.select"
        />
        <div class="scene-tools">
          <button
            class="icon-button"
            type="button"
            title="Reset camera"
            aria-label="Reset camera"
            @click="cameraVersion++"
          >
            <Icon name="reset" />
          </button>
        </div>
        <div class="scene-caption">
          {{
            fleet.loading
              ? 'Loading robot state…'
              : 'Drag to orbit · Scroll to zoom · Right-drag to pan'
          }}
        </div>
      </div>
      <footer class="viewport-footer">
        <div class="legend">
          <span><i class="charging" /> Charging</span
          ><span><i class="pickup" /> Pickup</span
          ><span><i class="delivery" /> Delivery</span>
        </div>
        <span v-if="fleet.simulation" class="grid-size"
          >{{ fleet.simulation.gridWidth }} ×
          {{ fleet.simulation.gridHeight }} GRID</span
        >
      </footer>
    </section>
    <RobotDetailsPanel
      :robots="fleet.robots"
      :selected="fleet.selected"
      :loading="fleet.loading"
      @select="fleet.select"
    />
  </div>
</template>
