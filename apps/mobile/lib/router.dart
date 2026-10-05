import 'package:go_router/go_router.dart';

import 'features/missions/mission_detail_screen.dart';
import 'features/missions/missions_screen.dart';
import 'features/talents/talent_profile_screen.dart';
import 'features/talents/talent_search_screen.dart';
import 'widgets/home_shell.dart';

final router = GoRouter(
  initialLocation: '/talents',
  routes: [
    StatefulShellRoute.indexedStack(
      builder: (context, state, shell) => HomeShell(navigationShell: shell),
      branches: [
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: '/talents',
              builder: (context, state) => const TalentSearchScreen(),
              routes: [
                GoRoute(
                  path: ':id',
                  builder: (context, state) =>
                      TalentProfileScreen(talentId: int.parse(state.pathParameters['id']!)),
                ),
              ],
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: '/missions',
              builder: (context, state) => const MissionsScreen(),
              routes: [
                GoRoute(
                  path: ':id',
                  builder: (context, state) =>
                      MissionDetailScreen(missionId: int.parse(state.pathParameters['id']!)),
                ),
              ],
            ),
          ],
        ),
      ],
    ),
  ],
);
