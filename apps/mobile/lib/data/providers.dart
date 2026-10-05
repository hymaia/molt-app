import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import 'api_client.dart';
import 'models/mission.dart';
import 'models/talent.dart';
import 'repositories/mission_repository.dart';
import 'repositories/talent_repository.dart';

final apiClientProvider = Provider<ApiClient>((ref) => ApiClient());

final talentRepositoryProvider = Provider<TalentRepository>(
  (ref) => TalentRepository(ref.watch(apiClientProvider)),
);

final missionRepositoryProvider = Provider<MissionRepository>(
  (ref) => MissionRepository(ref.watch(apiClientProvider)),
);

final talentDetailProvider = FutureProvider.family<TalentDetail, int>(
  (ref, id) => ref.watch(talentRepositoryProvider).getTalent(id),
);

class TalentProfileData {
  TalentProfileData({required this.talent, required this.rateAmount, required this.premium});

  final TalentDetail talent;
  final String rateAmount;
  final bool premium;
}

final talentProfileProvider = FutureProvider.autoDispose.family<TalentProfileData, (int, String)>((ref, args) async {
  final (id, locale) = args;
  final talent = await ref.watch(talentDetailProvider(id).future);
  final euros = talent.dailyRateCents / 100;
  final fmt = euros == euros.roundToDouble()
      ? NumberFormat.decimalPattern(locale)
      : NumberFormat.decimalPatternDigits(locale: locale, decimalDigits: 2);
  return TalentProfileData(
    talent: talent,
    rateAmount: fmt.format(euros),
    premium: talent.dailyRateCents >= 80000,
  );
});

final missionStatusFilterProvider = NotifierProvider<MissionStatusFilter, MissionStatus?>(
  MissionStatusFilter.new,
);

class MissionStatusFilter extends Notifier<MissionStatus?> {
  @override
  MissionStatus? build() => null;

  void set(MissionStatus? status) => state = status;
}

final missionsProvider = FutureProvider.autoDispose<List<Mission>>((ref) {
  final status = ref.watch(missionStatusFilterProvider);
  return ref.watch(missionRepositoryProvider).list(status: status);
});

final missionDetailProvider = FutureProvider.autoDispose.family<MissionDetail, int>((ref, id) async {
  final json = await ref.watch(missionRepositoryProvider).getMission(id);
  return MissionDetail.fromJson(json);
});
