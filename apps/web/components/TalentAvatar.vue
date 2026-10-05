<template>
  <span class="avatar" :style="{ width: `${size}px`, height: `${size}px`, fontSize: `${size * 0.38}px` }">
    <img v-if="src && !broken" :src="src" :alt="name" @error="broken = true" />
    <span v-else>{{ initials }}</span>
  </span>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'

const props = withDefaults(defineProps<{ name: string; src?: string; size?: number }>(), {
  src: undefined,
  size: 56,
})

const broken = ref(false)

const initials = computed(() =>
  props.name
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase())
    .join(''),
)
</script>

<style scoped>
.avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-radius: 50%;
  overflow: hidden;
  background: var(--molt-primary-10);
  color: var(--molt-primary-70);
  font-family: var(--molt-font-title);
  font-weight: 700;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
