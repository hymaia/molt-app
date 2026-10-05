export type TalentKind = 'HUMAN' | 'AGENT' | 'HYBRID'

export type MissionStatus = 'OPEN' | 'CONTRACTED' | 'DONE'

export type TalentSort = 'relevance' | 'rating' | 'missions'

export interface Talent {
  id: number
  kind: TalentKind
  name: string
  title: string
  location?: string
  avatarUrl?: string
  skills: string[]
  dailyRateCents: number
  rating: number
  missionCount: number
  available: boolean
  agent: AgentProfile | null
}

export interface TalentRef {
  id: number
  name: string
}

export interface AgentProfile {
  model: string
  tools: string[]
  operator?: TalentRef | any
}

export interface Review {
  author: string
  rating: number
  comment: string
  createdAt: string
}

export interface TalentDetail extends Talent {
  bio: string
  reviews: Review[]
}

export interface TalentPage {
  items: Talent[]
  page: number
  size: number
  total?: number
}

export interface TalentSearchParams {
  q?: string
  kind?: TalentKind
  skill?: string
  available?: boolean
  sort?: TalentSort
  page?: number
  size?: number
}

export interface Client {
  id: number
  kind: TalentKind
  name: string
}

export interface Mission {
  id: number
  title: string
  client: Client
  skills: string[]
  status: MissionStatus
  durationDays: number
  remote?: boolean
  createdAt: string
}

export interface Proposal {
  id: number
  talentId: number
  talentName: string
  dailyRateCents: number
  message: string
  createdAt: string
}

export interface MissionDetail extends Mission {
  description: string
  proposals: Proposal[]
}

export type MissionListItem = Omit<Mission, 'createdAt'> & { createdAt: Date }

export interface ProposalRequest {
  talentId: number
  dailyRateCents: number
  message: string
}

export interface ApiError {
  code: string
  message: string
}
