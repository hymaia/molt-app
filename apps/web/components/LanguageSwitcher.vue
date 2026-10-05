<template>
  <label class="switcher">
    <span class="sr-only">{{ $t('header.language') }}</span>
    <select :value="locale" class="switcher-select" @change="onChange">
      <option v-for="l in locales" :key="l.code" :value="l.code">{{ l.code.toUpperCase() }}</option>
    </select>
  </label>
</template>

<script setup lang="ts">
const { locale, locales, setLocale } = useI18n()

function onChange(event: Event) {
  const code = (event.target as HTMLSelectElement).value as typeof locale.value
  setLocale(code)
}
</script>

<style scoped>
.switcher-select {
  font-family: var(--molt-font-body);
  font-weight: 700;
  font-size: 14px;
  color: var(--molt-secondary);
  background: var(--molt-neutral-0);
  border: 1px solid var(--molt-border);
  border-radius: var(--molt-radius-pill);
  padding: 6px 12px;
  cursor: pointer;
}

.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  overflow: hidden;
  clip: rect(0 0 0 0);
}
</style>
