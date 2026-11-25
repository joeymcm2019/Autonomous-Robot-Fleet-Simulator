<script setup lang="ts">
import { robotColors, type Robot } from '../fleet'
import Icon from './Icon.vue'

defineProps<{ robots: Robot[]; selected?: Robot; loading: boolean }>()
defineEmits<{ select: [id: string] }>()
</script>

<template>
  <section class="panel robot-panel" aria-labelledby="robot-heading">
    <header class="panel-header">
      <div class="panel-title">
        <Icon name="robots" />
        <h2 id="robot-heading">Robot inspector</h2>
      </div>
    </header>
    <div v-if="selected" class="robot-detail">
      <div class="robot-identity">
        <span class="metric-icon"><Icon name="robots" :size="23" /></span>
        <div>
          <h3>{{ selected.id }}</h3>
          <p>Autonomous mobile robot</p>
        </div>
      </div>
      <dl>
        <div>
          <dt>Status</dt>
          <dd>
            <span class="robot-status" :class="selected.status.toLowerCase()"
              ><i />{{
                selected.status.replace('ROBOT_STATUS_UNSPECIFIED', 'UNKNOWN')
              }}</span
            >
          </dd>
        </div>
        <div>
          <dt>Grid position</dt>
          <dd>X {{ selected.position.x }} · Y {{ selected.position.y }}</dd>
        </div>
        <div>
          <dt>Heading</dt>
          <dd>{{ selected.headingDegrees }}°</dd>
        </div>
        <div>
          <dt>Battery</dt>
          <dd>{{ selected.batteryPercent }}%</dd>
        </div>
      </dl>
      <progress
        :value="selected.batteryPercent"
        max="100"
        aria-label="Robot battery level"
      />
    </div>
    <p v-else class="empty">
      {{ loading ? 'Loading robot state…' : 'No robot state available.' }}
    </p>
    <div v-if="robots.length" class="robot-list">
      <h3>FLEET ROBOTS</h3>
      <button
        v-for="(robot, index) in robots"
        :key="robot.id"
        type="button"
        :aria-pressed="robot.id === selected?.id"
        @click="$emit('select', robot.id)"
      >
        <i
          :style="{ background: robotColors[index % robotColors.length] }"
        /><span>{{ robot.id }}</span
        ><small>{{ robot.batteryPercent }}%</small>
      </button>
    </div>
  </section>
</template>
