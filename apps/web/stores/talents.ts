import { defineStore } from 'pinia'
import type { TalentDetail } from '~/types/api'

export const useTalentsStore = defineStore('talents', {
  state: () => ({
    byId: {} as Record<string, TalentDetail>,
    current: null as TalentDetail | null,
    loading: false,
  }),
  getters: {
    operatorOf: (state) => (talent: any) => {
      const opId = talent?.agent?.operator?.id
      return opId ? state.byId[String(opId)] || null : null
    },
  },
  actions: {
    async fetchTalent(id: number | string, force = false) {
      const key = String(id)
      if (!force && this.byId[key]) {
        this.current = this.byId[key]
        return this.current
      }
      this.loading = true
      try {
        const apiBase = useRuntimeConfig().public.apiBase
        const talent = await $fetch<TalentDetail>(apiBase + '/talents/' + key)
        this.byId[key] = talent
        this.current = talent
        return talent
      } finally {
        this.loading = false
      }
    },
  },
})
