import 'package:flutter/material.dart';

import '../theme/molt_colors.dart';

class RatingStars extends StatelessWidget {
  const RatingStars({super.key, required this.rating, this.size = 16, this.showValue = true});

  final double rating;
  final double size;
  final bool showValue;

  @override
  Widget build(BuildContext context) {
    final stars = <Widget>[];
    for (var i = 1; i <= 5; i++) {
      IconData icon;
      if (rating >= i) {
        icon = Icons.star_rounded;
      } else if (rating >= i - 0.5) {
        icon = Icons.star_half_rounded;
      } else {
        icon = Icons.star_outline_rounded;
      }
      stars.add(Icon(icon, size: size, color: MoltColors.warning));
    }
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        ...stars,
        if (showValue) ...[
          const SizedBox(width: 4),
          Text(
            rating.toStringAsFixed(1),
            style: TextStyle(fontSize: size * 0.85, fontWeight: FontWeight.w700, color: MoltColors.text),
          ),
        ],
      ],
    );
  }
}
