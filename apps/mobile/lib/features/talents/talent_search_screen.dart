import 'dart:async';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../../data/models/talent.dart';
import '../../l10n/app_localizations.dart';
import '../../theme/molt_colors.dart';
import '../../widgets/molt_app_bar.dart';
import '../../widgets/state_views.dart';
import '../../widgets/talent_card.dart';

class TalentSearchScreen extends StatefulWidget {
  const TalentSearchScreen({super.key});

  @override
  State<TalentSearchScreen> createState() => _TalentSearchScreenState();
}

class _TalentSearchScreenState extends State<TalentSearchScreen> {
  final Dio _dio = Dio();
  final TextEditingController _qController = TextEditingController();
  final TextEditingController _skillController = TextEditingController();
  Timer? _debounce;

  String _q = '';
  String _kind = 'ALL';
  String _skill = '';
  bool _availableOnly = false;
  String _sort = 'relevance';

  int _page = 0;
  int _size = 12;
  int _total = 0;
  List<Talent> _items = [];
  bool _loading = false;
  bool _loadingMore = false;
  bool _error = false;
  bool _hasMore = false;
  bool _firstLoad = true;
  int _requestId = 0;

  @override
  void initState() {
    super.initState();
    _search();
  }

  @override
  void dispose() {
    _debounce?.cancel();
    _qController.dispose();
    _skillController.dispose();
    super.dispose();
  }

  Future<void> _search({bool more = false}) async {
    final id = ++_requestId;
    setState(() {
      if (more) {
        _loadingMore = true;
      } else {
        _loading = true;
        _page = 0;
      }
      _error = false;
    });

    final query = <String, dynamic>{};
    if (_q.trim().isNotEmpty) {
      query['q'] = _q.trim();
    }
    if (_kind != 'ALL') {
      query['kind'] = _kind;
    }
    if (_skill.trim().isNotEmpty) {
      query['skill'] = _skill.trim();
    }
    if (_availableOnly) {
      query['available'] = 'true';
    }
    query['sort'] = _sort;
    query['page'] = more ? (_page + 1).toString() : '0';
    query['size'] = _size.toString();

    try {
      final res = await _dio.get('http://localhost:8080/api/talents', queryParameters: query);
      if (id != _requestId || !mounted) return;
      final data = res.data as Map<String, dynamic>;
      final list = (data['items'] as List).map((e) => Talent.fromJson(e as Map<String, dynamic>)).toList();
      setState(() {
        if (more) {
          _items = [..._items, ...list];
          _page = _page + 1;
        } else {
          _items = list;
          _page = 0;
        }
        _total = (data['total'] as num).toInt();
        _size = (data['size'] as num?)?.toInt() ?? _size;
        _hasMore = _items.length < _total;
        _loading = false;
        _loadingMore = false;
        if (_firstLoad) {
          setState(() {
            _firstLoad = false;
          });
        }
      });
    } catch (e) {
      if (id != _requestId || !mounted) return;
      setState(() {
        _loading = false;
        _loadingMore = false;
        if (!more) {
          _items = [];
          _total = 0;
          _hasMore = false;
          _error = true;
        } else {
          setState(() {
            _error = true;
          });
        }
      });
    }
  }

  void _onQueryChanged(String value) {
    _debounce?.cancel();
    _debounce = Timer(const Duration(milliseconds: 350), () {
      setState(() {
        _q = value;
      });
      _search();
    });
  }

