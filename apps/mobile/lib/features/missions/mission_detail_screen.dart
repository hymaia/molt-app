import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../data/models/mission.dart';
import '../../data/providers.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../theme/molt_theme.dart';
import '../../utils/format.dart';
import '../../widgets/kind_badge.dart';
import '../../widgets/molt_app_bar.dart';
import '../../widgets/skill_chips.dart';
import '../../widgets/state_views.dart';
import 'mission_status_pill.dart';
import 'proposal_form.dart';

class MissionDetailScreen extends ConsumerWidget {
  const MissionDetailScreen({super.key, required this.missionId});

  final int missionId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final mission = ref.watch(missionDetailProvider(missionId));
    return Scaffold(
      appBar: moltAppBar(),
      body: mission.when(
        loading: () => const LoadingView(),
        error: (_, _) => ErrorView(onRetry: () => ref.invalidate(missionDetailProvider(missionId))),
        data: (m) => _MissionBody(mission: m),
      ),
    );
  }
}

class _MissionBody extends StatelessWidget {
  const _MissionBody({required this.mission});

  final MissionDetail mission;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        MoltCard(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              MissionStatusPill(status: mission.status),
              const SizedBox(height: 10),
              Text(mission.title, style: moltTitle(size: 22)),
              const SizedBox(height: 10),
              Row(
                children: [
                  Flexible(
                    child: Text(
                      mission.client.name,
                      style: const TextStyle(color: MoltColors.muted, fontWeight: FontWeight.w600),
                    ),
                  ),
                  const SizedBox(width: 8),
                  KindBadge(kind: mission.client.kind),
                ],
              ),
              const SizedBox(height: 12),
              SkillChips(skills: mission.skills),
            ],
          ),
        ),
        SectionTitle(l10n.missionDescription),
        MoltCard(child: Text(mission.description, style: const TextStyle(height: 1.5))),
        SectionTitle(l10n.missionDetails),
        MoltCard(
          child: Column(
            children: [
              _DetailRow(label: l10n.missionClient, value: mission.client.name),
              _DetailRow(label: l10n.missionDuration, value: l10n.durationDays(mission.durationDays)),
              _DetailRow(label: l10n.missionWorkplace, value: mission.remote ? l10n.remote : l10n.onSite),
              _DetailRow(label: l10n.missionCreatedAt, value: formatDate(context, mission.createdAt), last: true),
            ],
          ),
        ),
        SectionTitle('${l10n.proposalsTitle} (${mission.proposals.length})'),
        if (mission.proposals.isEmpty)
          Text(l10n.noProposals, style: const TextStyle(color: MoltColors.muted))
        else
          for (final p in mission.proposals)
            Padding(
              padding: const EdgeInsets.only(bottom: 12),
              child: MoltCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Expanded(child: Text(p.talentName, style: const TextStyle(fontWeight: FontWeight.w700))),
                        Text(
                          l10n.ratePerDay(formatEuros(context, p.dailyRateCents)),
                          style: const TextStyle(fontWeight: FontWeight.w800),
                        ),
                      ],
                    ),
                    const SizedBox(height: 4),
                    Text(formatDate(context, p.createdAt),
                        style: const TextStyle(color: MoltColors.muted, fontSize: 12)),
                    const SizedBox(height: 8),
                    Text(p.message, style: const TextStyle(height: 1.4)),
                  ],
                ),
              ),
            ),
        if (mission.status == MissionStatus.open) ...[
          SectionTitle(l10n.sendProposalTitle),
          ProposalForm(missionId: mission.id),
        ],
        const SizedBox(height: 24),
      ],
    );
  }
}

class _DetailRow extends StatelessWidget {
  const _DetailRow({required this.label, required this.value, this.last = false});

  final String label;
  final String value;
  final bool last;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(bottom: last ? 0 : 10),
      child: Row(
        children: [
          Text(label, style: const TextStyle(color: MoltColors.muted)),
          const Spacer(),
          Text(value, style: const TextStyle(fontWeight: FontWeight.w600)),
        ],
      ),
    );
  }
}
