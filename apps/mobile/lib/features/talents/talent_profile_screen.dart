import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../data/models/talent.dart';
import '../../data/providers.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../theme/molt_theme.dart';
import '../../utils/format.dart';
import '../../widgets/avatar.dart';
import '../../widgets/kind_badge.dart';
import '../../widgets/molt_app_bar.dart';
import '../../widgets/rating_stars.dart';
import '../../widgets/skill_chips.dart';
import '../../widgets/state_views.dart';

class TalentProfileScreen extends ConsumerWidget {
  const TalentProfileScreen({super.key, required this.talentId});

  final int talentId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final locale = Localizations.localeOf(context).toString();
    final profile = ref.watch(talentProfileProvider((talentId, locale)));
    return Scaffold(
      appBar: moltAppBar(),
      body: profile.when(
        loading: () => const LoadingView(),
        error: (_, _) => ErrorView(onRetry: () => ref.invalidate(talentDetailProvider(talentId))),
        data: (p) => _ProfileBody(data: p),
      ),
    );
  }
}

class _ProfileBody extends StatelessWidget {
  const _ProfileBody({required this.data});

  final TalentProfileData data;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    final TalentDetail talent = data.talent;
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        MoltCard(
          padding: const EdgeInsets.all(20),
          child: Column(
            children: [
              Avatar(name: talent.name, url: talent.avatarUrl, size: 96),
              const SizedBox(height: 12),
              Text(talent.name, textAlign: TextAlign.center, style: moltTitle(size: 22)),
              const SizedBox(height: 4),
              Text(
                talent.title,
                textAlign: TextAlign.center,
                style: const TextStyle(color: MoltColors.muted, fontSize: 15),
              ),
              if (talent.location != null) ...[
                const SizedBox(height: 6),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.place_outlined, size: 16, color: MoltColors.muted),
                    const SizedBox(width: 2),
                    Text(talent.location!, style: const TextStyle(color: MoltColors.muted)),
                  ],
                ),
              ],
              const SizedBox(height: 12),
              Wrap(
                alignment: WrapAlignment.center,
                spacing: 8,
                runSpacing: 8,
                children: [
                  KindBadge(kind: talent.kind),
                  talent.available
                      ? StatusPill(
                          label: l10n.available,
                          color: MoltColors.success,
                          background: MoltColors.success.withValues(alpha: 0.12),
                        )
                      : StatusPill(
                          label: l10n.unavailable,
                          color: MoltColors.muted,
                          background: MoltColors.bg,
                        ),
                ],
              ),
              const SizedBox(height: 12),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  RatingStars(rating: talent.rating),
                  const SizedBox(width: 8),
                  Text(
                    '· ${l10n.missionCount(talent.missionCount)}',
                    style: const TextStyle(color: MoltColors.muted),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Text(
                l10n.ratePerDay(data.rateAmount),
                style: moltTitle(size: 24, weight: FontWeight.w800),
              ),
              if (talent.agent != null) ...[
                const SizedBox(height: 16),
                const Divider(height: 1),
                _InfoRow(icon: Icons.memory, label: l10n.profileModel, value: talent.agent!.model),
                if (talent.agent!.operator != null) ...[
                  Padding(
                    padding: const EdgeInsets.only(top: 12, bottom: 8),
                    child: Row(
                      children: [
                        const Icon(Icons.support_agent, size: 18, color: MoltColors.ai),
                        const SizedBox(width: 8),
                        Text(l10n.profileOperatedBy, style: const TextStyle(color: MoltColors.muted)),
                      ],
                    ),
                  ),
                  _OperatorCard(operatorId: talent.agent!.operator!.id),
                ],
              ],
            ],
          ),
        ),
        SectionTitle(l10n.profileAbout),
        MoltCard(child: Text(talent.bio, style: const TextStyle(height: 1.5))),
        SectionTitle(l10n.profileSkills),
        SkillChips(skills: talent.skills),
        if (talent.agent != null && talent.agent!.tools.isNotEmpty) ...[
          SectionTitle(l10n.profileTools),
          Wrap(
            spacing: 6,
            runSpacing: 6,
            children: [
              for (final tool in talent.agent!.tools)
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: MoltColors.ai10,
                    borderRadius: BorderRadius.circular(MoltColors.radiusPill),
                  ),
                  child: Text(tool, style: const TextStyle(color: MoltColors.ai, fontSize: 12)),
                ),
            ],
          ),
        ],
        SectionTitle(l10n.profileReviews),
        if (talent.reviews.isEmpty)
          Text(l10n.noReviews, style: const TextStyle(color: MoltColors.muted))
        else
          for (final r in talent.reviews)
            Padding(
              padding: const EdgeInsets.only(bottom: 12),
              child: MoltCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Expanded(child: Text(r.author, style: const TextStyle(fontWeight: FontWeight.w700))),
                        Text(formatDate(context, r.createdAt),
                            style: const TextStyle(color: MoltColors.muted, fontSize: 12)),
                      ],
                    ),
                    const SizedBox(height: 4),
                    RatingStars(rating: r.rating.toDouble(), size: 14, showValue: false),
                    const SizedBox(height: 8),
                    Text(r.comment, style: const TextStyle(height: 1.4)),
                  ],
                ),
              ),
            ),
        const SizedBox(height: 24),
      ],
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({required this.icon, required this.label, required this.value});

  final IconData icon;
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 12),
      child: Row(
        children: [
          Icon(icon, size: 18, color: MoltColors.ai),
          const SizedBox(width: 8),
          Text(label, style: const TextStyle(color: MoltColors.muted)),
          const Spacer(),
          Flexible(
            child: Text(value, textAlign: TextAlign.end, style: const TextStyle(fontWeight: FontWeight.w600)),
          ),
        ],
      ),
    );
  }
}

final operatorProvider = FutureProvider.family<Map<String, dynamic>, int>((ref, id) async {
  final res = await Dio().get('http://localhost:8080/api/talents/$id');
  return res.data as Map<String, dynamic>;
});

class _OperatorCard extends ConsumerWidget {
  const _OperatorCard({required this.operatorId});

  final int operatorId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final op = ref.watch(operatorProvider(operatorId));
    return op.when(
      loading: () => const SizedBox(
        height: 72,
        child: Center(child: CircularProgressIndicator(strokeWidth: 2, color: MoltColors.primary)),
      ),
      error: (_, _) => const Text('Error'),
      data: (json) {
        final talent = Talent.fromJson(json);
        return Tooltip(
          message: "Voir le profil de l'opérateur",
          child: Container(
            decoration: BoxDecoration(
              color: const Color(0xFFFAFAF8),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: talent.kindColor.withValues(alpha: 0.25)),
            ),
            child: Material(
              color: Colors.transparent,
              child: InkWell(
                borderRadius: BorderRadius.circular(12),
                onTap: () => context.push('/talents/${talent.id}'),
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Avatar(name: talent.name, url: talent.avatarUrl, size: 44),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(talent.name, style: moltTitle(size: 15)),
                            const SizedBox(height: 2),
                            Text(
                              talent.title,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: const TextStyle(color: Color(0xFF6B6B6B), fontSize: 13),
                            ),
                            const SizedBox(height: 6),
                            Row(
                              children: [
                                RatingStars(rating: talent.rating, size: 13),
                                const Spacer(),
                                Text(
                                  talent.displayRate,
                                  style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 14, color: MoltColors.text),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
