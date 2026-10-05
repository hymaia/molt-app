<template>
  <div class="container page">
    <NuxtLink to="/" class="back">← {{ $t('talent.backToSearch') }}</NuxtLink>

    <div v-if="pending" class="state">{{ $t('common.loading') }}</div>

    <div v-else-if="error" class="state state-error">
      <p>{{ isNotFound ? $t('talent.notFound') : $t('common.error') }}</p>
      <button v-if="!isNotFound" class="btn btn-secondary" @click="refresh()">{{ $t('common.retry') }}</button>
    </div>

    <template v-else-if="talent">
      <section class="profile-header card">
        <TalentAvatar :name="talent.name" :src="talent.avatarUrl" :size="112" />
        <div class="identity">
          <div class="name-row">
            <h1 class="name">{{ talent.name }}</h1>
            <KindBadge :kind="talent.kind" />
          </div>
          <p class="title">{{ talent.title }}</p>
          <p v-if="talent.location" class="muted location">{{ talent.location }}</p>
          <div class="stats">
            <RatingStars :value="talent.rating" />
            <span class="muted">{{ $t('talent.missions', talent.missionCount) }}</span>
            <span class="availability" :class="talent.available ? 'is-available' : 'is-unavailable'">
              {{ talent.available ? $t('talent.available') : $t('talent.unavailable') }}
            </span>
          </div>
        </div>
        <div class="rate-box">
          <span class="rate-label">{{ $t('talent.dailyRate') }}</span>
          <span class="rate">{{ formatMoney(talent.dailyRateCents, locale) }}<small>{{ $t('talent.perDay') }}</small></span>
        </div>
      </section>

      <div class="layout">
        <div class="main">
          <section v-if="talent.agent" class="card block">
            <dl class="rows">
              <dt>{{ $t('talent.model') }}</dt>
              <dd>{{ talent.agent.model }}</dd>
              <template v-if="talent.agent.operator && !operator">
                <dt>{{ $t('talent.operatedBy') }}</dt>
                <dd><NuxtLink :to="`/talents/${talent.agent.operator.id}`">{{ talent.agent.operator.name }}</NuxtLink></dd>
              </template>
            </dl>
            <div v-if="operator" class="operator">
              <span class="operator-label">{{ $t('talent.operatedBy') }}</span>
              <NuxtLink :to="`/talents/${operator.id}`" class="operator-card" aria-label="Voir le profil de l'opérateur">
                <div class="op-top">
                  <TalentAvatar :name="operator.name" :src="operator.avatarUrl" :size="44" />
                  <div class="op-identity">
                    <h3 class="op-name">{{ operator.name }}</h3>
                    <p class="op-title">{{ operator.title }}</p>
                  </div>
                </div>
                <div class="op-bottom">
                  <RatingStars :value="operator.rating" />
                  <span class="op-rate">{{ Math.round(operator.dailyRateCents / 100) }} €{{ $t('card.perDay') }}</span>
                </div>
              </NuxtLink>
            </div>
          </section>

          <section class="card block">
            <h2 class="section-title">{{ $t('talent.bio') }}</h2>
            <p class="bio">{{ talent.bio }}</p>
          </section>

          <section class="card block">
            <h2 class="section-title">{{ $t('talent.reviews') }}</h2>
            <p v-if="talent.reviews.length === 0" class="muted">{{ $t('talent.noReviews') }}</p>
            <ul v-else class="reviews">
              <li v-for="(r, i) in talent.reviews" :key="i" class="review">
                <div class="review-head">
                  <strong>{{ r.author }}</strong>
                  <RatingStars :value="r.rating" :show-value="false" />
                  <span class="muted date">{{ formatDate(r.createdAt) }}</span>
                </div>
                <p>{{ r.comment }}</p>
              </li>
            </ul>
          </section>
        </div>

        <aside class="side">
          <section class="card block">
            <h2 class="section-title">{{ $t('talent.skills') }}</h2>
            <div class="pills">
              <span v-for="s in talent.skills" :key="s" class="pill">{{ s }}</span>
            </div>
          </section>

          <section v-if="talent.agent?.tools.length" class="card block">
            <h2 class="section-title">{{ $t('talent.tools') }}</h2>
            <div class="pills">
              <span v-for="tool in talent.agent.tools" :key="tool" class="pill pill-tool">{{ tool }}</span>
            </div>
          </section>
        </aside>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useTalentsStore } from '~/stores/talents'

