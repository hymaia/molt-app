import 'package:flutter/material.dart';

class MoltColors {
  MoltColors._();

  static const primary = Color(0xFFFC5757);
  static const primary70 = Color(0xFFD4524F);
  static const primary10 = Color(0xFFFFF4F4);

  static const secondary = Color(0xFF035266);
  static const secondary10 = Color(0xFFECF3F4);

  static const neutral0 = Color(0xFFFFFFFF);
  static const bg = Color(0xFFF7F7F5);
  static const text = Color(0xFF181818);
  static const muted = Color(0xFF4B4B4B);
  static const border = Color(0xFFE6E6E3);

  static const success = Color(0xFF81B928);
  static const warning = Color(0xFFF2A10C);
  static const error = Color(0xFFE02D2D);

  static const ai = Color(0xFF9035A2);
  static const ai10 = Color(0xFFF2E0F5);

  static const radiusS = 8.0;
  static const radiusM = 16.0;
  static const radiusPill = 32.0;

  static const cardShadow = [
    BoxShadow(
      color: Color(0x14181818),
      blurRadius: 16,
      offset: Offset(0, 4),
    ),
  ];
}
