<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { navigation } from './router'
import { useFleet } from './stores/fleet'
import Icon from './components/Icon.vue'

const collapsed = ref(false)
const fleet = useFleet()
onMounted(() => void fleet.refresh())
</script>

<template>
  <div class="shell" :class="{ collapsed }">
    <aside class="sidebar" aria-label="Application sidebar">
      <RouterLink v-slot="{ href, navigate }" to="/" custom>
        <a
          :href="href"
          class="brand"
          aria-label="Fleet Console home"
          @click="navigate"
        >
          <span class="brand-mark"><Icon name="simulation" :size="21" /></span>
          <span class="nav-text"
            ><strong>FLEET<span class="accent">.</span></strong
            ><small>SIMULATOR</small></span
          >
        </a>
      </RouterLink>
      <div class="nav-caption nav-text">WORKSPACE</div>
      <nav aria-label="Main navigation">
        <RouterLink
          v-for="item in navigation"
          v-slot="{ href, navigate, isExactActive }"
          :key="item.path"
          :to="item.path"
          custom
        >
          <a
            :href="href"
            :class="{ 'router-link-exact-active': isExactActive }"
            :aria-current="isExactActive ? 'page' : undefined"
            :title="item.label"
            :aria-label="item.label"
            class="nav-link"
            @click="navigate"
          >
            <Icon :name="item.icon" /><span class="nav-text">{{
              item.label
            }}</span>
          </a>
        </RouterLink>
      </nav>
      <footer class="sidebar-footer">
        <span class="nav-text">PHASE 01</span><span class="version">v0.1</span>
      </footer>
    </aside>
    <div class="workspace">
      <header class="topbar">
        <div class="topbar-start">
          <button
            class="icon-button"
            type="button"
            :aria-label="
              collapsed ? 'Expand navigation' : 'Collapse navigation'
            "
            :aria-expanded="!collapsed"
            @click="collapsed = !collapsed"
          >
            <Icon name="panel" />
          </button>
          <span class="topbar-label">Warehouse simulator</span>
        </div>
        <div
          class="connections"
          aria-label="Service connections"
          aria-live="polite"
        >
          <span class="connection"
            ><i
              :class="{
                online: fleet.health?.apiOperational,
                offline: !fleet.loading && !fleet.health?.apiOperational,
              }"
            />API
            <span class="connection-state">{{
              fleet.health?.apiOperational
                ? 'online'
                : fleet.loading
                  ? 'checking'
                  : 'offline'
            }}</span></span
          >
          <span class="connection"
            ><i
              :class="{
                online: fleet.health?.simulationReachable,
                offline: !fleet.loading && !fleet.health?.simulationReachable,
              }"
            />Engine
            <span class="connection-state">{{
              fleet.health?.simulationReachable
                ? 'online'
                : fleet.loading
                  ? 'checking'
                  : 'offline'
            }}</span></span
          >
        </div>
      </header>
      <main id="main-content" class="main-content"><RouterView /></main>
    </div>
  </div>
</template>
