<script setup lang="ts">
defineProps<{ x: number; z: number; color: string }>()
</script>

<template>
  <TresGroup :position="[x, 0, z]">
    <TresMesh :position="[0, 0.027, 0]">
      <TresBoxGeometry :args="[3.8, 0.05, 2.55]" />
      <TresMeshStandardMaterial
        :color="color"
        :transparent="true"
        :opacity="0.32"
      />
    </TresMesh>
    <TresMesh
      v-for="side in [-1, 1]"
      :key="side"
      :position="[side * 1.85, 0.06, 0]"
    >
      <TresBoxGeometry :args="[0.1, 0.025, 2.55]" />
      <TresMeshStandardMaterial
        :color="color"
        :emissive="color"
        :emissive-intensity="0.22"
      />
    </TresMesh>
    <slot />
  </TresGroup>
</template>
