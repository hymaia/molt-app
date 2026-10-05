<template>
  <div class="container page">
    <header class="page-head">
      <h1>{{ $t('missions.title') }}</h1>
      <p class="muted">{{ $t('missions.subtitle') }}</p>
    </header>

    <div class="tabs" role="tablist">
      <button
        v-for="option in statusOptions"
        :key="option.label"
        type="button"
        role="tab"
        class="tab"
        :class="{ active: activeStatus === option.value }"
        :aria-selected="activeStatus === option.value"
        @click="selectStatus(option.value)"
      >
        {{ $t(option.label) }}
      </button>
    </div>

    <div v-if="pending" class="state">{{ $t('common.loading') }}</div>

    <div v-else-if="error" class="state state-error">
      <p>{{ $t('common.error') }}</p>
      <button class="btn btn-secondary" @click="refresh()">{{ $t('common.retry') }}</button>
    </div>

    <div v-else-if="!missions || missions.length === 0" class="state">{{ $t('missions.empty') }}</div>

    <div v-else class="list">
      <MissionCard v-for="m in missions" :key="m.id" :mission="m" />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MissionStatus } from '~/types/api'
import { useMissionFiltersStore } from '~/stores/missionFilters'

const filters = useMissionFiltersStore()
const { listMissions } = useMissions()

const statusOptions: { value: MissionStatus | null; label: string }[] = [
  { value: null, label: 'missions.all' },
  { value: 'OPEN', label: 'missions.status.OPEN' },
  { value: 'CONTRACTED', label: 'missions.status.CONTRACTED' },
  { value: 'DONE', label: 'missions.status.DONE' },
]

const activeStatus = ref<MissionStatus | null>(filters.status)

function selectStatus(status: MissionStatus | null) {
  activeStatus.value = status
  filters.setStatus(status)
}

const { data: missions, pending, error, refresh } = await useAsyncData(
  'missions',
  () => listMissions(activeStatus.value ?? undefined),
  { watch: [activeStatus] },
)
</script>

<style scoped>
.page-head {
  margin-bottom: 24px;
}

.page-head h1 {
  font-size: 40px;
  font-weight: 800;
}

.tabs {
  display: inline-flex;
  gap: 4px;
  background: var(--molt-neutral-0);
  border-radius: var(--molt-radius-pill);
  box-shadow: var(--molt-shadow-sm);
  padding: 4px;
  margin-bottom: 28px;
}

.tab {
  border: none;
  background: transparent;
  font-family: var(--molt-font-body);
  font-size: 14px;
  font-weight: 500;
  color: var(--molt-muted);
  padding: 8px 18px;
  border-radius: var(--molt-radius-pill);
  cursor: pointer;
}

.tab.active {
  background: var(--molt-secondary);
  color: var(--molt-neutral-0);
  font-weight: 700;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
