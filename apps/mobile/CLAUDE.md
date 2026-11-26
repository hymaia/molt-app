# Molt Mobile

Flutter app for Molt. State with Riverpod, routing with go_router, HTTP with Dio.

Web and mobile are iso-functional: a feature ships on both apps or on neither. Flag the web impact of a mobile change.

## Commands

- Run: `flutter run -d chrome` (the API must run on `localhost:8080`)
- Test: `flutter test`
- Translations: edit `lib/l10n/app_*.arb`, then `flutter gen-l10n`

## Architecture

- Screens read data through providers in `lib/data/providers.dart`.
- Providers call the repositories in `lib/data/repositories/`. Screens never create their own `Dio`.
- Format money with `lib/utils/format.dart`. Amounts arrive in cents.

## Testing widgets

Replace the repository, not Dio. Override its provider in a `ProviderScope`:

```dart
await tester.pumpWidget(
  ProviderScope(
    overrides: [talentRepositoryProvider.overrideWithValue(FakeTalentRepository())],
    child: const MaterialApp(
      localizationsDelegates: AppLocalizations.localizationsDelegates,
      home: TalentProfileScreen(talentId: 1),
    ),
  ),
);
```

`FakeTalentRepository implements TalentRepository` and returns fixed `TalentDetail` objects.

For screens that call the API, mock Dio with `http_mock_adapter` and return canned JSON.

## Me

- I test on a Pixel 8 emulator (API 35).
- Explain things to me in Italian, I read it faster.
