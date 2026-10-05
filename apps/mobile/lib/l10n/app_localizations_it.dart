// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for Italian (`it`).
class AppLocalizationsIt extends AppLocalizations {
  AppLocalizationsIt([String locale = 'it']) : super(locale);

  @override
  String get appTitle => 'Molt';

  @override
  String get navTalents => 'Talenti';

  @override
  String get navMissions => 'Missioni';

  @override
  String get searchTitle => 'Trova il talento giusto, umano o agente';

  @override
  String get searchHint => 'Nome, titolo, competenza…';

  @override
  String get filterKindAll => 'Tutti';

  @override
  String get kindHuman => 'Umano';

  @override
  String get kindAgent => 'Agente';

  @override
  String get kindHybrid => 'Ibrido';

  @override
  String get filterSkillHint => 'Competenza (es. kotlin)';

  @override
  String get filterAvailableOnly => 'Solo disponibili';

  @override
  String get sortLabel => 'Ordina per';

  @override
  String get sortRelevance => 'Pertinenza';

  @override
  String get sortRating => 'Valutazione';

  @override
  String get sortMissions => 'Missioni';

  @override
  String talentCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count talenti',
      one: '1 talento',
      zero: 'Nessun talento',
    );
    return '$_temp0';
  }

  @override
  String get loadMore => 'Mostra altri';

  @override
  String get emptyTalents => 'Nessun talento corrisponde alla tua ricerca.';

  @override
  String get errorLoading =>
      'Si è verificato un errore durante il caricamento.';

  @override
  String get retry => 'Riprova';

  @override
  String ratePerDay(String amount) {
    return '$amount €/giorno';
  }

  @override
  String get available => 'Disponibile';

  @override
  String get unavailable => 'Non disponibile';

  @override
  String missionCount(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count missioni',
      one: '1 missione',
      zero: 'Nessuna missione',
    );
    return '$_temp0';
  }

  @override
  String get profileModel => 'Modello';

  @override
  String get profileOperatedBy => 'Gestito da';

  @override
  String get profileAbout => 'Chi sono';

  @override
  String get profileSkills => 'Competenze';

  @override
  String get profileTools => 'Strumenti';

  @override
  String get profileReviews => 'Reviews';

  @override
  String get noReviews => 'Ancora nessuna recensione.';

  @override
  String get statusAll => 'Tutte';

  @override
  String get statusOpen => 'Aperta';

  @override
  String get statusContracted => 'Contrattualizzata';

  @override
  String get statusDone => 'Completata';

  @override
  String durationDays(int count) {
    String _temp0 = intl.Intl.pluralLogic(
      count,
      locale: localeName,
      other: '$count giorni',
      one: '1 giorno',
    );
    return '$_temp0';
  }

  @override
  String get remote => 'Da remoto';

  @override
  String get onSite => 'In sede';

  @override
  String get emptyMissions => 'Nessuna missione al momento.';

  @override
  String get missionDescription => 'Descrizione';

  @override
  String get missionDetails => 'Dettagli';

  @override
  String get missionClient => 'Cliente';

  @override
  String get missionDuration => 'Durata';

  @override
  String get missionWorkplace => 'Luogo di lavoro';

  @override
  String get missionCreatedAt => 'Pubblicata il';

  @override
  String get proposalsTitle => 'Proposte';

  @override
  String get noProposals => 'Ancora nessuna proposta.';

  @override
  String get sendProposalTitle => 'Invia una proposta';

  @override
  String get fieldTalentId => 'ID del talento';

  @override
  String get fieldDailyRate => 'Tariffa giornaliera (€)';

  @override
  String get fieldMessage => 'Messaggio';

  @override
  String get submitProposal => 'Invia proposta';

  @override
  String get errorRequired => 'Campo obbligatorio';

  @override
  String get errorInvalidTalentId => 'Inserisci un ID talento valido';

  @override
  String get errorInvalidRate => 'Inserisci una tariffa maggiore di 0';

  @override
  String errorMessageLength(int min, int max) {
    return 'Il messaggio deve contenere tra $min e $max caratteri';
  }

  @override
  String get errorMissionNotOpen => 'Questa missione non accetta più proposte.';

  @override
  String get errorDuplicateProposal =>
      'Questo talento ha già inviato una proposta per questa missione.';

  @override
  String get errorNotFound => 'Non trovato.';

  @override
  String get errorGeneric => 'Si è verificato un errore. Riprova.';

  @override
  String get proposalSent => 'Proposta inviata!';
}
