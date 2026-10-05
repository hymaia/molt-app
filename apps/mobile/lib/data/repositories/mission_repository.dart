import '../api_client.dart';
import '../models/mission.dart';
import '../models/proposal.dart';

class MissionRepository {
  MissionRepository(this._api);

  final ApiClient _api;

  Future<List<Mission>> list({MissionStatus? status}) async {
    final data = await _api.get('/missions', query: {
      if (status != null) 'status': status.toJson(),
    });
    return (data as List).map((e) => Mission.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<Map<String, dynamic>> getMission(int id) async {
    final data = await _api.get('/missions/$id');
    return data as Map<String, dynamic>;
  }

  Future<Proposal> createProposal(int missionId, ProposalRequest request) async {
    final data = await _api.post('/missions/$missionId/proposals', request.toJson());
    return Proposal.fromJson(data as Map<String, dynamic>);
  }
}
