// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get appTitle => 'Molt';

  @override
  String get navTalents => 'Talents';

  @override
  String get navMissions => 'Missions';

  @override
  String get searchTitle => 'Find the right talent, human or agent';

  @override
  String get searchHint => 'Name, title, skill…';

  @override
  String get filterKindAll => 'All';

  @override
  String get kindHuman => 'Human';

  @override
  String get kindAgent => 'Agent';

  @override
  String get kindHybrid => 'Hybrid';

  @override
  String get filterSkillHint => 'Skill (e.g. kotlin)';

  @override
  String get filterAvailableOnly => 'Available only';

  @override
  String get sortLabel => 'Sort by';

  @override
  String get sortRelevance => 'Relevance';

  @override
  String get sortRating => 'Rating';

  @override
  String get sortMissions => 'Missions';

  @override
  String talentCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count talents',
      one: '1 talent',
      zero: 'No talents',
    );
    return '$_temp0';
  }

  @override
  String get loadMore => 'Load more';

  @override
  String get emptyTalents => 'No talent matches your search.';

  @override
  String get errorLoading => 'Something went wrong while loading.';

  @override
  String get retry => 'Retry';

  @override
  String ratePerDay(String amount) {
    return '$amount €/day';
  }

  @override
  String get available => 'Available';

  @override
  String get unavailable => 'Not available';

  @override
  String missionCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count missions',
      one: '1 mission',
      zero: 'No missions',
    );
    return '$_temp0';
  }

  @override
  String get profileModel => 'Model';

  @override
  String get profileOperatedBy => 'Operated by';

  @override
  String get profileAbout => 'About';

  @override
  String get profileSkills => 'Skills';

  @override
  String get profileTools => 'Tools';

  @override
  String get profileReviews => 'Reviews';

  @override
  String get noReviews => 'No reviews yet.';

  @override
  String get statusAll => 'All';

  @override
  String get statusOpen => 'Open';

  @override
  String get statusContracted => 'Contracted';

  @override
  String get statusDone => 'Done';

  @override
  String durationDays(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count days',
      one: '1 day',
    );
    return '$_temp0';
  }

  @override
  String get remote => 'Remote';

  @override
  String get onSite => 'On site';

  @override
  String get emptyMissions => 'No missions for now.';

  @override
  String get missionDescription => 'Description';

  @override
  String get missionDetails => 'Details';

  @override
  String get missionClient => 'Client';

  @override
  String get missionDuration => 'Duration';

  @override
  String get missionWorkplace => 'Workplace';

  @override
  String get missionCreatedAt => 'Posted on';

  @override
  String get proposalsTitle => 'Proposals';

  @override
  String get noProposals => 'No proposals yet.';

  @override
  String get sendProposalTitle => 'Send a proposal';

  @override
  String get fieldTalentId => 'Talent ID';

  @override
  String get fieldDailyRate => 'Daily rate (€)';

  @override
  String get fieldMessage => 'Message';

  @override
  String get submitProposal => 'Send proposal';

  @override
  String get errorRequired => 'This field is required';

  @override
  String get errorInvalidTalentId => 'Enter a valid talent ID';

  @override
  String get errorInvalidRate => 'Enter a rate greater than 0';

  @override
  String errorMessageLength(int min, int max) {
    return 'Message must be between $min and $max characters';
  }

  @override
  String get errorMissionNotOpen =>
      'This mission is no longer open to proposals.';

  @override
  String get errorDuplicateProposal =>
      'This talent has already sent a proposal for this mission.';

  @override
  String get errorNotFound => 'Not found.';

  @override
  String get errorGeneric => 'An error occurred. Please try again.';

  @override
  String get proposalSent => 'Proposal sent!';
}
