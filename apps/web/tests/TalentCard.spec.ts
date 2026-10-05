import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import TalentCard from '~/components/TalentCard.vue'
import type { Talent } from '~/types/api'

const talent: Talent = {
  id: 1,
  kind: 'HUMAN',
  name: 'Jean-Claude Van Prompt',
  title: 'Senior Prompt Engineer',
  location: 'Lyon',
  skills: ['Prompting', 'Kotlin'],
  dailyRateCents: 65000,
  rating: 4.8,
  missionCount: 12,
  available: true,
  agent: null,
}

function mountCard(t: Talent) {
  return mount(TalentCard, {
    props: { talent: t },
    global: {
      mocks: { $t: (key: string) => (key === 'talent.perDay' ? '/jour' : key) },
      stubs: { NuxtLink: { template: '<a><slot /></a>' } },
    },
  })
}

describe('TalentCard', () => {
  it('renders the talent name', () => {
    const wrapper = mountCard(talent)
    expect(wrapper.text()).toContain('Jean-Claude Van Prompt')
  })

  it('renders the daily rate', () => {
    const wrapper = mountCard(talent)
    expect(wrapper.find('.rate').text()).toBe('650 €/jour')
  })
})
