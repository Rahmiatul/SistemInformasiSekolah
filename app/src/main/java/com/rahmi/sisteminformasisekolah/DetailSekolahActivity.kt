package com.rahmi.sisteminformasisekolah

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetailSekolahActivity : AppCompatActivity() {
    private lateinit var imgSekolah: ImageView
    private lateinit var tvNamaSekolah: TextView
    private lateinit var tvNoTelepon: TextView
    private lateinit var tvAkreditasi: TextView
    private lateinit var tvInformasiSekolah: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_sekolah)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        imgSekolah = findViewById(R.id.imgSekolah)
        tvNamaSekolah = findViewById(R.id.tvNamaSekolah)
        tvNoTelepon = findViewById(R.id.tvNoTelepon)
        tvAkreditasi = findViewById(R.id.tvAkreditasi)
        tvInformasiSekolah = findViewById(R.id.tvInformasiSekolah)

        // Get data from Intent
        val gambarSekolah = intent.getStringExtra("gambar")
        val namaSekolah = intent.getStringExtra("nama_sekolah")
        val noTelepon = intent.getStringExtra("no_telepon")
        val akreditasi = intent.getStringExtra("akreditasi")
        val informasiSekolah = intent.getStringExtra("informasi")

        // Set data to UI
        if (!gambarSekolah.isNullOrEmpty()) {
            Picasso.get().load(gambarSekolah).into(imgSekolah)
        }
        tvNamaSekolah.text = namaSekolah ?: "Nama Sekolah tidak tersedia"
        tvNoTelepon.text = "No. Telepon: $noTelepon"
        tvAkreditasi.text = "Akreditasi: $akreditasi"
        tvInformasiSekolah.text = informasiSekolah ?: "Informasi sekolah tidak tersedia"
    }
}