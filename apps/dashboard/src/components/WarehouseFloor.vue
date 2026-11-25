<script setup lang="ts">
import { computed } from 'vue'
import { BufferGeometry, Vector3 } from 'three'

const props = defineProps<{ width: number; height: number }>()
const grid = computed(() => {
  const lines: Vector3[] = []
  for (let x = 0; x <= props.width; x++) {
    lines.push(
      new Vector3(x - 0.5, 0.012, -0.5),
      new Vector3(x - 0.5, 0.012, props.height - 0.5),
    )
  }
  for (let z = 0; z <= props.height; z++) {
    lines.push(
      new Vector3(-0.5, 0.012, z - 0.5),
      new Vector3(props.width - 0.5, 0.012, z - 0.5),
    )
  }
  return new BufferGeometry().setFromPoints(lines)
})
</script>

<template>
  <TresMesh
    :position="[(width - 1) / 2, -0.22, (height - 1) / 2]"
    receive-shadow
  >
    <TresBoxGeometry :args="[width, 0.44, height]" />
    <TresMeshStandardMaterial color="#263444" :roughness="0.94" />
  </TresMesh>
  <TresLineSegments :geometry="grid">
    <TresLineBasicMaterial
      color="#607484"
      :transparent="true"
      :opacity="0.42"
    />
  </TresLineSegments>
</template>
