import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../data/api_client.dart';
import '../../data/providers.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../widgets/state_views.dart';

class ProposalForm extends ConsumerStatefulWidget {
  const ProposalForm({super.key, required this.missionId});

  final int missionId;

  @override
  ConsumerState<ProposalForm> createState() => _ProposalFormState();
}

class _ProposalFormState extends ConsumerState<ProposalForm> {
  static const _minMessage = 10;
  static const _maxMessage = 2000;

  final _formKey = GlobalKey<FormState>();
  final _talentIdCtrl = TextEditingController();
  final _rateCtrl = TextEditingController();
  final _messageCtrl = TextEditingController();
  bool _submitting = false;
  String? _apiError;

  @override
  void dispose() {
    _talentIdCtrl.dispose();
    _rateCtrl.dispose();
    _messageCtrl.dispose();
    super.dispose();
  }

  String? _errorMessage(AppLocalizations l10n, Map<String, dynamic> body) {
    switch (body['code']) {
      case 'MISSION_NOT_OPEN':
        return l10n.errorMissionNotOpen;
      case 'DUPLICATE_PROPOSAL':
        return l10n.errorDuplicateProposal;
      case 'NOT_FOUND':
        return l10n.errorNotFound;
    }
    return null;
  }

  Future<void> _submit() async {
    final l10n = AppLocalizations.of(context);
    setState(() => _apiError = null);
    if (!_formKey.currentState!.validate()) return;

    final euros = double.parse(_rateCtrl.text.trim().replaceAll(',', '.'));

    setState(() => _submitting = true);
    try {
      await ApiClient().dio.post('/missions/${widget.missionId}/proposals', data: {
        'talentId': int.parse(_talentIdCtrl.text.trim()),
        'dailyRateCents': (euros * 100).round(),
        'message': _messageCtrl.text.trim(),
      });
      if (!mounted) return;
      _formKey.currentState!.reset();
      _talentIdCtrl.clear();
      _rateCtrl.clear();
      _messageCtrl.clear();
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(l10n.proposalSent)));
      ref.invalidate(missionDetailProvider(widget.missionId));
    } on DioException catch (e) {
      debugPrint('proposal failed: ${e.response?.statusCode} ${e.response?.data}');
      if (!mounted) return;
      final body = e.response?.data;
      final msg = body is Map<String, dynamic> ? _errorMessage(l10n, body) : null;
      if (msg != null) {
        setState(() => _apiError = msg);
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(l10n.errorGeneric)));
      }
    } finally {
      if (mounted) setState(() => _submitting = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    return MoltCard(
      child: Form(
        key: _formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextFormField(
              controller: _talentIdCtrl,
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              decoration: InputDecoration(labelText: l10n.fieldTalentId),
              validator: (v) {
                if (v == null || v.trim().isEmpty) return l10n.errorRequired;
                final id = int.tryParse(v.trim());
                if (id == null || id <= 0) return l10n.errorInvalidTalentId;
                return null;
              },
            ),
            const SizedBox(height: 12),
            TextFormField(
              controller: _rateCtrl,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              inputFormatters: [FilteringTextInputFormatter.allow(RegExp(r'[0-9.,]'))],
              decoration: InputDecoration(labelText: l10n.fieldDailyRate, suffixText: '€'),
              validator: (v) {
                if (v == null || v.trim().isEmpty) return l10n.errorRequired;
                final rate = double.tryParse(v.trim().replaceAll(',', '.'));
                if (rate == null || (rate * 100).round() < 1) return l10n.errorInvalidRate;
                return null;
              },
            ),
            const SizedBox(height: 12),
            TextFormField(
              controller: _messageCtrl,
              minLines: 4,
              maxLines: 8,
              maxLength: _maxMessage,
              decoration: InputDecoration(labelText: l10n.fieldMessage, alignLabelWithHint: true),
              validator: (v) {
                final len = v?.trim().length ?? 0;
                if (len == 0) return l10n.errorRequired;
                if (len < _minMessage || len > _maxMessage) {
                  return l10n.errorMessageLength(_minMessage, _maxMessage);
                }
                return null;
              },
            ),
            if (_apiError != null) ...[
              const SizedBox(height: 8),
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: MoltColors.error.withValues(alpha: 0.08),
                  borderRadius: BorderRadius.circular(MoltColors.radiusS),
                ),
                child: Text(_apiError!, style: const TextStyle(color: MoltColors.error)),
              ),
            ],
            const SizedBox(height: 16),
            FilledButton(
              onPressed: _submitting ? null : _submit,
              child: _submitting
                  ? const SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(strokeWidth: 2, color: MoltColors.neutral0),
                    )
                  : Text(l10n.submitProposal),
            ),
          ],
        ),
      ),
    );
  }
}
