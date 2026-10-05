import 'package:dio/dio.dart';

class ApiError implements Exception {
  ApiError({required this.code, required this.message, this.status});

  final String code;
  final String message;
  final int? status;

  factory ApiError.fromDio(DioException e) {
    final data = e.response?.data;
    if (data is Map<String, dynamic> && data['code'] != null) {
      return ApiError(
        code: data['code'] as String,
        message: (data['message'] ?? '') as String,
        status: e.response?.statusCode,
      );
    }
    return ApiError(
      code: e.response == null ? 'NETWORK' : 'HTTP_${e.response!.statusCode}',
      message: e.message ?? e.toString(),
      status: e.response?.statusCode,
    );
  }

  @override
  String toString() => 'ApiError($status, $code): $message';
}
