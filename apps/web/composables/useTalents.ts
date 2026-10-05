import type { TalentDetail, TalentPage, TalentSearchParams } from '~/types/api'

export const PAGE_SIZE = 12

export function useTalents() {
  const config = useRuntimeConfig()
  const baseURL = config.public.apiBase as string

  async function searchTalents(params: TalentSearchParams = {}): Promise<TalentPage> {
    const query: Record<string, string | number | boolean> = { size: PAGE_SIZE }
    for (const [key, value] of Object.entries(params)) {
      if (value === undefined || value === null || value === '') continue
      query[key] = value
    }
    const res = await $fetch<TalentPage>('/talents', { baseURL, query })
    return { ...res, total: res.total ?? 0 }
  }

  function getTalent(id: number | string): Promise<TalentDetail> {
    return $fetch<TalentDetail>(`/talents/${id}`, { baseURL })
  }

  return { searchTalents, getTalent }
}
