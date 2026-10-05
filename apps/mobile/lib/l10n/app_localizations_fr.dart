// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for French (`fr`).
class AppLocalizationsFr extends AppLocalizations {
  AppLocalizationsFr([String locale = 'fr']) : super(locale);

  @override
  String get appTitle => 'Molt';

  @override
  String get navTalents => 'Talents';

  @override
  String get navMissions => 'Missions';

  @override
  String get searchTitle => 'Trouvez le bon talent, humain ou agent';

  @override
  String get searchHint => 'Nom, titre, compétence…';

  @override
  String get filterKindAll => 'Tous';

  @override
  String get kindHuman => 'Humain';

  @override
  String get kindAgent => 'Agent';

  @override
  String get kindHybrid => 'Hybride';

  @override
  String get filterSkillHint => 'Compétence (ex. kotlin)';

  @override
  String get filterAvailableOnly => 'Disponibles uniquement';

  @override
  String get sortLabel => 'Trier par';

  @override
  String get sortRelevance => 'Pertinence';

  @override
  String get sortRating => 'Note';

  @override
  String get sortMissions => 'Missions';

  @override
  String talentCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count talents',
      one: '1 talent',
      zero: 'Aucun talent',
    );
    return '$_temp0';
  }

  @override
  String get loadMore => 'Voir plus';

  @override
  String get emptyTalents => 'Aucun talent ne correspond à votre recherche.';

  @override
  String get errorLoading => 'Une erreur est survenue lors du chargement.';

  @override
  String get retry => 'Réessayer';

  @override
  String ratePerDay(String amount) {
    return '$amount €/jour';
  }

  @override
  String get available => 'Disponible';

  @override
  String get unavailable => 'Indisponible';

  @override
  String missionCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count missions',
      one: '1 mission',
      zero: 'Aucune mission',
    );
    return '$_temp0';
  }

  @override
  String get profileModel => 'Modèle';

  @override
  String get profileOperatedBy => 'Opéré par';

  @override
  String get profileAbout => 'À propos';

  @override
  String get profileSkills => 'Compétences';

  @override
  String get profileTools => 'Outils';

  @override
  String get profileReviews => 'Avis';

  @override
  String get noReviews => 'Pas encore d\'avis.';

  @override
  String get statusAll => 'Toutes';

  @override
  String get statusOpen => 'Ouverte';

  @override
  String get statusContracted => 'Contractée';

  @override
  String get statusDone => 'Terminée';

  @override
  String durationDays(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count jours',
      one: '1 jour',
    );
    return '$_temp0';
  }

  @override
  String get remote => 'Télétravail';

  @override
  String get onSite => 'Sur site';

  @override
  String get emptyMissions => 'Aucune mission pour le moment.';

  @override
  String get missionDescription => 'Description';

  @override
  String get missionDetails => 'Détails';

  @override
  String get missionClient => 'Client';

  @override
  String get missionDuration => 'Durée';

  @override
  String get missionWorkplace => 'Lieu de travail';

  @override
  String get missionCreatedAt => 'Publiée le';

  @override
  String get proposalsTitle => 'Propositions';

  @override
  String get noProposals => 'Aucune proposition pour le moment.';

  @override
  String get sendProposalTitle => 'Envoyer une proposition';

  @override
  String get fieldTalentId => 'ID du talent';

  @override
  String get fieldDailyRate => 'TJM (€)';

  @override
  String get fieldMessage => 'Message';

  @override
  String get submitProposal => 'Envoyer la proposition';

  @override
  String get errorRequired => 'Ce champ est obligatoire';

  @override
  String get errorInvalidTalentId => 'Saisissez un ID de talent valide';

  @override
  String get errorInvalidRate => 'Saisissez un tarif supérieur à 0';

  @override
  String errorMessageLength(int min, int max) {
    return 'Le message doit contenir entre $min et $max caractères';
  }

  @override
  String get errorMissionNotOpen =>
      'Cette mission n\'accepte plus de propositions.';

  @override
  String get errorDuplicateProposal =>
      'Ce talent a déjà envoyé une proposition pour cette mission.';

  @override
  String get errorNotFound => 'Introuvable.';

  @override
  String get errorGeneric => 'Une erreur est survenue. Veuillez réessayer.';

  @override
  String get proposalSent => 'Proposition envoyée !';
}
