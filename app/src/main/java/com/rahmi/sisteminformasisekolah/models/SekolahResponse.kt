package com.rahmi.sisteminformasisekolah.models

data class SekolahResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<ListItems>
) {
    data class ListItems(
        val id: Int,
        val nama_sekolah: String,
        val no_telepon: String,
        val akreditasi: String,
        val gambar: String,
        val informasi: String
    )
}

