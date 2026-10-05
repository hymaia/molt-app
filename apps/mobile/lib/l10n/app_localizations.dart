import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart' as intl;

import 'app_localizations_en.dart';
import 'app_localizations_fr.dart';
import 'app_localizations_it.dart';

// ignore_for_file: type=lint

/// Callers can lookup localized strings with an instance of AppLocalizations
/// returned by `AppLocalizations.of(context)`.
///
/// Applications need to include `AppLocalizations.delegate()` in their app's
/// `localizationDelegates` list, and the locales they support in the app's
/// `supportedLocales` list. For example:
///
/// ```dart
/// import 'l10n/app_localizations.dart';
///
/// return MaterialApp(
///   localizationsDelegates: AppLocalizations.localizationsDelegates,
///   supportedLocales: AppLocalizations.supportedLocales,
///   home: MyApplicationHome(),
/// );
/// ```
///
/// ## Update pubspec.yaml
///
/// Please make sure to update your pubspec.yaml to include the following
/// packages:
///
/// ```yaml
/// dependencies:
///   # Internationalization support.
///   flutter_localizations:
///     sdk: flutter
///   intl: any # Use the pinned version from flutter_localizations
///
///   # Rest of dependencies
/// ```
///
/// ## iOS Applications
///
/// iOS applications define key application metadata, including supported
/// locales, in an Info.plist file that is built into the application bundle.
/// To configure the locales supported by your app, you’ll need to edit this
/// file.
///
/// First, open your project’s ios/Runner.xcworkspace Xcode workspace file.
/// Then, in the Project Navigator, open the Info.plist file under the Runner
/// project’s Runner folder.
///
/// Next, select the Information Property List item, select Add Item from the
/// Editor menu, then select Localizations from the pop-up menu.
///
/// Select and expand the newly-created Localizations item then, for each
/// locale your application supports, add a new item and select the locale
/// you wish to add from the pop-up menu in the Value field. This list should
/// be consistent with the languages listed in the AppLocalizations.supportedLocales
/// property.
abstract class AppLocalizations {
  AppLocalizations(String locale)
    : localeName = intl.Intl.canonicalizedLocale(locale.toString());

  final String localeName;

