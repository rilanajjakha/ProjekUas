package com.example.projekuas.model

import com.google.gson.annotations.SerializedName

data class PaketWisata(
    @SerializedName("_id")
    val _id: String?,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("gambar_url")
    val gambar_url: String,

    @SerializedName("detail")
    val detail: String,
)