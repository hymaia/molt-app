import 'proposal.dart';
import 'talent_kind.dart';

enum MissionStatus {
  open,
  contracted,
  done;

  static MissionStatus fromJson(String value) =>
      MissionStatus.values.firstWhere((s) => s.name.toUpperCase() == value);

  String toJson() => name.toUpperCase();
}

class Client {
  Client({required this.id, required this.kind, required this.name});

  final int id;
  final TalentKind kind;
  final String name;

  factory Client.fromJson(Map<String, dynamic> json) {
    return Client(
      id: (json['id'] as num).toInt(),
      kind: TalentKind.fromJson(json['kind'] as String?),
      name: json['name'] as String,
    );
  }
}

class Mission {
  Mission({
    required this.id,
    required this.title,
    required this.client,
    required this.skills,
    required this.status,
    required this.durationDays,
    required this.remote,
    required this.createdAt,
  });

  final int id;
  final String title;
  final Client client;
  final List<String> skills;
  final MissionStatus status;
  final int durationDays;
  final bool remote;
  final DateTime createdAt;

  factory Mission.fromJson(Map<String, dynamic> json) {
    return Mission(
      id: (json['id'] as num).toInt(),
      title: json['title'] as String,
      client: Client.fromJson(json['client'] as Map<String, dynamic>),
      skills: (json['skills'] as List? ?? []).cast<String>(),
      status: MissionStatus.fromJson(json['status'] as String),
      durationDays: (json['durationDays'] as num).toInt(),
      remote: json['remote'] as bool? ?? false,
      createdAt: DateTime.parse(json['createdAt'] as String),
    );
  }
}

class MissionDetail extends Mission {
  MissionDetail({
    required super.id,
    required super.title,
    required super.client,
    required super.skills,
    required super.status,
    required super.durationDays,
    required super.remote,
    required super.createdAt,
    required this.description,
    required this.proposals,
  });

  final String description;
  final List<Proposal> proposals;

  factory MissionDetail.fromJson(Map<String, dynamic> json) {
    final m = Mission.fromJson(json);
    return MissionDetail(
      id: m.id,
      title: m.title,
      client: m.client,
      skills: m.skills,
      status: m.status,
      durationDays: m.durationDays,
      remote: m.remote,
      createdAt: m.createdAt,
      description: json['description'] as String? ?? '',
      proposals: (json['proposals'] as List? ?? [])
          .map((p) => Proposal.fromJson(p as Map<String, dynamic>))
          .toList(),
    );
  }
}
