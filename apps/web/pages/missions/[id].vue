<template>
  <div class="container page">
    <NuxtLink to="/missions" class="back">← {{ $t('missions.backToList') }}</NuxtLink>

    <div v-if="pending && !mission" class="state">{{ $t('common.loading') }}</div>

    <div v-else-if="error" class="state state-error">
      <p>{{ isNotFound ? $t('missions.notFound') : $t('common.error') }}</p>
      <button v-if="!isNotFound" class="btn btn-secondary" @click="refresh()">{{ $t('common.retry') }}</button>
    </div>

    <template v-else-if="mission">
      <header class="mission-head">
        <div class="title-row">
          <h1>{{ mission.title }}</h1>
          <StatusPill :status="mission.status" />
        </div>
        <div class="client">
          <span class="muted">{{ $t('missions.postedBy') }}</span>
          <strong>{{ mission.client.name }}</strong>
          <KindBadge :kind="mission.client.kind" />
        </div>
      </header>

      <div class="layout">
        <div class="main">
          <section class="card block">
            <h2 class="section-title">{{ $t('missions.description') }}</h2>
            <p class="description">{{ mission.description }}</p>
          </section>

          <section class="card block">
            <h2 class="section-title">
              {{ $t('missions.proposals') }} <span class="muted count">({{ mission.proposals.length }})</span>
            </h2>
            <p v-if="mission.proposals.length === 0" class="muted">{{ $t('missions.noProposals') }}</p>
            <ul v-else class="proposals">
              <li v-for="p in mission.proposals" :key="p.id" class="proposal">
                <div class="proposal-head">
                  <NuxtLink :to="`/talents/${p.talentId}`" class="proposal-name">{{ p.talentName }}</NuxtLink>
                  <span class="proposal-rate">{{ formatMoney(p.dailyRateCents, locale) }}{{ $t('talent.perDay') }}</span>
                </div>
                <p class="proposal-message">{{ p.message }}</p>
                <span class="muted date">{{ formatDate(p.createdAt) }}</span>
              </li>
            </ul>
          </section>

          <section v-if="mission.status === 'OPEN'" class="card block">
            <h2 class="section-title">{{ $t('proposal.title') }}</h2>
            <form class="proposal-form" novalidate @submit.prevent="submit">
              <div class="form-row">
                <div class="field">
                  <label class="label" for="talentId">{{ $t('proposal.talentId') }}</label>
                  <input id="talentId" v-model.number="form.talentId" class="input" type="number" min="1" step="1" />
                  <p v-if="fieldErrors.talentId" class="field-error">{{ $t(fieldErrors.talentId) }}</p>
                </div>
                <div class="field">
                  <label class="label" for="dailyRate">{{ $t('proposal.dailyRate') }}</label>
                  <input id="dailyRate" v-model.number="form.dailyRate" class="input" type="number" min="1" step="1" />
                  <p v-if="fieldErrors.dailyRate" class="field-error">{{ $t(fieldErrors.dailyRate) }}</p>
                </div>
              </div>
              <div class="field">
                <label class="label" for="message">{{ $t('proposal.message') }}</label>
                <textarea id="message" v-model="form.message" class="textarea" rows="5" maxlength="2000" />
                <p class="muted hint">{{ $t('proposal.messageHint', { count: form.message.length }) }}</p>
                <p v-if="fieldErrors.message" class="field-error">{{ $t(fieldErrors.message) }}</p>
              </div>

              <p v-if="submitError" class="alert alert-error">{{ submitError }}</p>
              <p v-if="submitted" class="alert alert-success">{{ $t('proposal.success') }}</p>

              <button type="submit" class="btn btn-primary" :disabled="sending">
                {{ sending ? $t('proposal.sending') : $t('proposal.submit') }}
              </button>
            </form>
          </section>
        </div>

        <aside class="side">
          <section class="card block">
            <h2 class="section-title">{{ $t('missions.details') }}</h2>
            <dl class="details">
              <dt>{{ $t('missions.client') }}</dt>
              <dd>{{ mission.client.name }}</dd>
              <dt>{{ $t('missions.workMode') }}</dt>
              <dd>{{ mission.remote ? $t('missions.remote') : $t('missions.onSite') }}</dd>
              <dt>{{ $t('missions.createdAt') }}</dt>
              <dd>{{ formatDate(mission.createdAt) }}</dd>
            </dl>
            <p class="duration">{{ $t('missions.duration', mission.durationDays) }}</p>
          </section>
          <section class="card block">
            <h2 class="section-title">{{ $t('missions.skills') }}</h2>
            <div class="pills">
              <span v-for="s in mission.skills" :key="s" class="pill">{{ s }}</span>
            </div>
          </section>
        </aside>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
