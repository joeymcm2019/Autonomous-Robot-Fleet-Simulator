<script setup lang="ts">
import type { Robot } from '../fleet'

defineProps<{ robot: Robot; color: string; selected: boolean }>()
</script>

<template>
  <TresGroup
    :position="[robot.position.x, 0, robot.position.y]"
    :user-data="{ robotId: robot.id }"
    :rotation="[0, (robot.headingDegrees * Math.PI) / 180, 0]"
  >
    <TresMesh
      v-if="selected"
      :position="[0, 0.08, 0]"
      :rotation="[-Math.PI / 2, 0, 0]"
    >
      <TresRingGeometry :args="[0.77, 0.84, 40]" />
      <TresMeshBasicMaterial :color="color" />
    </TresMesh>
    <TresMesh :position="[0, 0.24, 0]" cast-shadow>
      <TresBoxGeometry :args="[0.99, 0.37, 1.12]" />
      <TresMeshStandardMaterial
        :color="color"
        :emissive="color"
        :emissive-intensity="0.22"
      />
    </TresMesh>
    <TresMesh :position="[0, 0.46, 0]">
      <TresBoxGeometry :args="[0.74, 0.19, 0.76]" />
      <TresMeshStandardMaterial color="#172836" />
    </TresMesh>
    <TresMesh :position="[0, 0.57, 0.22]">
      <TresBoxGeometry :args="[0.48, 0.04, 0.11]" />
      <TresMeshBasicMaterial :color="color" />
    </TresMesh>
  </TresGroup>
</template>
