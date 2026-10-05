import { defineStore } from 'pinia'
import type { MissionStatus } from '~/types/api'

export const useMissionFiltersStore = defineStore('missionFilters', {
  state: () => ({
    status: null as MissionStatus | null,
    lastChangedAt: 0,
  }),
  actions: {
    setStatus(status: MissionStatus | null) {
      this.status = status
      this.lastChangedAt = Date.now()
    },
  },
})
