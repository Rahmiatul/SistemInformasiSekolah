package com.rahmi.sisteminformasisekolah

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rahmi.sisteminformasisekolah.adapter.SekolahAdapter
import com.rahmi.sisteminformasisekolah.models.SekolahResponse
import com.rahmi.sisteminformasisekolah.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var addSchoolButton: FloatingActionButton
    private lateinit var sekolahAdapter: SekolahAdapter
    private lateinit var notFoundImage: ImageView

    private val addSekolahResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fetchSchools("") // Refresh list setelah menambah sekolah baru
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Inisialisasi Views
        searchView = findViewById(R.id.svNamaSekolah)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rvSekolah)
        addSchoolButton = findViewById(R.id.floatBtnTambahSekolah)
        notFoundImage = findViewById(R.id.imgNotFound)

        // Inisialisasi adapter
        sekolahAdapter = SekolahAdapter(ArrayList(), ::onDeleteSekolah)
        recyclerView.adapter = sekolahAdapter

        // Fetch data sekolah
        fetchSchools("")

        // Listener untuk pencarian
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                fetchSchools(query.orEmpty())
                return true
            }
        })

        // Listener untuk tombol tambah sekolah
        addSchoolButton.setOnClickListener {
            val intent = Intent(this, TambahSekolahActivity::class.java)
            addSekolahResult.launch(intent)
        }
    }

    private fun fetchSchools(searchQuery: String) {
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getListSekolah(searchQuery).enqueue(object : Callback<SekolahResponse> {
            override fun onResponse(call: Call<SekolahResponse>, response: Response<SekolahResponse>) {
                if (response.isSuccessful) {
                    val sekolahData = response.body()?.data.orEmpty()
                    if (sekolahData.isNotEmpty()) {
                        sekolahAdapter.setData(sekolahData) // Update adapter
                        notFoundImage.visibility = View.GONE
                    } else {
                        sekolahAdapter.setData(emptyList()) // Kosongkan data jika tidak ditemukan
                        notFoundImage.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Gagal mendapatkan data sekolah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<SekolahResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun onDeleteSekolah(sekolahId: Int) {
        // Refresh data sekolah setelah dihapus
        fetchSchools("")
    }
}
