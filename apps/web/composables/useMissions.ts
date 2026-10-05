import type { MissionDetail, MissionListItem, MissionStatus, Proposal, ProposalRequest } from '~/types/api'

export function useMissions() {
  const config = useRuntimeConfig()
  const baseURL = config.public.apiBase as string

  async function listMissions(status?: MissionStatus): Promise<MissionListItem[]> {
    const res = await $fetch<any[]>('/missions', {
      baseURL,
      query: status ? { status } : undefined,
    })
    return res.map((m) => ({ ...m, createdAt: new Date(m.createdAt) }))
  }

  function getMission(id: number | string): Promise<MissionDetail> {
    return $fetch<MissionDetail>(`/missions/${id}`, { baseURL })
  }

  function createProposal(missionId: number | string, body: ProposalRequest): Promise<Proposal> {
    return $fetch<Proposal>(`/missions/${missionId}/proposals`, {
      baseURL,
      method: 'POST',
      body,
    })
  }

  return { listMissions, getMission, createProposal }
}
