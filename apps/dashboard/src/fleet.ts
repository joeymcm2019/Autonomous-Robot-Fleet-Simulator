export interface Robot {
  id: string
  position: { x: number; y: number }
  headingDegrees: number
  batteryPercent: number
  status:
    | 'IDLE'
    | 'MOVING'
    | 'CHARGING'
    | 'ERROR'
    | 'ROBOT_STATUS_UNSPECIFIED'
    | 'UNRECOGNIZED'
}

export interface SimulationStatus {
  tick: number
  running: boolean
  gridWidth: number
  gridHeight: number
}

export interface Health {
  apiOperational: boolean
  simulationReachable: boolean
}

export const robotColors = ['#52eed0', '#78a9ff', '#ffc276'] as const

export async function getApi<T>(endpoint: string): Promise<T> {
  const response = await fetch('/api/' + endpoint, {
    headers: { Accept: 'application/json' },
    signal: AbortSignal.timeout(6500),
  })
  if (!response.ok) throw new Error(`Request failed (HTTP ${response.status})`)
  return response.json() as Promise<T>
}
