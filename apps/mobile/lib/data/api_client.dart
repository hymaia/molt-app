import 'package:dio/dio.dart';

import 'models/api_error.dart';

class ApiClient {
  ApiClient({Dio? dio})
      : dio = dio ??
            Dio(BaseOptions(
              baseUrl: 'http://localhost:8080/api',
              connectTimeout: const Duration(seconds: 10),
              receiveTimeout: const Duration(seconds: 10),
              headers: {'Accept': 'application/json'},
            ));

  final Dio dio;

  Future<dynamic> get(String path, {Map<String, dynamic>? query}) async {
    try {
      final res = await dio.get(path, queryParameters: query);
      return res.data;
    } on DioException catch (e) {
      throw ApiError.fromDio(e);
    }
  }

  Future<dynamic> post(String path, Object body) async {
    try {
      final res = await dio.post(path, data: body);
      return res.data;
    } on DioException catch (e) {
      throw ApiError.fromDio(e);
    }
  }
}
