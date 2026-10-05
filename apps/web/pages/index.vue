<template>
  <div>
    <section class="hero">
      <div class="container">
        <h1 class="hero-title">{{ $t('search.heroTitle') }}</h1>
        <p class="hero-subtitle">{{ $t('search.heroSubtitle') }}</p>
        <form class="hero-search" role="search" @submit.prevent="onSearch">
          <svg class="hero-icon" viewBox="0 0 24 24" width="22" height="22" aria-hidden="true">
            <circle cx="11" cy="11" r="7" fill="none" stroke="currentColor" stroke-width="2" />
            <path d="M20 20 L16 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
          </svg>
          <input
            v-model="searchInput"
            type="search"
            class="hero-input"
            :placeholder="$t('search.placeholder')"
            :aria-label="$t('search.placeholder')"
          />
          <button type="submit" class="btn btn-primary hero-btn">{{ $t('search.submit') }}</button>
        </form>
      </div>
    </section>

    <div class="container page">
      <div class="filters card">
        <div class="filter">
          <span class="label">{{ $t('search.kind') }}</span>
          <div class="segmented" role="radiogroup">
            <button
              v-for="k in kinds"
              :key="k.value"
              type="button"
              role="radio"
              :aria-checked="kind === k.value"
              class="segment"
              :class="{ active: kind === k.value }"
              @click="setKind(k.value)"
            >
              {{ $t(k.label) }}
            </button>
          </div>
        </div>

        <div class="filter">
          <label class="label" for="skill">{{ $t('search.skill') }}</label>
          <input
            id="skill"
            v-model.lazy="skill"
            class="input"
            type="text"
            :placeholder="$t('search.skillPlaceholder')"
            @change="page = 0"
          />
        </div>

        <div class="filter filter-check">
          <label class="check">
            <input v-model="available" type="checkbox" @change="page = 0" />
            <span>{{ $t('search.availableOnly') }}</span>
          </label>
        </div>

        <div class="filter">
          <label class="label" for="sort">{{ $t('search.sort') }}</label>
          <select id="sort" v-model="sort" class="select" @change="page = 0">
            <option value="relevance">{{ $t('search.sortRelevance') }}</option>
            <option value="rating">{{ $t('search.sortRating') }}</option>
            <option value="missions">{{ $t('search.sortMissions') }}</option>
          </select>
        </div>
      </div>

      <div v-if="loading" class="state">{{ $t('common.loading') }}</div>

      <div v-else-if="error" class="state state-error">
        <p>{{ $t('common.error') }}</p>
        <button class="btn btn-secondary" @click="shouldFetch = true">{{ $t('common.retry') }}</button>
      </div>

      <template v-else>
        <p class="count">{{ $t('search.results', total) }}</p>

        <div v-if="talents.length === 0" class="state empty">
          <h2>{{ $t('search.emptyTitle') }}</h2>
          <p>{{ $t('search.emptyHint') }}</p>
        </div>

        <div v-else class="grid">
          <TalentCard v-for="t in talents" :key="t.id" :talent="t" />
        </div>

        <div v-if="total > 0" class="pagination">
          <button class="btn btn-secondary" :disabled="page === 0" @click="page = page - 1">
            {{ $t('common.previous') }}
          </button>
          <span class="muted">{{ $t('common.page', { page: page + 1, pages: totalPages }) }}</span>
          <button class="btn btn-secondary" :disabled="page + 1 >= totalPages" @click="page = page + 1">
            {{ $t('common.next') }}
          </button>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Talent, TalentKind, TalentPage } from '~/types/api'

const route = useRoute()
const router = useRouter()

const kinds = [
  { value: '', label: 'kind.all' },
  { value: 'HUMAN', label: 'kind.HUMAN' },
  { value: 'AGENT', label: 'kind.AGENT' },
  { value: 'HYBRID', label: 'kind.HYBRID' },
]

const searchInput = ref('')
const q = ref('')
const kind = ref<TalentKind | ''>('')
const skill = ref('')
const available = ref(false)
const sort = ref('relevance')
const page = ref(0)
const size = 12

const filtersSnapshot = ref<any>({})
const queryString = ref('')
const shouldFetch = ref(false)

const talents = ref<Talent[]>([])
const total = ref(0)
const loading = ref(true)
const error = ref<any>(null)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / size)))

function onSearch() {
  q.value = searchInput.value
  page.value = 0
}

function setKind(value: string) {
  kind.value = value as TalentKind | ''
  page.value = 0
}

