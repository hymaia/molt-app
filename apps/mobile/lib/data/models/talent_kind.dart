enum TalentKind {
  human,
  agent;

  static TalentKind fromJson(String? value) {
    return TalentKind.values.firstWhere(
      (k) => k.name.toUpperCase() == value,
      orElse: () => TalentKind.agent,
    );
  }

  String toJson() => name.toUpperCase();
}