  static AppLocalizations of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations)!;
  }

  static const LocalizationsDelegate<AppLocalizations> delegate =
      _AppLocalizationsDelegate();

  /// A list of this localizations delegate along with the default localizations
  /// delegates.
  ///
  /// Returns a list of localizations delegates containing this delegate along with
  /// GlobalMaterialLocalizations.delegate, GlobalCupertinoLocalizations.delegate,
  /// and GlobalWidgetsLocalizations.delegate.
  ///
  /// Additional delegates can be added by appending to this list in
  /// MaterialApp. This list does not have to be used at all if a custom list
  /// of delegates is preferred or required.
  static const List<LocalizationsDelegate<dynamic>> localizationsDelegates =
      <LocalizationsDelegate<dynamic>>[
        delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
      ];

  /// A list of this localizations delegate's supported locales.
  static const List<Locale> supportedLocales = <Locale>[
    Locale('en'),
    Locale('fr'),
    Locale('it'),
  ];

  /// Application name
  ///
  /// In en, this message translates to:
  /// **'Molt'**
  String get appTitle;

  /// No description provided for @navTalents.
  ///
  /// In en, this message translates to:
  /// **'Talents'**
  String get navTalents;

  /// No description provided for @navMissions.
  ///
  /// In en, this message translates to:
  /// **'Missions'**
  String get navMissions;

  /// No description provided for @searchTitle.
  ///
  /// In en, this message translates to:
  /// **'Find the right talent, human or agent'**
  String get searchTitle;

  /// No description provided for @searchHint.
  ///
  /// In en, this message translates to:
  /// **'Name, title, skill…'**
  String get searchHint;

  /// No description provided for @filterKindAll.
  ///
  /// In en, this message translates to:
  /// **'All'**
  String get filterKindAll;

  /// No description provided for @kindHuman.
  ///
  /// In en, this message translates to:
  /// **'Human'**
  String get kindHuman;

  /// No description provided for @kindAgent.
  ///
  /// In en, this message translates to:
  /// **'Agent'**
  String get kindAgent;

  /// No description provided for @kindHybrid.
  ///
  /// In en, this message translates to:
  /// **'Hybrid'**
  String get kindHybrid;

  /// No description provided for @filterSkillHint.
  ///
  /// In en, this message translates to:
  /// **'Skill (e.g. kotlin)'**
  String get filterSkillHint;

  /// No description provided for @filterAvailableOnly.
  ///
  /// In en, this message translates to:
  /// **'Available only'**
  String get filterAvailableOnly;

  /// No description provided for @sortLabel.
  ///
  /// In en, this message translates to:
  /// **'Sort by'**
  String get sortLabel;

  /// No description provided for @sortRelevance.
  ///
  /// In en, this message translates to:
  /// **'Relevance'**
  String get sortRelevance;

  /// No description provided for @sortRating.
  ///
  /// In en, this message translates to:
  /// **'Rating'**
  String get sortRating;

  /// No description provided for @sortMissions.
  ///
  /// In en, this message translates to:
  /// **'Missions'**
  String get sortMissions;

  /// No description provided for @talentCount.
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =0{No talents} =1{1 talent} other{{count} talents}}'**
  String talentCount(int count);

  /// No description provided for @loadMore.
  ///
  /// In en, this message translates to:
  /// **'Load more'**
  String get loadMore;

  /// No description provided for @emptyTalents.
  ///
  /// In en, this message translates to:
  /// **'No talent matches your search.'**
  String get emptyTalents;

  /// No description provided for @errorLoading.
  ///
  /// In en, this message translates to:
  /// **'Something went wrong while loading.'**
  String get errorLoading;

  /// No description provided for @retry.
  ///
  /// In en, this message translates to:
  /// **'Retry'**
  String get retry;

  /// No description provided for @ratePerDay.
  ///
  /// In en, this message translates to:
  /// **'{amount} €/day'**
  String ratePerDay(String amount);

  /// No description provided for @available.
  ///
  /// In en, this message translates to:
  /// **'Available'**
  String get available;

  /// No description provided for @unavailable.
  ///
  /// In en, this message translates to:
  /// **'Not available'**
  String get unavailable;

  /// No description provided for @missionCount.
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =0{No missions} =1{1 mission} other{{count} missions}}'**
  String missionCount(int count);

  /// No description provided for @profileModel.
  ///
  /// In en, this message translates to:
  /// **'Model'**
  String get profileModel;

  /// No description provided for @profileOperatedBy.
  ///
  /// In en, this message translates to:
  /// **'Operated by'**
  String get profileOperatedBy;

  /// No description provided for @profileAbout.
  ///
  /// In en, this message translates to:
  /// **'About'**
  String get profileAbout;

  /// No description provided for @profileSkills.
  ///
  /// In en, this message translates to:
  /// **'Skills'**
  String get profileSkills;

  /// No description provided for @profileTools.
  ///
  /// In en, this message translates to:
  /// **'Tools'**
  String get profileTools;

  /// No description provided for @profileReviews.
  ///
  /// In en, this message translates to:
  /// **'Reviews'**
  String get profileReviews;

  /// No description provided for @noReviews.
  ///
  /// In en, this message translates to:
  /// **'No reviews yet.'**
  String get noReviews;

  /// No description provided for @statusAll.
  ///
  /// In en, this message translates to:
  /// **'All'**
  String get statusAll;

  /// No description provided for @statusOpen.
  ///
  /// In en, this message translates to:
  /// **'Open'**
  String get statusOpen;

  /// No description provided for @statusContracted.
  ///
  /// In en, this message translates to:
  /// **'Contracted'**
  String get statusContracted;

  /// No description provided for @statusDone.
  ///
  /// In en, this message translates to:
  /// **'Done'**
  String get statusDone;

  /// No description provided for @durationDays.
  ///
  /// In en, this message translates to:
  /// **'{count, plural, =1{1 day} other{{count} days}}'**
  String durationDays(int count);

  /// No description provided for @remote.
  ///
  /// In en, this message translates to:
  /// **'Remote'**
  String get remote;

  /// No description provided for @onSite.
  ///
  /// In en, this message translates to:
  /// **'On site'**
  String get onSite;

  /// No description provided for @emptyMissions.
  ///
  /// In en, this message translates to:
  /// **'No missions for now.'**
  String get emptyMissions;

  /// No description provided for @missionDescription.
  ///
  /// In en, this message translates to:
  /// **'Description'**
  String get missionDescription;

  /// No description provided for @missionDetails.
  ///
  /// In en, this message translates to:
  /// **'Details'**
  String get missionDetails;

  /// No description provided for @missionClient.
  ///
  /// In en, this message translates to:
  /// **'Client'**
  String get missionClient;

  /// No description provided for @missionDuration.
  ///
  /// In en, this message translates to:
  /// **'Duration'**
  String get missionDuration;

  /// No description provided for @missionWorkplace.
  ///
  /// In en, this message translates to:
  /// **'Workplace'**
  String get missionWorkplace;

  /// No description provided for @missionCreatedAt.
  ///
  /// In en, this message translates to:
  /// **'Posted on'**
  String get missionCreatedAt;

  /// No description provided for @proposalsTitle.
  ///
  /// In en, this message translates to:
  /// **'Proposals'**
  String get proposalsTitle;

  /// No description provided for @noProposals.
  ///
  /// In en, this message translates to:
  /// **'No proposals yet.'**
  String get noProposals;

  /// No description provided for @sendProposalTitle.
  ///
  /// In en, this message translates to:
  /// **'Send a proposal'**
  String get sendProposalTitle;

  /// No description provided for @fieldTalentId.
  ///
  /// In en, this message translates to:
  /// **'Talent ID'**
  String get fieldTalentId;

  /// No description provided for @fieldDailyRate.
  ///
  /// In en, this message translates to:
  /// **'Daily rate (€)'**
  String get fieldDailyRate;

  /// No description provided for @fieldMessage.
  ///
  /// In en, this message translates to:
  /// **'Message'**
  String get fieldMessage;

  /// No description provided for @submitProposal.
  ///
  /// In en, this message translates to:
  /// **'Send proposal'**
  String get submitProposal;

  /// No description provided for @errorRequired.
  ///
  /// In en, this message translates to:
  /// **'This field is required'**
  String get errorRequired;

  /// No description provided for @errorInvalidTalentId.
  ///
  /// In en, this message translates to:
  /// **'Enter a valid talent ID'**
  String get errorInvalidTalentId;

  /// No description provided for @errorInvalidRate.
  ///
  /// In en, this message translates to:
  /// **'Enter a rate greater than 0'**
  String get errorInvalidRate;

  /// No description provided for @errorMessageLength.
  ///
  /// In en, this message translates to:
  /// **'Message must be between {min} and {max} characters'**
  String errorMessageLength(int min, int max);

  /// No description provided for @errorMissionNotOpen.
  ///
  /// In en, this message translates to:
  /// **'This mission is no longer open to proposals.'**
  String get errorMissionNotOpen;

  /// No description provided for @errorDuplicateProposal.
  ///
  /// In en, this message translates to:
  /// **'This talent has already sent a proposal for this mission.'**
  String get errorDuplicateProposal;

  /// No description provided for @errorNotFound.
  ///
  /// In en, this message translates to:
  /// **'Not found.'**
  String get errorNotFound;

  /// No description provided for @errorGeneric.
  ///
  /// In en, this message translates to:
  /// **'An error occurred. Please try again.'**
  String get errorGeneric;

  /// No description provided for @proposalSent.
  ///
  /// In en, this message translates to:
  /// **'Proposal sent!'**
  String get proposalSent;
}

class _AppLocalizationsDelegate
    extends LocalizationsDelegate<AppLocalizations> {
  const _AppLocalizationsDelegate();

  @override
  Future<AppLocalizations> load(Locale locale) {
    return SynchronousFuture<AppLocalizations>(lookupAppLocalizations(locale));
  }

  @override
  bool isSupported(Locale locale) =>
      <String>['en', 'fr', 'it'].contains(locale.languageCode);

  @override
  bool shouldReload(_AppLocalizationsDelegate old) => false;
}

AppLocalizations lookupAppLocalizations(Locale locale) {
  // Lookup logic when only language code is specified.
  switch (locale.languageCode) {
    case 'en':
      return AppLocalizationsEn();
    case 'fr':
      return AppLocalizationsFr();
    case 'it':
      return AppLocalizationsIt();
  }

  throw FlutterError(
    'AppLocalizations.delegate failed to load unsupported locale "$locale". This is likely '
    'an issue with the localizations generation tool. Please file an issue '
    'on GitHub with a reproducible sample app and the gen-l10n configuration '
    'that was used.',
  );
}
