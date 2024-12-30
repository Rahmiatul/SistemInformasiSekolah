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
import com.rahmi.sisteminformasisekolah.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etFullname: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSignup: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvLogin: TextView // Tambahkan TextView untuk Log In redirect


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regis)

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etFullname = findViewById(R.id.etFullname)
        etEmail = findViewById(R.id.etEmail)
        btnSignup = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvLogin = findViewById(R.id.tvRegister) // Inisialisasi TextView

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tombol Signup
        btnSignup.setOnClickListener {
            if (validateInput()) {
                prosesRegister()
            }
        }

        // Event klik pada tulisan "Log In"
        tvLogin.setOnClickListener {
            val intent = Intent(this@RegisActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Validasi input sebelum registrasi
    private fun validateInput(): Boolean {
        if (etUsername.text.isNullOrEmpty()) {
            etUsername.error = "Username harus diisi"
            return false
        }
        if (etPassword.text.isNullOrEmpty()) {
            etPassword.error = "Password harus diisi"
            return false
        }
        if (etFullname.text.isNullOrEmpty()) {
            etFullname.error = "Nama lengkap harus diisi"
            return false
        }
        if (etEmail.text.isNullOrEmpty()) {
            etEmail.error = "Email harus diisi"
            return false
        }
        return true
    }

    // METHOD PROSES REGISTRASI
    private fun prosesRegister() {
        progressBar.visibility = View.VISIBLE
        btnSignup.isEnabled = false

        ApiClient.apiService.register(
            etUsername.text.toString(),
            etPassword.text.toString(),
            etFullname.text.toString(),
            etEmail.text.toString()
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                progressBar.visibility = View.GONE
                btnSignup.isEnabled = true

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        Toast.makeText(
                            this@RegisActivity,
                            "Registrasi berhasil! Silakan login.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisActivity,
                            "Registrasi gagal: ${registerResponse?.message ?: "Terjadi kesalahan"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisActivity,
                        "Registrasi gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnSignup.isEnabled = true

                Toast.makeText(
                    this@RegisActivity,
                    "Terjadi kesalahan: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}