  void _onSkillChanged(String value) {
    _debounce?.cancel();
    _debounce = Timer(const Duration(milliseconds: 350), () {
      setState(() {
        _skill = value;
      });
      _search();
    });
  }

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context);
    return Scaffold(
      appBar: moltAppBar(),
      body: RefreshIndicator(
        color: MoltColors.primary,
        onRefresh: () => _search(),
        child: CustomScrollView(
          slivers: [
            SliverToBoxAdapter(
              child: Container(
                color: MoltColors.neutral0,
                padding: const EdgeInsets.fromLTRB(16, 8, 16, 16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(l10n.searchTitle, style: Theme.of(context).textTheme.titleLarge),
                    const SizedBox(height: 12),
                    TextField(
                      controller: _qController,
                      onChanged: _onQueryChanged,
                      textInputAction: TextInputAction.search,
                      onSubmitted: (v) {
                        _debounce?.cancel();
                        setState(() {
                          _q = v;
                        });
                        _search();
                      },
                      decoration: InputDecoration(
                        hintText: l10n.searchHint,
                        prefixIcon: const Icon(Icons.search),
                        suffixIcon: _qController.text.isEmpty
                            ? null
                            : IconButton(
                                icon: const Icon(Icons.close),
                                onPressed: () {
                                  _qController.clear();
                                  setState(() {
                                    _q = '';
                                  });
                                  _search();
                                },
                              ),
                      ),
                    ),
                    const SizedBox(height: 12),
                    SizedBox(
                      width: double.infinity,
                      child: SegmentedButton<String>(
                        showSelectedIcon: false,
                        segments: [
                          ButtonSegment(value: 'ALL', label: Text(l10n.filterKindAll)),
                          ButtonSegment(value: 'HUMAN', label: Text(l10n.kindHuman)),
                          ButtonSegment(value: 'AGENT', label: Text(l10n.kindAgent)),
                          ButtonSegment(value: 'HYBRID', label: Text(l10n.kindHybrid)),
                        ],
                        selected: {_kind},
                        onSelectionChanged: (s) {
                          setState(() {
                            _kind = s.first;
                          });
                          _search();
                        },
                      ),
                    ),
                    const SizedBox(height: 12),
                    TextField(
                      controller: _skillController,
                      onChanged: _onSkillChanged,
                      decoration: InputDecoration(
                        hintText: l10n.filterSkillHint,
                        prefixIcon: const Icon(Icons.sell_outlined),
                        isDense: true,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Switch(
                          value: _availableOnly,
                          onChanged: (v) {
                            setState(() {
                              _availableOnly = v;
                            });
                            _search();
                          },
                        ),
                        const SizedBox(width: 6),
                        Expanded(child: Text(l10n.filterAvailableOnly)),
                        Text('${l10n.sortLabel} ', style: const TextStyle(color: MoltColors.muted)),
                        DropdownButton<String>(
                          value: _sort,
                          underline: const SizedBox.shrink(),
                          borderRadius: BorderRadius.circular(MoltColors.radiusS),
                          items: [
                            DropdownMenuItem(value: 'relevance', child: Text(l10n.sortRelevance)),
                            DropdownMenuItem(value: 'rating', child: Text(l10n.sortRating)),
                            DropdownMenuItem(value: 'missions', child: Text(l10n.sortMissions)),
                          ],
                          onChanged: (v) {
                            if (v == null) return;
                            setState(() {
                              _sort = v;
                            });
                            _search();
                          },
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            if (_loading && (_firstLoad || _items.isEmpty))
              const SliverFillRemaining(hasScrollBody: false, child: LoadingView())
            else if (_error && _items.isEmpty)
              SliverFillRemaining(
                hasScrollBody: false,
                child: ErrorView(onRetry: () => _search()),
              )
            else if (_items.isEmpty)
              SliverFillRemaining(
                hasScrollBody: false,
                child: EmptyView(message: l10n.emptyTalents),
              )
            else ...[
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                  child: Row(
                    children: [
                      Text(
                        l10n.talentCount(_total),
                        style: const TextStyle(fontWeight: FontWeight.w700, color: MoltColors.text),
                      ),
                      if (_loading) ...[
                        const SizedBox(width: 8),
                        const SizedBox(
                          width: 14,
                          height: 14,
                          child: CircularProgressIndicator(strokeWidth: 2, color: MoltColors.primary),
                        ),
                      ],
                    ],
                  ),
                ),
              ),
              SliverPadding(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                sliver: SliverList.separated(
                  itemCount: _items.length,
                  separatorBuilder: (_, _) => const SizedBox(height: 12),
                  itemBuilder: (context, i) {
                    final t = _items[i];
                    return TalentCard(
                      talent: t,
                      onTap: () => context.go('/talents/${t.id}'),
                    );
                  },
                ),
              ),
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.all(24),
                  child: Center(
                    child: _loadingMore
                        ? const CircularProgressIndicator(color: MoltColors.primary)
                        : _hasMore
                            ? OutlinedButton(
                                onPressed: () => _search(more: true),
                                child: Text(l10n.loadMore),
                              )
                            : _error
                                ? TextButton(
                                    onPressed: () => _search(more: true),
                                    child: Text(l10n.retry),
                                  )
                                : const SizedBox.shrink(),
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
