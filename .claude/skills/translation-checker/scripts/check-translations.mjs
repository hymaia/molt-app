#!/usr/bin/env node
// Deterministic translation checker for Molt web (JSON) and mobile (ARB) locales.
// Errors (exit 1): unreadable files, missing keys, non-string values, empty values,
// placeholder mismatches. Warnings (exit 0): value identical to English, extra keys.

import { readFile } from 'node:fs/promises';
import path from 'node:path';
import process from 'node:process';

const args = process.argv.slice(2);
const jsonOutput = args.includes('--json');
const rootIndex = args.indexOf('--root');
const root = path.resolve(rootIndex >= 0 ? args[rootIndex + 1] : process.cwd());

const clients = [
  {
    name: 'web',
    source: 'apps/web/i18n/locales/en.json',
    targets: [
      ['fr', 'apps/web/i18n/locales/fr.json'],
      ['it', 'apps/web/i18n/locales/it.json'],
    ],
    ignoreKey: () => false,
  },
  {
    name: 'mobile',
    source: 'apps/mobile/lib/l10n/app_en.arb',
    targets: [
      ['fr', 'apps/mobile/lib/l10n/app_fr.arb'],
      ['it', 'apps/mobile/lib/l10n/app_it.arb'],
    ],
    ignoreKey: (key) => key.startsWith('@'),
  },
];

async function loadJson(relativePath) {
  try {
    return { value: JSON.parse(await readFile(path.resolve(root, relativePath), 'utf8')) };
  } catch (error) {
    return { error: `${relativePath}: ${error.message}` };
  }
}

function flatten(value, prefix, ignored, output = new Map()) {
  if (typeof value === 'string') {
    output.set(prefix, value);
    return output;
  }
  if (value === null || Array.isArray(value) || typeof value !== 'object') {
    output.set(prefix, { invalidType: Array.isArray(value) ? 'array' : typeof value });
    return output;
  }
  for (const [key, child] of Object.entries(value)) {
    if (ignored(key)) continue;
    flatten(child, prefix ? `${prefix}.${key}` : key, ignored, output);
  }
  return output;
}

// Variable names: `{count}` (vue-i18n, ICU simple) and `{count, plural, ...}` (ICU).
// ICU branch text such as `=0{No talents}` is not a variable and is not matched.
export function placeholders(message) {
  const names = new Set();
  for (const match of message.matchAll(/\{\s*([A-Za-z_]\w*)\s*[,}]/g)) names.add(match[1]);
  return [...names].sort();
}

export async function check() {
  const errors = [];
  const warnings = [];

  for (const client of clients) {
    const sourceFile = await loadJson(client.source);
    if (sourceFile.error) {
      errors.push({ client: client.name, status: 'file-error', file: client.source, detail: sourceFile.error });
      continue;
    }
    const sourceEntries = flatten(sourceFile.value, '', client.ignoreKey);

    for (const [locale, targetPath] of client.targets) {
      const targetFile = await loadJson(targetPath);
      if (targetFile.error) {
        errors.push({ client: client.name, locale, status: 'file-error', file: targetPath, detail: targetFile.error });
        continue;
      }
      const targetEntries = flatten(targetFile.value, '', client.ignoreKey);
      const at = { client: client.name, locale, file: targetPath };

      for (const [key, english] of sourceEntries) {
        if (typeof english !== 'string') {
          errors.push({ ...at, file: client.source, key, status: 'invalid-source-type', english });
          continue;
        }
        const target = targetEntries.get(key);
        if (target === undefined) {
          errors.push({ ...at, key, status: 'missing', english });
        } else if (typeof target !== 'string') {
          errors.push({ ...at, key, status: 'type-mismatch', english, actual: target });
        } else if (target.trim() === '') {
          errors.push({ ...at, key, status: 'empty', english });
        } else {
          const expected = placeholders(english);
          const actual = placeholders(target);
          if (expected.join() !== actual.join()) {
            errors.push({ ...at, key, status: 'placeholder-mismatch', english, actual: target, expected, found: actual });
          } else if (target === english) {
            warnings.push({ ...at, key, status: 'same-as-english', english });
          }
        }
      }

      for (const [key, value] of targetEntries) {
        if (!sourceEntries.has(key)) warnings.push({ ...at, key, status: 'extra-key', actual: value });
      }
    }
  }

  return { clean: errors.length === 0, errors, warnings };
}

function printText(result) {
  const print = (level, item) => {
    const where = `[${item.client}${item.locale ? ` / ${item.locale}` : ''}]`;
    if (item.status === 'file-error') {
      console.log(`${level} FILE-ERROR ${where} ${item.detail}`);
      return;
    }
    console.log(`${level} ${item.status.toUpperCase()} ${where} ${item.key}  (${item.file})`);
    if (item.english !== undefined) console.log(`  English: ${JSON.stringify(item.english)}`);
    if (item.actual !== undefined) console.log(`  Current: ${JSON.stringify(item.actual)}`);
    if (item.expected) console.log(`  Placeholders expected [${item.expected}] found [${item.found}]`);
  };
  result.errors.forEach((item) => print('ERROR', item));
  result.warnings.forEach((item) => print('WARN ', item));
  console.log(
    result.clean
      ? `Translation check passed (${result.warnings.length} warning(s) to review).`
      : `Translation check failed: ${result.errors.length} error(s), ${result.warnings.length} warning(s).`,
  );
}

if (import.meta.url === `file://${process.argv[1]}`) {
  const result = await check();
  if (jsonOutput) console.log(JSON.stringify(result, null, 2));
  else printText(result);
  process.exitCode = result.clean ? 0 : 1;
}
