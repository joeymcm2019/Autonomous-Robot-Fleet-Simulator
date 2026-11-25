<script setup lang="ts">
import { onErrorCaptured, ref } from 'vue'
import { TresCanvas } from '@tresjs/core'
import { DirectionalLight, Object3D } from 'three'
import { robotColors, type Robot } from '../fleet'
import WarehouseCamera from './WarehouseCamera.vue'
import WarehouseFloor from './WarehouseFloor.vue'
import WarehouseShelf from './WarehouseShelf.vue'
import WarehouseZone from './WarehouseZone.vue'
import ChargingStation from './ChargingStation.vue'
import RobotModel from './RobotModel.vue'

defineProps<{
  robots: Robot[]
  selectedId: string | null
  width: number
  height: number
}>()
defineEmits<{ select: [id: string] }>()
const keyLight = new DirectionalLight('#ffffff', 2.25)
keyLight.position.set(3, 18, 8)
keyLight.target = new Object3D()
keyLight.target.position.set(9, 0, 7)
keyLight.castShadow = true
keyLight.shadow.mapSize.set(1024, 1024)
Object.assign(keyLight.shadow.camera, {
  left: -20,
  right: 20,
  top: 20,
  bottom: -20,
})
keyLight.shadow.camera.updateProjectionMatrix()
keyLight.shadow.normalBias = 0.025

const renderError = ref(false)
onErrorCaptured(() => {
  renderError.value = true
  return false
})
</script>

<template>
  <div v-if="renderError" class="scene-error" role="status">
    3D rendering is unavailable. Robot details remain available in the
    inspector.
  </div>
  <TresCanvas v-else clear-color="#111923" shadows>
    <WarehouseCamera
      :width="width"
      :height="height"
      @select="$emit('select', $event)"
    />
    <TresAmbientLight :intensity="1.22" />
    <primitive :object="keyLight" />
    <primitive :object="keyLight.target" />
    <TresDirectionalLight
      :position="[20, 7, 15]"
      :intensity="0.8"
      color="#91b8f1"
    />
    <WarehouseFloor :width="width" :height="height" />
    <template v-for="z in [2, 5, 8]" :key="z">
      <WarehouseShelf v-for="x in [3, 8, 14]" :key="x" :x="x" :z="z" />
    </template>
    <ChargingStation />
    <WarehouseZone :x="8" :z="13.35" color="#e6a64b">
      <TresMesh
        v-for="x in [-0.92, 0, 0.92]"
        :key="x"
        :position="[x, 0.27, 0.25]"
        cast-shadow
      >
        <TresBoxGeometry :args="[0.68, 0.46, 0.63]" />
        <TresMeshStandardMaterial color="#d49b59" />
      </TresMesh>
    </WarehouseZone>
    <WarehouseZone :x="13.6" :z="13.35" color="#6699fa" />
    <RobotModel
      v-for="(robot, index) in robots"
      :key="robot.id"
      :robot="robot"
      :color="robotColors[index % robotColors.length]!"
      :selected="selectedId === robot.id"
    />
  </TresCanvas>
</template>
