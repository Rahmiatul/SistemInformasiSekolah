package com.rahmi.sisteminformasisekolah.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Ganti BASE_URL sesuai dengan server Anda
    private const val BASE_URL = "http://192.168.76.234/"// Pastikan menggunakan trailing slash "/"

    // Fungsi untuk membuat OkHttpClient dengan logging interceptor
    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Log semua request dan response

        return OkHttpClient.Builder()
            .addInterceptor(logging) // Tambahkan logging interceptor
            .build()
    }

    // Retrofit instance untuk API sekolah
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Tentukan base URL server
            .client(provideOkHttpClient()) // Gunakan OkHttpClient yang sudah dikonfigurasi
            .addConverterFactory(GsonConverterFactory.create()) // Gunakan Gson untuk parsing JSON
            .build()
    }

    // Properti ApiService untuk API Sekolah
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java) // Buat instance ApiService
    }
}



