import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import 'molt_colors.dart';

TextStyle moltTitle({double size = 20, FontWeight weight = FontWeight.w700, Color color = MoltColors.text}) {
  return GoogleFonts.archivo(fontSize: size, fontWeight: weight, color: color);
}

ThemeData buildMoltTheme() {
  final base = ThemeData(
    useMaterial3: true,
    colorScheme: ColorScheme.fromSeed(
      seedColor: MoltColors.primary,
      primary: MoltColors.primary,
      onPrimary: MoltColors.neutral0,
      secondary: MoltColors.secondary,
      onSecondary: MoltColors.neutral0,
      error: MoltColors.error,
      surface: MoltColors.neutral0,
      onSurface: MoltColors.text,
    ),
    scaffoldBackgroundColor: MoltColors.bg,
  );

  final body = GoogleFonts.dmSansTextTheme(base.textTheme).apply(
    bodyColor: MoltColors.text,
    displayColor: MoltColors.text,
  );

  final pillShape = RoundedRectangleBorder(borderRadius: BorderRadius.circular(MoltColors.radiusPill));

  return base.copyWith(
    textTheme: body.copyWith(
      headlineSmall: moltTitle(size: 24),
      titleLarge: moltTitle(size: 20),
      titleMedium: moltTitle(size: 16, weight: FontWeight.w600),
    ),
    appBarTheme: AppBarTheme(
      backgroundColor: MoltColors.neutral0,
      foregroundColor: MoltColors.text,
      surfaceTintColor: Colors.transparent,
      elevation: 0,
      scrolledUnderElevation: 1,
      shadowColor: const Color(0x14181818),
      titleTextStyle: moltTitle(size: 18),
    ),
    cardTheme: CardThemeData(
      color: MoltColors.neutral0,
      surfaceTintColor: Colors.transparent,
      elevation: 0,
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(MoltColors.radiusM)),
    ),
    filledButtonTheme: FilledButtonThemeData(
      style: FilledButton.styleFrom(
        backgroundColor: MoltColors.primary,
        foregroundColor: MoltColors.neutral0,
        shape: pillShape,
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        textStyle: GoogleFonts.dmSans(fontWeight: FontWeight.w700, fontSize: 15),
      ),
    ),
    outlinedButtonTheme: OutlinedButtonThemeData(
      style: OutlinedButton.styleFrom(
        foregroundColor: MoltColors.primary,
        side: const BorderSide(color: MoltColors.primary),
        shape: pillShape,
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        textStyle: GoogleFonts.dmSans(fontWeight: FontWeight.w700, fontSize: 15),
      ),
    ),
    segmentedButtonTheme: SegmentedButtonThemeData(
      style: ButtonStyle(
        shape: WidgetStatePropertyAll(pillShape),
        backgroundColor: WidgetStateProperty.resolveWith(
          (states) => states.contains(WidgetState.selected) ? MoltColors.primary10 : MoltColors.neutral0,
        ),
        foregroundColor: WidgetStateProperty.resolveWith(
          (states) => states.contains(WidgetState.selected) ? MoltColors.primary70 : MoltColors.muted,
        ),
      ),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: MoltColors.neutral0,
      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(MoltColors.radiusS),
        borderSide: const BorderSide(color: MoltColors.border),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(MoltColors.radiusS),
        borderSide: const BorderSide(color: MoltColors.border),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(MoltColors.radiusS),
        borderSide: const BorderSide(color: MoltColors.secondary, width: 1.5),
      ),
    ),
    chipTheme: base.chipTheme.copyWith(
      backgroundColor: MoltColors.secondary10,
      labelStyle: GoogleFonts.dmSans(color: MoltColors.secondary, fontSize: 13, fontWeight: FontWeight.w500),
      side: BorderSide.none,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(MoltColors.radiusPill)),
    ),
    switchTheme: SwitchThemeData(
      trackColor: WidgetStateProperty.resolveWith(
        (states) => states.contains(WidgetState.selected) ? MoltColors.primary : null,
      ),
    ),
    navigationBarTheme: NavigationBarThemeData(
      backgroundColor: MoltColors.neutral0,
      surfaceTintColor: Colors.transparent,
      indicatorColor: MoltColors.primary10,
      iconTheme: WidgetStateProperty.resolveWith(
        (states) => IconThemeData(
          color: states.contains(WidgetState.selected) ? MoltColors.primary : MoltColors.muted,
        ),
      ),
      labelTextStyle: WidgetStateProperty.resolveWith(
        (states) => GoogleFonts.dmSans(
          fontSize: 12,
          fontWeight: states.contains(WidgetState.selected) ? FontWeight.w700 : FontWeight.w500,
          color: states.contains(WidgetState.selected) ? MoltColors.primary : MoltColors.muted,
        ),
      ),
    ),
    snackBarTheme: const SnackBarThemeData(behavior: SnackBarBehavior.floating),
  );
}
