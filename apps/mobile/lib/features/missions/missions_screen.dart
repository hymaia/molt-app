import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../data/models/mission.dart';
import '../../data/providers.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../theme/molt_theme.dart';
import '../../widgets/kind_badge.dart';
import '../../widgets/molt_app_bar.dart';
import '../../widgets/skill_chips.dart';
import '../../widgets/state_views.dart';
import 'mission_status_pill.dart';

class MissionsScreen extends ConsumerWidget {
  const MissionsScreen({super.key});

  static const _filters = <MissionStatus?>[null, MissionStatus.open, MissionStatus.contracted, MissionStatus.done];

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final l10n = AppLocalizations.of(context);
    final status = ref.watch(missionStatusFilterProvider);
    final missions = ref.watch(missionsProvider);

    return Scaffold(
      appBar: moltAppBar(),
      body: Column(
        children: [
          Container(
            color: MoltColors.neutral0,
            width: double.infinity,
            padding: const EdgeInsets.fromLTRB(16, 8, 16, 16),
            child: SegmentedButton<MissionStatus?>(
              showSelectedIcon: false,
              segments: [
                for (final f in _filters) ButtonSegment(value: f, label: Text(missionStatusLabel(l10n, f))),
              ],
              selected: {status},
              onSelectionChanged: (s) => ref.read(missionStatusFilterProvider.notifier).set(s.first),
            ),
          ),
          Expanded(
            child: missions.when(
              loading: () => const LoadingView(),
              error: (_, _) => ErrorView(onRetry: () => ref.invalidate(missionsProvider)),
              data: (list) {
                if (list.isEmpty) {
                  return EmptyView(message: l10n.emptyMissions, icon: Icons.work_off_outlined);
                }
                return RefreshIndicator(
                  color: MoltColors.primary,
                  onRefresh: () => ref.refresh(missionsProvider.future),
                  child: ListView.separated(
                    padding: const EdgeInsets.all(16),
                    itemCount: list.length,
                    separatorBuilder: (_, _) => const SizedBox(height: 12),
                    itemBuilder: (context, i) => _MissionCard(mission: list[i]),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class _MissionCard extends StatelessWidget {
  const _MissionCard({required this.mission});

  final Mission mission;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    return Container(
      decoration: BoxDecoration(
        color: MoltColors.neutral0,
        borderRadius: BorderRadius.circular(MoltColors.radiusM),
        boxShadow: MoltColors.cardShadow,
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          borderRadius: BorderRadius.circular(MoltColors.radiusM),
          onTap: () => context.go('/missions/${mission.id}'),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Expanded(child: Text(mission.title, style: moltTitle(size: 16))),
                    const SizedBox(width: 8),
                    MissionStatusPill(status: mission.status),
                  ],
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Flexible(
                      child: Text(
                        mission.client.name,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(color: MoltColors.muted, fontWeight: FontWeight.w600),
                      ),
                    ),
                    const SizedBox(width: 8),
                    KindBadge(kind: mission.client.kind),
                  ],
                ),
                const SizedBox(height: 12),
                SkillChips(skills: mission.skills, max: 5),
                const SizedBox(height: 12),
                Row(
                  children: [
                    const Icon(Icons.schedule, size: 16, color: Color(0xFF4B4B4B)),
                    const SizedBox(width: 4),
                    Text(l10n.durationDays(mission.durationDays), style: const TextStyle(color: MoltColors.muted)),
                    const SizedBox(width: 16),
                    Icon(mission.remote ? Icons.home_work_outlined : Icons.apartment,
                        size: 16, color: MoltColors.muted),
                    const SizedBox(width: 4),
                    Text(mission.remote ? l10n.remote : l10n.onSite, style: const TextStyle(color: MoltColors.muted)),
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
