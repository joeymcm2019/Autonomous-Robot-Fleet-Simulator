<script setup lang="ts">
defineProps<{ x: number; z: number }>()
const posts = [
  [-1.47, -0.43],
  [1.47, -0.43],
  [-1.47, 0.43],
  [1.47, 0.43],
] as const
const levels = [0.22, 0.86, 1.5]
const cartons = [
  { x: -0.99, width: 0.61, height: 0.38, color: '#b38250' },
  { x: -0.26, width: 0.67, height: 0.43, color: '#9b6943' },
  { x: 0.65, width: 0.87, height: 0.32, color: '#bd9169' },
]
</script>

<template>
  <TresGroup :position="[x, 0, z]">
    <TresMesh
      v-for="[dx, dz] in posts"
      :key="`${dx},${dz}`"
      :position="[dx, 0.88, dz]"
      cast-shadow
    >
      <TresBoxGeometry :args="[0.105, 1.76, 0.12]" />
      <TresMeshStandardMaterial color="#668198" />
    </TresMesh>
    <TresGroup v-for="level in levels" :key="level" :position="[0, level, 0]">
      <TresMesh cast-shadow receive-shadow>
        <TresBoxGeometry :args="[3, 0.105, 0.92]" />
        <TresMeshStandardMaterial color="#526d85" />
      </TresMesh>
      <TresMesh :position="[0, 0.015, 0.475]">
        <TresBoxGeometry :args="[2.82, 0.075, 0.04]" />
        <TresMeshStandardMaterial color="#c58442" />
      </TresMesh>
      <TresMesh
        v-for="carton in cartons"
        :key="carton.x"
        :position="[carton.x, carton.height / 2 + 0.056, 0]"
        cast-shadow
      >
        <TresBoxGeometry :args="[carton.width, carton.height, 0.65]" />
        <TresMeshStandardMaterial :color="carton.color" />
      </TresMesh>
    </TresGroup>
  </TresGroup>
</template>
