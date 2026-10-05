import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../theme/molt_colors.dart';

class Avatar extends StatelessWidget {
  const Avatar({super.key, required this.name, this.url, this.size = 56});

  final String name;
  final String? url;
  final double size;

  @override
  Widget build(BuildContext context) {
    final initials = name
        .split(' ')
        .where((p) => p.isNotEmpty)
        .take(2)
        .map((p) => p[0].toUpperCase())
        .join();
    final fallback = Center(
      child: Text(
        initials,
        style: TextStyle(color: MoltColors.primary70, fontWeight: FontWeight.w700, fontSize: size / 2.8),
      ),
    );

    Widget child = fallback;
    if (url != null && url!.isNotEmpty) {
      child = SvgPicture.network(
        url!,
        width: size,
        height: size,
        fit: BoxFit.cover,
        placeholderBuilder: (_) => fallback,
        errorBuilder: (_, _, _) => fallback,
      );
    }

    return Container(
      width: size,
      height: size,
      decoration: const BoxDecoration(color: MoltColors.primary10, shape: BoxShape.circle),
      clipBehavior: Clip.antiAlias,
      child: child,
    );
  }
}
