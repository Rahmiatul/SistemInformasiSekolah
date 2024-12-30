package com.rahmi.sisteminformasisekolah.api

import com.rahmi.sisteminformasisekolah.models.SekolahResponse
import com.rahmi.sisteminformasisekolah.models.TambahSekolahResponse
import com.rahmi.sisteminformasisekolah.models.DeleteSekolahResponse
import com.rahmi.sisteminformasisekolah.models.RegisterResponse
import com.rahmi.sisteminformasisekolah.models.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    // Endpoint untuk Register
    @FormUrlEncoded
    @POST("API_SEKOLAH/register.php")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("fullname") fullname: String,
        @Field("email") email: String,
    ): Call<RegisterResponse>

    // Endpoint untuk Login
    @FormUrlEncoded
    @POST("API_SEKOLAH/login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    // Endpoint untuk Mendapatkan Data Sekolah (Semua data atau berdasarkan nama sekolah)
    @GET("API_SEKOLAH/get_sekolah.php")
    fun getListSekolah(@Query("nama_sekolah") namaSekolah: String?): Call<SekolahResponse>

    // Optional: Endpoint untuk Mendapatkan Detail Sekolah Berdasarkan ID
    @GET("API_SEKOLAH/get_sekolah.php")
    fun getDetailSekolah(@Query("id") id: Int): Call<SekolahResponse>

    @Multipart
    @POST("API_SEKOLAH/add_sekolah.php")
    fun addSekolah(
        @Part("namaSekolah") namaSekolah: RequestBody,
        @Part("noTelepon") noTelepon: RequestBody,
        @Part("akreditasi") akreditasi: RequestBody,
        @Part("informasi") informasi: RequestBody,
        @Part fileGambar: MultipartBody.Part
    ): Call<TambahSekolahResponse>

    @FormUrlEncoded
    @POST("API_SEKOLAH/delete_sekolah.php")
    fun deleteSekolah(@Field("id") id: String): Call<DeleteSekolahResponse>  // Change id to String

}


