class TalentRef {
  TalentRef({required this.id, required this.name});

  final int id;
  final String name;

  factory TalentRef.fromJson(Map<String, dynamic> json) {
    return TalentRef(
      id: (json['id'] as num).toInt(),
      name: json['name'] as String,
    );
  }
}

class AgentProfile {
  AgentProfile({required this.model, required this.tools, this.operator});

  final String model;
  final List<String> tools;
  final TalentRef? operator;

  factory AgentProfile.fromJson(Map<String, dynamic> json) {
    return AgentProfile(
      model: json['model'] as String? ?? '',
      tools: (json['tools'] as List? ?? []).cast<String>(),
      operator: json['operator'] == null ? null : TalentRef.fromJson(json['operator'] as Map<String, dynamic>),
    );
  }
}
