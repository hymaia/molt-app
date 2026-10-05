import '../api_client.dart';
import '../models/talent.dart';
import '../models/talent_kind.dart';

class TalentRepository {
  TalentRepository(this._api);

  final ApiClient _api;

  Future<TalentPage> search({
    String? q,
    TalentKind? kind,
    String? skill,
    bool? available,
    String sort = 'relevance',
    int page = 0,
    int size = 12,
  }) async {
    final data = await _api.get('/talents', query: {
      if (q != null && q.isNotEmpty) 'q': q,
      if (kind != null) 'kind': kind.toJson(),
      if (skill != null && skill.isNotEmpty) 'skill': skill,
      if (available == true) 'available': true,
      'sort': sort,
      'page': page,
      'size': size,
    });
    return TalentPage.fromJson(data as Map<String, dynamic>);
  }

  Future<TalentDetail> getTalent(int id) async {
    final data = await _api.get('/talents/$id');
    return TalentDetail.fromJson(data as Map<String, dynamic>);
  }
}
