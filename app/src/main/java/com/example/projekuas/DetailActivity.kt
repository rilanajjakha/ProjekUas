package com.example.projekuas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.example.projekuas.databinding.ActivityDetailBinding
import com.example.projekuas.model.PaketWisata
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @UnstableApi
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali untuk menutup aktivitas ini
        binding.btnBack.setOnClickListener {
            finish() // Menggunakan onBackPressed untuk kembali ke aktivitas sebelumnya
        }

        // Mengambil data dari intent
        val id = intent.getStringExtra("_id") ?: "ID Tidak Tersedia"
        val nama = intent.getStringExtra("nama") ?: "Nama Tidak Tersedia"
        val deskripsi = intent.getStringExtra("deskripsi") ?: "Deskripsi Tidak Tersedia"
        val lokasi = intent.getStringExtra("lokasi") ?: "Lokasi Tidak Tersedia"
        val harga = intent.getStringExtra("harga") ?: "Harga Tidak Tersedia"
        val detail = intent.getStringExtra("detail") ?: "Detail Tidak Tersedia"
        val gambarUrl = intent.getStringExtra("gambar_url") ?: ""

        // Log untuk debugging
        Log.d("DetailActivity", "ID: $id, Nama: $nama, Deskripsi: $deskripsi, Lokasi: $lokasi")

        // Validasi data paket wisata
        if (nama == "Nama Tidak Tersedia") {
            Toast.makeText(this, "Data paket wisata tidak lengkap!", Toast.LENGTH_SHORT).show()
        }

        // Menampilkan data pada UI
        binding.namaDetail.text = nama
        binding.hargaDetail.text = harga
        binding.lokasiDetail.text = lokasi
        binding.descDetail.text = deskripsi
        binding.detailDetail.text = detail

        // Jika URL gambar ada, muat gambar menggunakan Glide
        if (gambarUrl.isNotEmpty()) {
            Glide.with(this).load(gambarUrl).into(binding.imageDetail)
        }

        // Mengambil data paket wisata dari API jika ada ID paket
        val paketId = intent.getStringExtra("ID_PAKET")
        if (paketId != null) {
            getPaketWisataDetail(paketId)
        } else {
            Toast.makeText(this, "Paket tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPaketWisataDetail(id: String) {
        val call = ApiClient.api.getPaketWisataById(id)
        call.enqueue(object : Callback<PaketWisata> {
            override fun onResponse(call: Call<PaketWisata>, response: Response<PaketWisata>) {
                if (response.isSuccessful) {
                    val paket = response.body()
                    paket?.let {
                        // Update UI dengan data dari paket
                        binding.namaDetail.text = it.nama
                        binding.hargaDetail.text = "Rp ${it.harga}"
                        binding.lokasiDetail.text = it.lokasi
                        binding.descDetail.text = it.deskripsi
                        binding.detailDetail.text = it.detail
                        Glide.with(this@DetailActivity).load(it.gambar_url).into(binding.imageDetail)
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data paket wisata", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaketWisata>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