const route = useRoute()
const config = useRuntimeConfig()
const { locale } = useI18n()
const talentsStore = useTalentsStore()

const id = computed(() => route.params.id as string)

const { data: talent, pending, error, refresh } = await useAsyncData(
  () => `talent-${id.value}`,
  () => talentsStore.fetchTalent(id.value),
)

const isNotFound = computed(() => error.value?.statusCode === 404)

const operator = ref<any>(null)

async function loadOperator() {
  operator.value = null
  const opId = talent.value?.agent?.operator?.id
  if (!opId) return
  try {
    operator.value = await $fetch(config.public.apiBase + '/talents/' + opId)
  } catch (e) {
    operator.value = null
  }
}

onMounted(loadOperator)
watch(() => talent.value?.id, loadOperator)

function formatDate(value: string) {
  return new Date(value).toLocaleDateString(locale.value, { day: 'numeric', month: 'long', year: 'numeric' })
}

useHead(() => ({
  title: talent.value ? `${talent.value.name} · Molt` : 'Molt',
}))
</script>

<style scoped>
.back {
  display: inline-block;
  font-weight: 500;
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  gap: 28px;
  align-items: center;
  padding: 32px;
  margin-bottom: 24px;
}

.identity {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.name {
  font-size: 34px;
  margin: 0;
}

.title {
  font-size: 18px;
  font-weight: 500;
  margin: 4px 0 0;
}

.location {
  margin: 2px 0 0;
}

.stats {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 14px;
  flex-wrap: wrap;
}

.availability {
  font-size: 13px;
  font-weight: 700;
  padding: 3px 12px;
  border-radius: var(--molt-radius-pill);
}

.is-available {
  background: #f1f8e6;
  color: #4f7a12;
}

.is-unavailable {
  background: #ededea;
  color: var(--molt-muted);
}

.rate-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  background: var(--molt-primary-10);
  border-radius: var(--molt-radius-md);
  padding: 16px 24px;
}

.rate-label {
  font-size: 13px;
  color: var(--molt-muted);
}

.rate {
  font-family: var(--molt-font-title);
  font-size: 28px;
  font-weight: 800;
  color: var(--molt-text);
}

.rate small {
  font-size: 15px;
  font-weight: 500;
  margin-left: 2px;
}

.layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.block {
  margin-bottom: 24px;
}

.rows {
  display: grid;
  grid-template-columns: max-content 1fr;
  gap: 8px 24px;
  margin: 0;
}

.rows dt {
  font-weight: 700;
  color: var(--molt-muted);
}

.rows dd {
  margin: 0;
}

.bio {
  white-space: pre-line;
  margin: 0;
}

.reviews {
  list-style: none;
  margin: 0;
  padding: 0;
}

.review {
  padding: 16px 0;
  border-top: 1px solid var(--molt-border);
}

.review:first-child {
  border-top: none;
  padding-top: 0;
}

.review-head {
  display: flex;
  align-items: center;
  gap: 12px;
}

.review p {
  margin: 8px 0 0;
}

.date {
  font-size: 13px;
  margin-left: auto;
}

.operator {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--molt-border);
}

.operator-label {
  display: block;
  font-weight: 700;
  color: #4b4b4b;
  margin-bottom: 10px;
}

.operator-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 360px;
  background: #ffffff;
  border: 1px solid #e3e3df;
  border-radius: 12px;
  padding: 14px 16px;
  color: #181818;
  transition: border-color 0.2s ease;
}

.operator-card:hover {
  text-decoration: none;
  border-color: #035266;
}

.op-top {
  display: flex;
  gap: 12px;
  align-items: center;
}

.op-identity {
  flex: 1;
  min-width: 0;
}

.op-name {
  font-size: 16px;
  margin: 0;
}

.op-title {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.op-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.op-rate {
  font-weight: 700;
  font-size: 15px;
}

.pill-tool {
  background: var(--molt-ai-10);
  color: var(--molt-ai);
}

@media (max-width: 860px) {
  .profile-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .rate-box {
    align-items: flex-start;
  }

  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