async function fetchTalents() {
  loading.value = true
  error.value = null
  let url = 'http://localhost:8080/api/talents?'
  url += 'sort=' + sort.value
  url += '&page=' + page.value
  url += '&size=' + size
  if (q.value.trim()) url += '&q=' + encodeURIComponent(q.value.trim())
  if (kind.value) url += '&kind=' + kind.value
  if (skill.value.trim()) url += '&skill=' + encodeURIComponent(skill.value.trim())
  if (available.value) url += '&available=true'
  try {
    const res = await $fetch<TalentPage>(url)
    talents.value = res.items
    total.value = res.total || 0
  } catch (e) {
    console.error(e)
    error.value = e
    talents.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

watch(shouldFetch, (val) => {
  if (!val) return
  shouldFetch.value = false
  if (import.meta.server) return
  fetchTalents()
}, { immediate: true })

watch(() => route.query, (query) => {
  q.value = (query.q as string) || ''
  searchInput.value = q.value
  kind.value = ['HUMAN', 'AGENT', 'HYBRID'].includes(query.kind as string) ? (query.kind as TalentKind) : ''
  skill.value = (query.skill as string) || ''
  available.value = query.available === 'true'
  sort.value = ['rating', 'missions'].includes(query.sort as string) ? (query.sort as string) : 'relevance'
  page.value = query.page ? parseInt(query.page as string) || 0 : 0
  shouldFetch.value = true
}, { immediate: true, deep: true })

watch([q, kind, skill, available, sort, page], () => {
  filtersSnapshot.value = {
    q: q.value,
    kind: kind.value,
    skill: skill.value,
    available: available.value,
    sort: sort.value,
    page: page.value,
  }
}, { immediate: true })

watch(filtersSnapshot, (snap) => {
  const params = new URLSearchParams()
  if (snap.q) params.set('q', snap.q)
  if (snap.kind) params.set('kind', snap.kind)
  if (snap.skill) params.set('skill', snap.skill)
  if (snap.available) params.set('available', 'true')
  if (snap.sort && snap.sort !== 'relevance') params.set('sort', snap.sort)
  if (snap.page > 0) params.set('page', String(snap.page))
  queryString.value = params.toString()
}, { deep: true, immediate: true })

watch(queryString, (qs) => {
  const query: Record<string, string> = {}
  new URLSearchParams(qs).forEach((value, key) => {
    query[key] = value
  })
  if (JSON.stringify(query) === JSON.stringify(route.query)) {
    shouldFetch.value = true
    return
  }
  router.replace({ query })
})
</script>

<style scoped>
.hero {
  background: var(--molt-neutral-0);
  padding: 72px 0 64px;
  text-align: center;
  border-bottom: 1px solid var(--molt-border);
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  max-width: 760px;
  margin: 0 auto 12px;
}

.hero-subtitle {
  font-size: 18px;
  color: var(--molt-muted);
  margin: 0 auto 36px;
  max-width: 620px;
}

.hero-search {
  display: flex;
  align-items: center;
  max-width: 720px;
  margin: 0 auto;
  background: var(--molt-neutral-0);
  border: 1px solid var(--molt-border);
  border-radius: var(--molt-radius-pill);
  box-shadow: var(--molt-shadow-md);
  padding: 6px 6px 6px 22px;
}

.hero-search:focus-within {
  border-color: var(--molt-secondary);
}

.hero-icon {
  color: var(--molt-muted);
  flex-shrink: 0;
}

.hero-input {
  flex: 1;
  border: none;
  outline: none;
  font-family: var(--molt-font-body);
  font-size: 17px;
  padding: 12px 14px;
  background: transparent;
  color: var(--molt-text);
}

.hero-btn {
  padding: 14px 28px;
  font-size: 16px;
}

.filters {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 24px;
  margin-bottom: 32px;
}

.filter {
  min-width: 180px;
}

.filter-check {
  display: flex;
  align-items: center;
  height: 42px;
}

.check {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  cursor: pointer;
}

.check input {
  width: 18px;
  height: 18px;
  accent-color: var(--molt-primary);
}

.segmented {
  display: inline-flex;
  background: var(--molt-bg);
  border-radius: var(--molt-radius-pill);
  padding: 4px;
}

.segment {
  border: none;
  background: transparent;
  font-family: var(--molt-font-body);
  font-weight: 500;
  font-size: 14px;
  padding: 7px 16px;
  border-radius: var(--molt-radius-pill);
  cursor: pointer;
  color: var(--molt-muted);
}

.segment.active {
  background: var(--molt-neutral-0);
  color: var(--molt-secondary);
  font-weight: 700;
  box-shadow: var(--molt-shadow-sm);
}

.count {
  font-weight: 700;
  margin: 0 0 16px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.empty h2 {
  font-size: 22px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 40px;
}

@media (max-width: 640px) {
  .hero-title {
    font-size: 34px;
  }

  .hero-btn {
    padding: 12px 18px;
  }
}
</style>
