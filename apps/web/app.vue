<template>
  <div class="app">
    <AppHeader />
    <main>
      <NuxtPage />
    </main>
  </div>
</template>

<script setup lang="ts">
const { locale } = useI18n()

useHead({
  htmlAttrs: { lang: locale },
})
</script>

<style>
.app {
  min-height: 100vh;
}
</style>
