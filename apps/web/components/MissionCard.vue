<template>
  <NuxtLink :to="`/missions/${mission.id}`" class="mission-card">
    <div class="head">
      <h3 class="title">{{ mission.title }}</h3>
      <StatusPill :status="mission.status" />
    </div>
    <div class="client">
      <span class="muted">{{ $t('missions.postedBy') }}</span>
      <strong>{{ mission.client.name }}</strong>
      <KindBadge :kind="mission.client.kind" />
    </div>
    <div class="pills">
      <span v-for="s in mission.skills.slice(0, 5)" :key="s" class="pill">{{ s }}</span>
      <span v-if="mission.skills.length > 5" class="pill">+{{ mission.skills.length - 5 }}</span>
    </div>
    <div class="meta">
      <span>{{ $t('missions.duration', mission.durationDays) }}</span>
      <span class="dot">·</span>
      <span>{{ mission.remote ? $t('missions.remote') : $t('missions.onSite') }}</span>
    </div>
  </NuxtLink>
</template>

<script setup lang="ts">
defineProps<{ mission: any }>()
</script>

<style scoped>
.mission-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: var(--molt-neutral-0);
  border-radius: var(--molt-radius-md);
  box-shadow: var(--molt-shadow-sm);
  padding: 20px 24px;
  color: var(--molt-text);
  transition: box-shadow 0.2s ease;
}

.mission-card:hover {
  text-decoration: none;
  box-shadow: var(--molt-shadow-md);
}

.head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.title {
  font-size: 19px;
  margin: 0;
}

.client {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
}

.meta {
  display: flex;
  gap: 8px;
  font-size: 14px;
  color: var(--molt-muted);
}
</style>
