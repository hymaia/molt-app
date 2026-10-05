import 'package:flutter/material.dart';

import '../data/models/talent_kind.dart';
import '../l10n/app_localizations.dart';
import '../theme/molt_colors.dart';

class KindBadge extends StatelessWidget {
  const KindBadge({super.key, required this.kind});

  final TalentKind kind;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    final isAgent = kind == TalentKind.agent;
    final label = isAgent ? l10n.kindAgent : l10n.kindHuman;
    final fg = isAgent ? MoltColors.ai : MoltColors.secondary;
    final bg = isAgent ? MoltColors.ai10 : MoltColors.secondary10;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 3),
      decoration: BoxDecoration(color: bg, borderRadius: BorderRadius.circular(MoltColors.radiusPill)),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(isAgent ? Icons.smart_toy_outlined : Icons.person_outline, size: 14, color: fg),
          const SizedBox(width: 4),
          Text(label, style: TextStyle(color: fg, fontSize: 12, fontWeight: FontWeight.w700)),
        ],
      ),
    );
  }
}

class StatusPill extends StatelessWidget {
  const StatusPill({super.key, required this.label, required this.color, required this.background});

  final String label;
  final Color color;
  final Color background;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 3),
      decoration: BoxDecoration(color: background, borderRadius: BorderRadius.circular(MoltColors.radiusPill)),
      child: Text(label, style: TextStyle(color: color, fontSize: 12, fontWeight: FontWeight.w700)),
    );
  }
}
