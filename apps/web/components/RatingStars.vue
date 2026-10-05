<template>
  <span class="rating" :title="value.toFixed(1)">
    <span class="stars" aria-hidden="true">
      <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= Math.round(value) }">★</span>
    </span>
    <span v-if="showValue" class="value">{{ value.toFixed(1) }}</span>
  </span>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ value: number; showValue?: boolean }>(), {
  showValue: true,
})
</script>

<style scoped>
.rating {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.star {
  color: #dcdcd6;
}

.star.filled {
  color: var(--molt-warning);
}

.value {
  font-weight: 700;
}
</style>