const route = useRoute()
const config = useRuntimeConfig()
const { t, locale } = useI18n()
const { getMission } = useMissions()

const id = computed(() => route.params.id as string)

const { data: mission, pending, error, refresh } = await useAsyncData(
  () => `mission-${id.value}`,
  () => getMission(id.value),
)

const isNotFound = computed(() => error.value?.statusCode === 404)

const form = reactive({
  talentId: null as number | null,
  dailyRate: null as number | null,
  message: '',
})

const fieldErrors = reactive<{ talentId?: string; dailyRate?: string; message?: string }>({})
const submitError = ref<string | null>(null)
const submitted = ref(false)
const sending = ref(false)

function validate() {
  fieldErrors.talentId = undefined
  fieldErrors.dailyRate = undefined
  fieldErrors.message = undefined

  if (!form.talentId || !Number.isInteger(form.talentId) || form.talentId < 1) {
    fieldErrors.talentId = 'proposal.errors.talentIdRequired'
  }
  if (!form.dailyRate || form.dailyRate <= 0) {
    fieldErrors.dailyRate = 'proposal.errors.dailyRateRequired'
  }
  const length = form.message.trim().length
  if (length < 10 || length > 2000) {
    fieldErrors.message = 'proposal.errors.messageLength'
  }
  return !fieldErrors.talentId && !fieldErrors.dailyRate && !fieldErrors.message
}

async function submit() {
  submitted.value = false
  submitError.value = null
  if (!validate()) return

  sending.value = true
  try {
    await $fetch(`${config.public.apiBase}/missions/${route.params.id}/proposals`, {
      method: 'POST',
      body: {
        talentId: form.talentId,
        dailyRateCents: Math.round(Number(form.dailyRate) * 100),
        message: form.message.trim(),
      },
    })
    submitted.value = true
    form.talentId = null
    form.dailyRate = null
    form.message = ''
    await refresh()
  } catch (e: any) {
    const status = e?.response?.status ?? e?.statusCode
    const code = e?.data?.code
    if (status === 409 && code === 'DUPLICATE_PROPOSAL') {
      submitError.value = t('proposal.errors.DUPLICATE_PROPOSAL')
    } else if (status === 409) {
      submitError.value = t('proposal.errors.MISSION_NOT_OPEN')
    } else if (status === 404) {
      submitError.value = t('proposal.errors.NOT_FOUND')
    } else if (status === 400) {
      submitError.value = t('proposal.errors.BAD_REQUEST')
    } else {
      submitError.value = t('proposal.errors.generic')
    }
  } finally {
    sending.value = false
  }
}

function formatDate(value: string) {
  return new Date(value).toLocaleDateString(locale.value, { day: 'numeric', month: 'long', year: 'numeric' })
}

useHead(() => ({
  title: mission.value ? `${mission.value.title} · Molt` : 'Molt',
}))
</script>

<style scoped>
.back {
  display: inline-block;
  font-weight: 500;
  margin-bottom: 20px;
}

.mission-head {
  margin-bottom: 28px;
}

.title-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.title-row h1 {
  font-size: 34px;
  margin: 0;
}

.client {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
}

.layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.block {
  margin-bottom: 24px;
}

.description {
  white-space: pre-line;
  margin: 0;
}

.count {
  font-weight: 500;
  font-size: 16px;
}

.proposals {
  list-style: none;
  margin: 0;
  padding: 0;
}

.proposal {
  padding: 16px 0;
  border-top: 1px solid var(--molt-border);
}

.proposal:first-child {
  border-top: none;
  padding-top: 0;
}

.proposal-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.proposal-name {
  font-weight: 700;
}

.proposal-rate {
  font-weight: 700;
}

.proposal-message {
  margin: 6px 0;
}

.date {
  font-size: 13px;
}

.proposal-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.textarea {
  resize: vertical;
}

.hint {
  font-size: 13px;
  margin: 4px 0 0;
}

.proposal-form .btn {
  align-self: flex-start;
}

.details {
  display: grid;
  grid-template-columns: max-content 1fr;
  gap: 8px 16px;
  margin: 0 0 12px;
}

.details dt {
  color: var(--molt-muted);
}

.details dd {
  margin: 0;
  font-weight: 500;
}

.duration {
  font-weight: 700;
  margin: 0;
}

@media (max-width: 860px) {
  .layout,
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
