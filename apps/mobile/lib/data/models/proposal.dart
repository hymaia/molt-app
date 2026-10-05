class Proposal {
  Proposal({
    required this.id,
    required this.talentId,
    required this.talentName,
    required this.dailyRateCents,
    required this.message,
    required this.createdAt,
  });

  final int id;
  final int talentId;
  final String talentName;
  final int dailyRateCents;
  final String message;
  final DateTime createdAt;

  factory Proposal.fromJson(Map<String, dynamic> json) {
    return Proposal(
      id: (json['id'] as num).toInt(),
      talentId: (json['talentId'] as num).toInt(),
      talentName: json['talentName'] as String,
      dailyRateCents: (json['dailyRateCents'] as num).toInt(),
      message: json['message'] as String,
      createdAt: DateTime.parse(json['createdAt'] as String),
    );
  }
}

class ProposalRequest {
  ProposalRequest({required this.talentId, required this.dailyRateCents, required this.message});

  final int talentId;
  final int dailyRateCents;
  final String message;

  Map<String, dynamic> toJson() => {
        'talentId': talentId,
        'dailyRateCents': dailyRateCents,
        'message': message,
      };
}
