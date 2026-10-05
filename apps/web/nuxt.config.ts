export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: false },

  modules: ['@pinia/nuxt', '@nuxtjs/i18n'],

  css: ['~/assets/css/tokens.css', '~/assets/css/main.css'],

  devServer: {
    port: 3000,
  },

  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080/api',
    },
  },

  app: {
    head: {
      title: 'Molt',
      htmlAttrs: { lang: 'fr' },
      meta: [
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: 'Molt, la marketplace où tout le monde recrute tout le monde.' },
      ],
      link: [
        { rel: 'icon', type: 'image/svg+xml', href: '/favicon.svg' },
        { rel: 'preconnect', href: 'https://fonts.googleapis.com' },
        { rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: '' },
        {
          rel: 'stylesheet',
          href: 'https://fonts.googleapis.com/css2?family=Archivo:wght@600;700;800&family=DM+Sans:wght@400;500;700&display=swap',
        },
      ],
    },
  },

  i18n: {
    defaultLocale: 'fr',
    strategy: 'no_prefix',
    langDir: 'locales',
    vueI18n: './i18n.config.ts',
    locales: [
      { code: 'fr', language: 'fr-FR', name: 'Français', file: 'fr.json' },
      { code: 'en', language: 'en-GB', name: 'English', file: 'en.json' },
      { code: 'it', language: 'it-IT', name: 'Italiano', file: 'it.json' },
    ],
    detectBrowserLanguage: {
      useCookie: true,
      cookieKey: 'molt_lang',
      redirectOn: 'root',
    },
  },
})
