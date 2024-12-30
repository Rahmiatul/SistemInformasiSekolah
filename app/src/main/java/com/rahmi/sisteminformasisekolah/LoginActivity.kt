package com.rahmi.sisteminformasisekolah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahmi.sisteminformasisekolah.api.ApiClient
import com.rahmi.sisteminformasisekolah.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvRegister = findViewById(R.id.tvRegister) // Tambahan untuk TextView Register

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tombol Login
        btnLogin.setOnClickListener {
            if (validateInput()) {
                prosesLogin()
            }
        }

        // Tombol Register (klik TextView untuk ke halaman RegisActivity)
        tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisActivity::class.java)
            startActivity(intent)
        }
    }

    // Validasi input username dan password
    private fun validateInput(): Boolean {
        if (etUsername.text.isNullOrEmpty()) {
            etUsername.error = "Username harus diisi"
            return false
        }
        if (etPassword.text.isNullOrEmpty()) {
            etPassword.error = "Password harus diisi"
            return false
        }
        return true
    }

    // Method Proses Login
    private fun prosesLogin() {
        // Tampilkan ProgressBar
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false // Nonaktifkan tombol untuk mencegah double klik

        // Panggil API untuk login
        ApiClient.apiService.login(
            etUsername.text.toString(),
            etPassword.text.toString()
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                // Sembunyikan ProgressBar
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {
                        // Jika login berhasil
                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil! Selamat datang, ${loginResponse.username ?: "Pengguna"}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Arahkan ke halaman DashboardActivity
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Tampilkan pesan error dari server
                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal: ${loginResponse?.message ?: "Cek username dan password"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Respons gagal
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Sembunyikan ProgressBar
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true

                // Tampilkan pesan kesalahan
                Toast.makeText(
                    this@LoginActivity,
                    "Terjadi kesalahan: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}