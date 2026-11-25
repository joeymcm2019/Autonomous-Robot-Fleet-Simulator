import { flushPromises, mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { defineComponent, type PropType } from 'vue'
import FleetView from './FleetView.vue'
import { useFleet } from '../stores/fleet'
import type { Robot } from '../fleet'

const robots: Robot[] = [
  {
    id: 'TEST-01',
    position: { x: 4, y: 9 },
    batteryPercent: 81,
    headingDegrees: 90,
    status: 'IDLE',
  },
  {
    id: 'TEST-02',
    position: { x: 7, y: 2 },
    batteryPercent: 44,
    headingDegrees: 180,
    status: 'CHARGING',
  },
]

const SceneStub = defineComponent({
  props: {
    robots: { type: Array as PropType<Robot[]>, required: true },
    selectedId: { type: String, default: null },
    width: { type: Number, required: true },
    height: { type: Number, required: true },
  },
  emits: ['select'],
  template:
    '<button class="scene-robot" @click="$emit(\'select\', robots[1].id)">Select in scene</button>',
})

function mountView() {
  const pinia = createPinia()
  const view = mount(FleetView, {
    props: { title: 'Overview' },
    global: { plugins: [pinia], stubs: { WarehouseScene: SceneStub } },
  })
  return { view, fleet: useFleet(pinia) }
}

afterEach(() => vi.unstubAllGlobals())

describe('FleetView', () => {
  it('renders the engine snapshot and shares selection between the scene and inspector', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (url: string) => {
        if (url.endsWith('/health'))
          return Response.json({
            apiOperational: true,
            simulationReachable: true,
          })
        if (url.endsWith('/robots')) return Response.json(robots)
        return Response.json({
          tick: 0,
          running: false,
          gridWidth: 20,
          gridHeight: 16,
        })
      }),
    )
    const { view, fleet } = mountView()
    await fleet.refresh()
    await flushPromises()

    expect(view.findComponent(SceneStub).props('robots')).toEqual(robots)
    expect(view.get('.robot-detail').text()).toContain('X 4 · Y 9')
    await view.get('.scene-robot').trigger('click')
    expect(view.get('.robot-detail').text()).toContain('CHARGING')
    expect(view.get('.robot-detail progress').attributes('value')).toBe('44')
    await view.get('.robot-list button').trigger('click')
    expect(view.get('.robot-detail h3').text()).toBe('TEST-01')
    expect(view.findComponent(SceneStub).props('selectedId')).toBe('TEST-01')
    view.unmount()
  })

  it('shows a loading state and an engine error without fabricating robots', async () => {
    const pending: Array<() => void> = []
    vi.stubGlobal(
      'fetch',
      vi.fn(
        (url: string) =>
          new Promise<Response>((resolve) => {
            pending.push(() =>
              resolve(
                url.endsWith('/health')
                  ? Response.json({
                      apiOperational: true,
                      simulationReachable: false,
                    })
                  : new Response(null, { status: 503 }),
              ),
            )
          }),
      ),
    )
    const { view, fleet } = mountView()
    const request = fleet.refresh()
    await flushPromises()
    expect(view.text()).toContain('Loading robot state…')
    expect(view.get('.action-button').attributes()).toHaveProperty('disabled')

    pending.forEach((respond) => respond())
    await request
    await flushPromises()
    expect(view.get('[role="alert"]').text()).toContain(
      'simulation engine is unavailable',
    )
    expect(view.findComponent(SceneStub).props('robots')).toEqual([])
    expect(view.find('.robot-detail').exists()).toBe(false)
    view.unmount()
  })

  it('reports when the API cannot be reached', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockRejectedValue(new TypeError('network failure')),
    )
    const { view, fleet } = mountView()
    await fleet.refresh()
    await flushPromises()
    expect(view.get('[role="alert"]').text()).toContain(
      'Fleet API is unreachable',
    )
    view.unmount()
  })
})
