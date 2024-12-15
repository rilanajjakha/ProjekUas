package com.example.projekuas.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_table")
data class Local(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "nama")
    val nama: String,

    @ColumnInfo(name = "deskripsi")
    val deskripsi: String,

    @ColumnInfo(name = "lokasi")
    val lokasi: String,

    @ColumnInfo(name = "harga")
    val harga: String,

    @ColumnInfo(name = "gambar_url")
    val gambar_url: String,

    @ColumnInfo(name = "detail")
    val detail: String?,
)