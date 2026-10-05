import 'package:flutter/material.dart';

import 'agent_profile.dart';
import 'review.dart';
import 'talent_kind.dart';

class Talent {
  Talent({
    required this.id,
    required this.kind,
    required this.name,
    required this.title,
    this.location,
    this.avatarUrl,
    required this.skills,
    required this.dailyRateCents,
    required this.rating,
    required this.missionCount,
    required this.available,
    this.agent,
  });

  final int id;
  final TalentKind kind;
  final String name;
  final String title;
  final String? location;
  final String? avatarUrl;
  final List<String> skills;
  final int dailyRateCents;
  final double rating;
  final int missionCount;
  final bool available;
  final AgentProfile? agent;

  factory Talent.fromJson(Map<String, dynamic> json) {
    return Talent(
      id: (json['id'] as num).toInt(),
      kind: TalentKind.fromJson(json['kind'] as String?),
      name: json['name'] as String,
      title: json['title'] as String,
      location: json['location'] as String,
      avatarUrl: json['avatarUrl'] as String?,
      skills: (json['skills'] as List? ?? []).cast<String>(),
      dailyRateCents: (json['dailyRateCents'] as num).toInt(),
      rating: json['rating'] as double,
      missionCount: (json['missionCount'] as num).toInt(),
      available: json['available'] as bool? ?? false,
      agent: json['agent'] == null ? null : AgentProfile.fromJson(json['agent'] as Map<String, dynamic>),
    );
  }

  Color get kindColor => kind == TalentKind.agent ? const Color(0xFF9035A2) : const Color(0xFF035266);

  String get displayRate => '${dailyRateCents ~/ 100} €/j';
}

class TalentDetail extends Talent {
  TalentDetail({
    required super.id,
    required super.kind,
    required super.name,
    required super.title,
    super.location,
    super.avatarUrl,
    required super.skills,
    required super.dailyRateCents,
    required super.rating,
    required super.missionCount,
    required super.available,
    super.agent,
    required this.bio,
    required this.reviews,
  });

  final String bio;
  final List<Review> reviews;

  factory TalentDetail.fromJson(Map<String, dynamic> json) {
    final t = Talent.fromJson(json);
    return TalentDetail(
      id: t.id,
      kind: t.kind,
      name: t.name,
      title: t.title,
      location: t.location,
      avatarUrl: t.avatarUrl,
      skills: t.skills,
      dailyRateCents: t.dailyRateCents,
      rating: t.rating,
      missionCount: t.missionCount,
      available: t.available,
      agent: t.agent,
      bio: json['bio'] as String? ?? '',
      reviews: (json['reviews'] as List? ?? [])
          .map((r) => Review.fromJson(r as Map<String, dynamic>))
          .toList(),
    );
  }
}

class TalentPage {
  TalentPage({required this.items, required this.page, required this.size, required this.total});

  final List<Talent> items;
  final int page;
  final int size;
  final int total;

  factory TalentPage.fromJson(Map<String, dynamic> json) {
    return TalentPage(
      items: (json['items'] as List).map((e) => Talent.fromJson(e as Map<String, dynamic>)).toList(),
      page: (json['page'] as num).toInt(),
      size: (json['size'] as num).toInt(),
      total: (json['total'] as num).toInt(),
    );
  }
}
