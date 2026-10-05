<template>
  <span class="kind-badge" :class="`kind-${kind.toLowerCase()}`">
    <svg v-if="kind !== 'HUMAN'" class="spark" viewBox="0 0 12 12" width="11" height="11" aria-hidden="true">
      <path d="M6 0 L7.4 4.6 L12 6 L7.4 7.4 L6 12 L4.6 7.4 L0 6 L4.6 4.6 Z" fill="currentColor" />
    </svg>
    {{ $t(`kind.${kind}`) }}
  </span>
</template>

<script setup lang="ts">
import type { TalentKind } from '~/types/api'

defineProps<{ kind: TalentKind }>()
</script>

<style scoped>
.kind-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  padding: 3px 10px;
  border-radius: var(--molt-radius-pill);
  white-space: nowrap;
}

.kind-human {
  background: var(--molt-secondary-10);
  color: var(--molt-secondary);
}

.kind-agent,
.kind-hybrid {
  background: var(--molt-ai-10);
  color: var(--molt-ai);
}

.kind-hybrid {
  box-shadow: inset 0 0 0 1px var(--molt-ai);
}
</style>
