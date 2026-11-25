<script setup lang="ts">
import WarehouseZone from './WarehouseZone.vue'
</script>

<template>
  <WarehouseZone :x="2.2" :z="13.35" color="#4fd7a4">
    <TresGroup v-for="x in [-1, 1]" :key="x" :position="[x, 0, 0.66]">
      <TresMesh :position="[0, 0.32, 0]" cast-shadow>
        <TresBoxGeometry :args="[0.64, 0.64, 0.53]" />
        <TresMeshStandardMaterial color="#304654" />
      </TresMesh>
      <TresMesh :position="[0, 0.64, 0]">
        <TresBoxGeometry :args="[0.46, 0.035, 0.36]" />
        <TresMeshStandardMaterial
          color="#7fffd2"
          emissive="#53dbab"
          :emissive-intensity="0.5"
        />
      </TresMesh>
    </TresGroup>
  </WarehouseZone>
</template>
