// Run: node --test .claude/skills/translation-checker/scripts/check-translations.test.mjs
import { test } from 'node:test';
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';
import { mkdtempSync, mkdirSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { placeholders } from './check-translations.mjs';

const script = path.join(path.dirname(fileURLToPath(import.meta.url)), 'check-translations.mjs');

function fixture({ web = {}, mobile = {} }) {
  const root = mkdtempSync(path.join(tmpdir(), 'molt-i18n-'));
  mkdirSync(path.join(root, 'apps/web/i18n/locales'), { recursive: true });
  mkdirSync(path.join(root, 'apps/mobile/lib/l10n'), { recursive: true });
  for (const [locale, content] of Object.entries(web)) {
    writeFileSync(path.join(root, `apps/web/i18n/locales/${locale}.json`), typeof content === 'string' ? content : JSON.stringify(content));
  }
  for (const [locale, content] of Object.entries(mobile)) {
    writeFileSync(path.join(root, `apps/mobile/lib/l10n/app_${locale}.arb`), JSON.stringify(content));
  }
  return root;
}

function run(root) {
  try {
    return { code: 0, result: JSON.parse(execFileSync('node', [script, '--root', root, '--json'], { encoding: 'utf8' })) };
  } catch (error) {
    return { code: error.status, result: JSON.parse(error.stdout) };
  }
}

const cleanMobile = {
  en: { '@@locale': 'en', hello: 'Hello', '@hello': { description: 'greeting' } },
  fr: { '@@locale': 'fr', hello: 'Bonjour' },
  it: { '@@locale': 'it', hello: 'Ciao' },
};
const cleanWeb = { en: { nav: { home: 'Home' } }, fr: { nav: { home: 'Accueil' } }, it: { nav: { home: 'Inizio' } } };

test('clean locales pass and ARB metadata is ignored', () => {
  const { code, result } = run(fixture({ web: cleanWeb, mobile: cleanMobile }));
  assert.equal(code, 0);
  assert.deepEqual(result.errors, []);
  assert.deepEqual(result.warnings, []);
});

test('missing and empty nested web keys fail', () => {
  const web = { ...cleanWeb, it: { nav: {} }, fr: { nav: { home: '  ' } } };
  const { code, result } = run(fixture({ web, mobile: cleanMobile }));
  assert.equal(code, 1);
  assert.deepEqual(
    result.errors.map((e) => `${e.locale}:${e.key}:${e.status}`).sort(),
    ['fr:nav.home:empty', 'it:nav.home:missing'],
  );
});

test('missing ARB message fails', () => {
  const mobile = { ...cleanMobile, it: { '@@locale': 'it' } };
  const { code, result } = run(fixture({ web: cleanWeb, mobile }));
  assert.equal(code, 1);
  assert.equal(result.errors[0].key, 'hello');
  assert.equal(result.errors[0].client, 'mobile');
});

test('renamed placeholder fails', () => {
  const web = {
    en: { n: '{count} days' },
    fr: { n: '{count} jours' },
    it: { n: '{conto} giorni' },
  };
  const { code, result } = run(fixture({ web, mobile: cleanMobile }));
  assert.equal(code, 1);
  assert.equal(result.errors[0].status, 'placeholder-mismatch');
  assert.equal(result.errors[0].locale, 'it');
});

test('copied English and stale keys are warnings only', () => {
  const web = { en: { a: 'Missions' }, fr: { a: 'Missions', old: 'x' }, it: { a: 'Missioni' } };
  const { code, result } = run(fixture({ web, mobile: cleanMobile }));
  assert.equal(code, 0);
  assert.deepEqual(result.warnings.map((w) => w.status).sort(), ['extra-key', 'same-as-english']);
});

test('malformed or absent file is reported, not treated as clean', () => {
  const web = { ...cleanWeb, fr: '{ not json' };
  const mobile = { en: cleanMobile.en, fr: cleanMobile.fr };
  const { code, result } = run(fixture({ web, mobile }));
  assert.equal(code, 1);
  assert.deepEqual(result.errors.map((e) => e.status), ['file-error', 'file-error']);
});

test('placeholders ignore ICU branch text', () => {
  assert.deepEqual(placeholders('{count, plural, =0{No talents} other{{count} talents}}'), ['count']);
  assert.deepEqual(placeholders('1 day | {count} days'), ['count']);
  assert.deepEqual(placeholders('Hi {name}, you have {n}'), ['n', 'name']);
});
