<script setup lang="ts">
defineProps<{ name: string; size?: number }>()
const paths: Record<string, string> = {
  overview: 'M3 3h7v7H3z M14 3h7v7h-7z M3 14h7v7H3z M14 14h7v7h-7z',
  simulation: 'm12 2 9 5v10l-9 5-9-5V7z M3 7l9 5 9-5 M12 12v10',
  robots:
    'M8 9h8a3 3 0 0 1 3 3v6H5v-6a3 3 0 0 1 3-3z M12 9V5 M10 5h4 M8 13h1 M15 13h1 M5 15H3m16 0h2',
  tasks: 'M8 4H5v17h14V4h-3 M8 2h8v5H8z M8 12h8m-8 4h6',
  analytics: 'M4 4v16h16 M8 16v-4m4 4V7m4 9v-6m4 6V5',
  settings:
    'M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8z M12 2v3m0 14v3M2 12h3m14 0h3M5 5l2 2m10 10 2 2M5 19l2-2M17 7l2-2',
  panel: 'M3 4h18v16H3z M9 4v16',
  refresh: 'M20 10a8 8 0 1 0-2 8 M20 4v6h-6',
  battery: 'M3 7h16v10H3z m16 3h2v4h-2 M6 10v4m3-4v4m3-4v4',
  moving: 'M2 12h5l3-7 4 14 3-7h5',
  grid: 'M3 3h18v18H3z M3 9h18M3 15h18M9 3v18M15 3v18',
  reset: 'M8 3H3v5m13-5h5v5M3 16v5h5m13-5v5h-5 M12 9v6m-3-3h6',
}
</script>

<template>
  <svg
    :width="size ?? 18"
    :height="size ?? 18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.6"
    stroke-linecap="round"
    stroke-linejoin="round"
    aria-hidden="true"
  >
    <path :d="paths[name]" />
  </svg>
</template>
