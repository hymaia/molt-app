#!/usr/bin/env node
// Hits a running API and checks responses against contracts/openapi.yaml.
// Usage: API_BASE=http://localhost:8080/api node scripts/contract-test.mjs
import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { parse } from 'yaml';

const BASE = process.env.API_BASE ?? 'http://localhost:8080/api';
const spec = parse(readFileSync(fileURLToPath(new URL('../contracts/openapi.yaml', import.meta.url)), 'utf8'));

function resolve(schema) {
  if (schema?.$ref) {
    const path = schema.$ref.replace('#/', '').split('/');
    return resolve(path.reduce((node, key) => node[key], spec));
  }
  return schema;
}

function validate(value, rawSchema, at = '$') {
  const schema = resolve(rawSchema);
  const errors = [];
  if (value === null) {
    return schema.nullable ? [] : [`${at}: null is not allowed`];
  }
  if (schema.allOf) {
    return schema.allOf.flatMap((part) => validate(value, part, at));
  }
  if (schema.enum && !schema.enum.includes(value)) {
    errors.push(`${at}: ${JSON.stringify(value)} not in [${schema.enum.join(', ')}]`);
  }
  switch (schema.type) {
    case 'object':
      if (typeof value !== 'object' || Array.isArray(value)) return [`${at}: expected object`];
      for (const key of schema.required ?? []) {
        if (!(key in value)) errors.push(`${at}.${key}: missing`);
      }
      for (const [key, sub] of Object.entries(schema.properties ?? {})) {
        if (key in value) errors.push(...validate(value[key], sub, `${at}.${key}`));
      }
      break;
    case 'array':
      if (!Array.isArray(value)) return [`${at}: expected array`];
      value.forEach((item, i) => errors.push(...validate(item, schema.items, `${at}[${i}]`)));
      break;
    case 'integer':
      if (!Number.isInteger(value)) errors.push(`${at}: expected integer, got ${JSON.stringify(value)}`);
      break;
    case 'number':
      if (typeof value !== 'number') errors.push(`${at}: expected number`);
      break;
    case 'string':
      if (typeof value !== 'string') errors.push(`${at}: expected string`);
      break;
    case 'boolean':
      if (typeof value !== 'boolean') errors.push(`${at}: expected boolean`);
      break;
  }
  return errors;
}

function responseSchema(path, method, status) {
  const response = resolve(spec.paths[path][method].responses[String(status)]);
  return response?.content?.['application/json']?.schema;
}

async function check(name, { path, url, method = 'get', body, expect }) {
  const res = await fetch(`${BASE}${url}`, {
    method: method.toUpperCase(),
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  });
  const json = await res.json().catch(() => undefined);
  const errors = [];
  if (res.status !== expect) errors.push(`status ${res.status}, expected ${expect}`);
  const schema = responseSchema(path, method, expect);
  if (schema && res.status === expect) errors.push(...validate(json, schema));
  console.log(`${errors.length ? '✗' : '✓'} ${name}`);
  errors.slice(0, 10).forEach((e) => console.log(`    ${e}`));
  return { ok: errors.length === 0, json };
}

const results = [];
const run = async (name, opts) => {
  const result = await check(name, opts);
  results.push(result.ok);
  return result.json;
};

try {
  await run('GET /talents', { path: '/talents', url: '/talents', expect: 200 });
  await run('GET /talents?kind=HYBRID&sort=rating', { path: '/talents', url: '/talents?kind=HYBRID&sort=rating', expect: 200 });
  await run('GET /talents?skill=java&available=true', { path: '/talents', url: '/talents?skill=java&available=true', expect: 200 });
  await run('GET /talents/1', { path: '/talents/{id}', url: '/talents/1', expect: 200 });
  await run('GET /talents/999999 → 404', { path: '/talents/{id}', url: '/talents/999999', expect: 404 });
  const missions = await run('GET /missions', { path: '/missions', url: '/missions', expect: 200 });
  await run('GET /missions?status=OPEN', { path: '/missions', url: '/missions?status=OPEN', expect: 200 });
  const hybrid = await run('GET /talents/40', { path: '/talents/{id}', url: '/talents/40', expect: 200 });
  if (!hybrid?.agent?.operator?.id) {
    console.log('✗ GET /talents/40 has agent.operator');
    results.push(false);
  } else {
    console.log('✓ GET /talents/40 has agent.operator');
    results.push(true);
  }
  const open = missions?.find((m) => m.status === 'OPEN');
  if (open) {
    await run(`GET /missions/${open.id}`, { path: '/missions/{id}', url: `/missions/${open.id}`, expect: 200 });
    await run('POST proposal with short message → 400', {
      path: '/missions/{id}/proposals',
      url: `/missions/${open.id}/proposals`,
      method: 'post',
      body: { talentId: 1, dailyRateCents: 50000, message: 'hi' },
      expect: 400,
    });
  }
} catch (error) {
  console.error(`Cannot reach ${BASE}: ${error.message}`);
  process.exit(2);
}

const failed = results.filter((ok) => !ok).length;
console.log(`\n${results.length - failed}/${results.length} passed`);
process.exit(failed ? 1 : 0);
