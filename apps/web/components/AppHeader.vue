<template>
  <header class="header">
    <div class="container header-inner">
      <NuxtLink to="/" class="brand" :aria-label="$t('nav.home')">
        <MoltLogo />
      </NuxtLink>
      <nav class="nav">
        <NuxtLink to="/" class="nav-link" :class="{ active: isTalents }">{{ $t('nav.talents') }}</NuxtLink>
        <NuxtLink to="/missions" class="nav-link" :class="{ active: isMissions }">{{ $t('nav.missions') }}</NuxtLink>
      </nav>
      <LanguageSwitcher class="lang" />
    </div>
  </header>
</template>

<script setup lang="ts">
const route = useRoute()

const isTalents = computed(() => route.path === '/' || route.path.startsWith('/talents'))
const isMissions = computed(() => route.path.startsWith('/missions'))
</script>

<style scoped>
.header {
  background: var(--molt-neutral-0);
  border-bottom: 1px solid var(--molt-border);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-inner {
  display: flex;
  align-items: center;
  height: 68px;
  gap: 40px;
}

.brand:hover {
  text-decoration: none;
}

.nav {
  display: flex;
  gap: 28px;
  flex: 1;
}

.nav-link {
  color: var(--molt-text);
  font-weight: 500;
  padding: 22px 0;
  border-bottom: 3px solid transparent;
}

.nav-link:hover {
  text-decoration: none;
  color: var(--molt-secondary);
}

.nav-link.active {
  color: var(--molt-secondary);
  border-bottom-color: var(--molt-primary);
}
</style>
