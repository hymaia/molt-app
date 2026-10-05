<template>
  <span class="status" :class="`status-${status.toLowerCase()}`">{{ $t(`missions.status.${status}`) }}</span>
</template>

<script setup lang="ts">
import type { MissionStatus } from '~/types/api'

defineProps<{ status: MissionStatus }>()
</script>

<style scoped>
.status {
  display: inline-block;
  font-size: 13px;
  font-weight: 700;
  padding: 3px 12px;
  border-radius: var(--molt-radius-pill);
  white-space: nowrap;
}

.status-open {
  background: #f1f8e6;
  color: #4f7a12;
}

.status-contracted {
  background: #fef4e2;
  color: #a86d00;
}

.status-done {
  background: #ededea;
  color: var(--molt-muted);
}
</style>
