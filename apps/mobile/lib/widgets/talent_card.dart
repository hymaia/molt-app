import 'package:flutter/material.dart';

import '../data/models/talent.dart';
import '../l10n/app_localizations.dart';
import '../theme/molt_colors.dart';
import '../theme/molt_theme.dart';
import 'avatar.dart';
import 'kind_badge.dart';
import 'rating_stars.dart';
import 'skill_chips.dart';

class TalentCard extends StatelessWidget {
  const TalentCard({super.key, required this.talent, this.onTap});

  final Talent talent;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    final isPremium = talent.dailyRateCents >= 80000;
    return Container(
      decoration: BoxDecoration(
        color: MoltColors.neutral0,
        borderRadius: BorderRadius.circular(MoltColors.radiusM),
        border: isPremium ? Border.all(color: const Color(0xFFE8C37A), width: 1.5) : null,
        boxShadow: MoltColors.cardShadow,
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(MoltColors.radiusM),
          onTap: onTap,
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Avatar(name: talent.name, url: talent.avatarUrl),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(talent.name, style: moltTitle(size: 16)),
                          const SizedBox(height: 2),
                          Text(
                            talent.title,
                            maxLines: 2,
                            overflow: TextOverflow.ellipsis,
                            style: const TextStyle(color: MoltColors.muted, fontSize: 14),
                          ),
                          if (talent.location != null) ...[
                            const SizedBox(height: 4),
                            Row(
                              children: [
                                const Icon(Icons.place_outlined, size: 14, color: MoltColors.muted),
                                const SizedBox(width: 2),
                                Flexible(
                                  child: Text(
                                    talent.location!,
                                    overflow: TextOverflow.ellipsis,
                                    style: const TextStyle(color: MoltColors.muted, fontSize: 13),
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ],
                      ),
                    ),
                    KindBadge(kind: talent.kind),
                  ],
                ),
                const SizedBox(height: 12),
                SkillChips(skills: talent.skills, max: 4),
                const SizedBox(height: 12),
                Row(
                  children: [
                    RatingStars(rating: talent.rating, size: 15),
                    const SizedBox(width: 8),
                    Text(
                      '· ${l10n.missionCount(talent.missionCount)}',
                      style: const TextStyle(color: MoltColors.muted, fontSize: 13),
                    ),
                    const Spacer(),
                    // Text(
                    //   l10n.ratePerDay(formatEuros(context, talent.dailyRateCents)),
                    //   style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16, color: MoltColors.text),
                    // ),
                    Text(
                      talent.displayRate,
                      style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16, color: MoltColors.text),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
