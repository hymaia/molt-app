import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import '../theme/molt_colors.dart';

class MoltWordmark extends StatelessWidget {
  const MoltWordmark({super.key});

  @override
  Widget build(BuildContext context) {
    return Text(
      'molt',
      style: GoogleFonts.archivo(
        color: MoltColors.primary,
        fontSize: 26,
        fontWeight: FontWeight.w800,
        letterSpacing: -1,
      ),
    );
  }
}

AppBar moltAppBar({Widget? title, List<Widget>? actions}) {
  return AppBar(
    title: title ?? const MoltWordmark(),
    centerTitle: false,
    actions: actions,
  );
}
