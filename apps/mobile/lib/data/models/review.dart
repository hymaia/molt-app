class Review {
  Review({required this.author, required this.rating, required this.comment, required this.createdAt});

  final String author;
  final int rating;
  final String comment;
  final DateTime createdAt;

  factory Review.fromJson(Map<String, dynamic> json) {
    return Review(
      author: json['author'] as String,
      rating: (json['rating'] as num).toInt(),
      comment: json['comment'] as String? ?? '',
      createdAt: DateTime.parse(json['createdAt'] as String),
    );
  }
}
