import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'l10n/app_localizations.dart';
import 'router.dart';
import 'theme/molt_theme.dart';

void main() {
  runApp(const ProviderScope(child: MoltApp()));
}

class MoltApp extends StatelessWidget {
  const MoltApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      onGenerateTitle: (context) => AppLocalizations.of(context).appTitle,
      debugShowCheckedModeBanner: false,
      theme: buildMoltTheme(),
      routerConfig: router,
      localizationsDelegates: const [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: const [Locale('fr'), Locale('en'), Locale('it')],
      localeListResolutionCallback: (locales, supported) {
        for (final locale in locales ?? const <Locale>[]) {
          for (final s in supported) {
            if (s.languageCode == locale.languageCode) return s;
          }
        }
        return const Locale('fr');
      },
    );
  }
}
