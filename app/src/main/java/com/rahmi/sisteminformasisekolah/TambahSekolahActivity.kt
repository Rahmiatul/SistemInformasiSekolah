package com.rahmi.sisteminformasisekolah

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahmi.sisteminformasisekolah.api.ApiClient
import com.rahmi.sisteminformasisekolah.models.TambahSekolahResponse
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TambahSekolahActivity : AppCompatActivity() {
    private lateinit var etNamaSekolah: EditText
    private lateinit var etNoTelepon: EditText
    private lateinit var etAkreditasi: EditText
    private lateinit var etInformasi: EditText
    private lateinit var btnPilihGambar: Button
    private lateinit var btnTambahSekolah: Button
    private lateinit var imgGambar: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_sekolah)

        // Initialize Views
        etNamaSekolah = findViewById(R.id.etNamaSekolah)
        etNoTelepon = findViewById(R.id.etNoTelepon)
        etAkreditasi = findViewById(R.id.etAkreditasi)
        etInformasi = findViewById(R.id.etInformasi)
        btnPilihGambar = findViewById(R.id.btnPilihGambar)
        btnTambahSekolah = findViewById(R.id.btnTambahSekolah)
        imgGambar = findViewById(R.id.imgGambar)
        progressBar = findViewById(R.id.progressBar)

        // Set the image picker for the button
        btnPilihGambar.setOnClickListener {
            ImagePicker.with(this)
                .crop()  // Crop the image
                .compress(1024)  // Compress the image to 1MB max
                .maxResultSize(1080, 1080)  // Set the max resolution
                .start()
        }

        // Handle the add school action
        btnTambahSekolah.setOnClickListener {
            val namaSekolah = etNamaSekolah.text.toString()
            val noTelepon = etNoTelepon.text.toString()
            val akreditasi = etAkreditasi.text.toString()
            val informasiSekolah = etInformasi.text.toString()

            // Ensure all fields are filled out and image is selected
            if (namaSekolah.isNotEmpty() && noTelepon.isNotEmpty() && akreditasi.isNotEmpty() && informasiSekolah.isNotEmpty() && imageFile != null) {
                tambahSekolah(namaSekolah, noTelepon, akreditasi, informasiSekolah, imageFile!!)
            } else {
                Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle the image picking result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            imageFile = File(uri.path!!)
            imgGambar.visibility = View.VISIBLE
            imgGambar.setImageURI(uri)
        }
    }

    // Function to upload the new school data
    private fun tambahSekolah(namaSekolah: String, noTelepon: String, akreditasi: String, informasi: String, fileGambar: File) {
        progressBar.visibility = View.VISIBLE

        val requestBody = fileGambar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partFileGambar = MultipartBody.Part.createFormData("fileGambar", fileGambar.name, requestBody)
        val nama = namaSekolah.toRequestBody("text/plain".toMediaTypeOrNull())
        val telepon = noTelepon.toRequestBody("text/plain".toMediaTypeOrNull())
        val akreditasiRequest = akreditasi.toRequestBody("text/plain".toMediaTypeOrNull())
        val informasi = informasi.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.apiService.addSekolah(nama, telepon, akreditasiRequest, informasi, partFileGambar)
            .enqueue(object : Callback<TambahSekolahResponse> {
                override fun onResponse(call: Call<TambahSekolahResponse>, response: Response<TambahSekolahResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success) {
                            // School added successfully, navigate back to the dashboard
                            startActivity(Intent(this@TambahSekolahActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@TambahSekolahActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@TambahSekolahActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<TambahSekolahResponse>, t: Throwable) {
                    Toast.makeText(this@TambahSekolahActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            })
    }
}
