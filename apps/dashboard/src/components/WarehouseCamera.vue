<script setup lang="ts">
import { OrbitControls } from '@tresjs/cientos'
import { useTresContext } from '@tresjs/core'
import { Mesh, Raycaster, Vector2 } from 'three'
import { onMounted, onUnmounted } from 'vue'

const emit = defineEmits<{ select: [id: string] }>()
const { camera, renderer, scene } = useTresContext()
const ray = new Raycaster()
let press: { id: number; x: number; y: number } | undefined

function start(event: globalThis.PointerEvent) {
  if (event.isPrimary && event.button === 0) {
    press = { id: event.pointerId, x: event.clientX, y: event.clientY }
  }
}

function cancel() {
  press = undefined
}

function select(event: globalThis.PointerEvent) {
  const start = press
  cancel()
  if (!start || start.id !== event.pointerId || !camera.value) return
  if (Math.hypot(event.clientX - start.x, event.clientY - start.y) > 5) return

  // Raycast at release so taps and clicks work without a preceding hover.
  const bounds = renderer.value.domElement.getBoundingClientRect()
  const cursor = new Vector2(
    ((event.clientX - bounds.left) / bounds.width) * 2 - 1,
    1 - ((event.clientY - bounds.top) / bounds.height) * 2,
  )
  ray.setFromCamera(cursor, camera.value)
  const hit = ray
    .intersectObjects(scene.value.children)
    .find(({ object }) => object instanceof Mesh)
  for (let object = hit?.object; object; object = object.parent ?? undefined) {
    if (typeof object.userData.robotId === 'string') {
      emit('select', object.userData.robotId)
      break
    }
  }
}

const canvas = renderer.value.domElement
onMounted(() => {
  canvas.addEventListener('pointerdown', start)
  canvas.addEventListener('pointerup', select)
  canvas.addEventListener('pointercancel', cancel)
})
onUnmounted(() => {
  canvas.removeEventListener('pointerdown', start)
  canvas.removeEventListener('pointerup', select)
  canvas.removeEventListener('pointercancel', cancel)
})

defineProps<{ width: number; height: number }>()
</script>

<template>
  <TresPerspectiveCamera
    :position="[width * 1.44, 24, height * 1.85]"
    :fov="40"
    :near="0.1"
    :far="140"
  />
  <OrbitControls
    :target="[(width - 1) / 2, 0, (height - 1) / 2]"
    :min-distance="9"
    :max-distance="56"
    :max-polar-angle="1.46"
    :enable-damping="true"
  />
</template>
