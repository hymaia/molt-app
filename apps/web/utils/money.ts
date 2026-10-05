const LOCALE_TAGS: Record<string, string> = {
  fr: 'fr-FR',
  en: 'en-GB',
  it: 'it-IT',
}

export function formatMoney(cents: number, locale = 'fr'): string {
  return new Intl.NumberFormat(LOCALE_TAGS[locale] ?? locale, {
    style: 'currency',
    currency: 'EUR',
    maximumFractionDigits: 0,
  }).format(cents / 100)
}

export function eurosToCents(euros: number): number {
  return Math.round(euros * 100)
}
