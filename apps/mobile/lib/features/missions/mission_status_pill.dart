import 'package:flutter/material.dart';

import '../../data/models/mission.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../widgets/kind_badge.dart';

class MissionStatusPill extends StatelessWidget {
  const MissionStatusPill({super.key, required this.status});

  final MissionStatus status;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    switch (status) {
      case MissionStatus.open:
        return StatusPill(
          label: l10n.statusOpen,
          color: MoltColors.success,
          background: MoltColors.success.withValues(alpha: 0.12),
        );
      case MissionStatus.contracted:
        return StatusPill(
          label: l10n.statusContracted,
          color: MoltColors.secondary,
          background: MoltColors.secondary10,
        );
      case MissionStatus.done:
        return StatusPill(label: l10n.statusDone, color: MoltColors.muted, background: MoltColors.bg);
    }
  }
}

String missionStatusLabel(AppLocalizations l10n, MissionStatus? status) {
  switch (status) {
    case null:
      return l10n.statusAll;
    case MissionStatus.open:
      return l10n.statusOpen;
    case MissionStatus.contracted:
      return l10n.statusContracted;
    case MissionStatus.done:
      return l10n.statusDone;
  }
}
