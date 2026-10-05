import 'package:flutter/material.dart';

import '../theme/molt_colors.dart';

class SkillChips extends StatelessWidget {
  const SkillChips({super.key, required this.skills, this.max});

  final List<String> skills;
  final int? max;

  @override
  Widget build(BuildContext context) {
    final shown = max != null && skills.length > max! ? skills.take(max!).toList() : skills;
    final extra = skills.length - shown.length;
    return Wrap(
      spacing: 6,
      runSpacing: 6,
      children: [
        for (final s in shown) _chip(s),
        if (extra > 0) _chip('+$extra'),
      ],
    );
  }

  Widget _chip(String label) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: MoltColors.secondary10,
        borderRadius: BorderRadius.circular(MoltColors.radiusPill),
      ),
      child: Text(
        label,
        style: const TextStyle(color: MoltColors.secondary, fontSize: 12, fontWeight: FontWeight.w500),
      ),
    );
  }
}
