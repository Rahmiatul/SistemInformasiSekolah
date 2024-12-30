package com.rahmi.sisteminformasisekolah.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.rahmi.sisteminformasisekolah.models.SekolahResponse
import com.rahmi.sisteminformasisekolah.api.ApiClient
import com.rahmi.sisteminformasisekolah.models.DeleteSekolahResponse
import com.rahmi.sisteminformasisekolah.R
import com.rahmi.sisteminformasisekolah.DetailSekolahActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SekolahAdapter(
    private var sekolahList: List<SekolahResponse.ListItems>, // Fixed the name to match the constructor
    private val onDeleteClick: (Int) -> Unit  // Callback for delete, expecting Int ID
) : RecyclerView.Adapter<SekolahAdapter.ViewHolder>() { // Change from SekolahResponse.ViewHolder to SekolahAdapter.ViewHolder

    // Define ViewHolder class inside the adapter
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Inisialisasi widget
        val imgSekolah: ImageView = view.findViewById(R.id.imgSekolah)
        val tvNamaSekolah: TextView = view.findViewById(R.id.tvNamaSekolah)
        val tvNoTelepon: TextView = view.findViewById(R.id.tvNoTelepon)
        val tvAkreditasi: TextView = view.findViewById(R.id.tvAkreditasi)
        val imgDelete: ImageView = view.findViewById(R.id.imgDelete)  // Icon delete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_sekolah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sekolah = sekolahList[position]

        // Set data to views
        Picasso.get().load(sekolah.gambar).into(holder.imgSekolah)
        holder.tvNamaSekolah.text = sekolah.nama_sekolah
        holder.tvNoTelepon.text = "No. Telepon: ${sekolah.no_telepon}"
        holder.tvAkreditasi.text = "Akreditasi: ${sekolah.akreditasi}"

        // Handle delete action
        holder.imgDelete.setOnClickListener {
            // Tampilkan dialog konfirmasi sebelum menghapus
            showDeleteConfirmationDialog(holder.itemView.context, sekolah.id)

        }

        // Klik item list sekolah untuk membuka detail
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailSekolahActivity::class.java).apply {
                putExtra("gambar_sekolah", sekolah.gambar)
                putExtra("nama_sekolah", sekolah.nama_sekolah)
                putExtra("no_telepon", sekolah.no_telepon)
                putExtra("akreditasi", sekolah.akreditasi)
                putExtra("informasi", sekolah.informasi)  // Jika ada alamat
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sekolahList.size // Fixed the reference
    }

    // Fungsi untuk memperbarui data yang ada pada adapter
    fun setData(data: List<SekolahResponse.ListItems>) {
        sekolahList = data  // Update the list
        notifyDataSetChanged() // Notify that the data has changed
    }

    // Fungsi untuk menampilkan dialog konfirmasi sebelum menghapus
    private fun showDeleteConfirmationDialog(context: Context, sekolahId: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin menghapus sekolah ini?")
        builder.setPositiveButton("Ya") { dialog: DialogInterface, id: Int ->
            deleteSekolah(sekolahId)  // Panggil untuk hapus sekolah
        }
        builder.setNegativeButton("Tidak") { dialog: DialogInterface, id: Int ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    // Fungsi untuk menghapus sekolah
    private fun deleteSekolah(sekolahId: Int) {
        ApiClient.apiService.deleteSekolah(sekolahId.toString()).enqueue(object : Callback<DeleteSekolahResponse> {
            override fun onResponse(
                call: Call<DeleteSekolahResponse>,
                response: Response<DeleteSekolahResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    onDeleteClick(sekolahId)  // Panggil callback untuk menghapus sekolah dari list
                }
            }

            override fun onFailure(call: Call<DeleteSekolahResponse>, t: Throwable) {
                // Handle failure (misalnya tampilkan pesan error)
            }
        })
    }
}