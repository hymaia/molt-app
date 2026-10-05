<template>
  <NuxtLink :to="`/talents/${talent.id}`" class="talent-card" :class="{ premium: talent.dailyRateCents >= 80000 }">
    <div class="top">
      <TalentAvatar :name="talent.name" :src="talent.avatarUrl" :size="56" />
      <div class="identity">
        <h3 class="name">{{ talent.name }}</h3>
        <p class="title">{{ talent.title }}</p>
        <p v-if="talent.location" class="location">{{ talent.location }}</p>
      </div>
      <KindBadge :kind="talent.kind" class="badge" />
    </div>

    <div class="pills skills">
      <span v-for="s in talent.skills.slice(0, 4)" :key="s" class="pill">{{ s }}</span>
      <span v-if="talent.skills.length > 4" class="pill more">+{{ talent.skills.length - 4 }}</span>
    </div>

    <div class="bottom">
      <RatingStars :value="rating" />
      <span class="rate">{{ (talent.dailyRateCents / 100).toFixed(0) }} €{{ $t('talent.perDay') }}</span>
    </div>
  </NuxtLink>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Talent } from '~/types/api'
import KindBadge from '~/components/KindBadge.vue'
import RatingStars from '~/components/RatingStars.vue'
import TalentAvatar from '~/components/TalentAvatar.vue'

const props = defineProps<{ talent: Talent }>()

const details = ref<any>(null)

const rating = computed(() => details.value?.rating ?? props.talent.rating)

onMounted(async () => {
  try {
    details.value = await $fetch(`http://localhost:8080/api/talents/${props.talent.id}`)
  } catch (e) {}
})
</script>

<style scoped>
.talent-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: var(--molt-neutral-0);
  border-radius: var(--molt-radius-md);
  box-shadow: var(--molt-shadow-sm);
  padding: 20px;
  color: var(--molt-text);
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.talent-card.premium {
  box-shadow: 0 0 0 1px #e8c37a, var(--molt-shadow-sm);
}

.talent-card:hover {
  text-decoration: none;
  box-shadow: var(--molt-shadow-md);
  transform: translateY(-2px);
}

.top {
  display: flex;
  gap: 14px;
  align-items: flex-start;
}

.identity {
  flex: 1;
  min-width: 0;
}

.name {
  font-size: 18px;
  margin: 0 0 2px;
}

.title {
  margin: 0;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.location {
  margin: 2px 0 0;
  font-size: 14px;
  color: var(--molt-muted);
}

.skills {
  min-height: 28px;
}

.more {
  background: var(--molt-bg);
  color: var(--molt-muted);
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 12px;
  border-top: 1px solid var(--molt-border);
}

.rate {
  font-weight: 700;
  font-size: 16px;
}
</style>
