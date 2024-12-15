package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityEditBinding
import com.example.projekuas.model.PaketWisata
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var paketWisata: PaketWisata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        val paketId = intent.getStringExtra("ID_PAKET")
        if (paketId != null) {
            getPaketWisataById(paketId)
        }

        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val deskripsi = binding.etDeskripsi.text.toString()
            val lokasi = binding.etLokasi.text.toString()
            val harga = binding.etHarga.text.toString()
            val gambarUrl = binding.etGambarUrl.text.toString()
            val detail = binding.etdetail.text.toString()

            if (paketWisata != null) {
                val updatedPaketWisata = PaketWisata(
                    _id = paketWisata?._id,
                    nama = nama,
                    deskripsi = deskripsi,
                    lokasi = lokasi,
                    harga = harga,
                    gambar_url = gambarUrl,
                    detail = detail
                )
                updatePaketWisata(updatedPaketWisata)
            } else {
                Toast.makeText(this, "ID Paket tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPaketWisataById(id: String) {
        val call = ApiClient.api.getPaketWisataById(id)
        call.enqueue(object : Callback<PaketWisata> {
            override fun onResponse(call: Call<PaketWisata>, response: Response<PaketWisata>) {
                if (response.isSuccessful) {
                    paketWisata = response.body()
                    paketWisata?.let {
                        binding.etNama.setText(it.nama)
                        binding.etDeskripsi.setText(it.deskripsi)
                        binding.etLokasi.setText(it.lokasi)
                        binding.etHarga.setText(it.harga)
                        binding.etGambarUrl.setText(it.gambar_url)
                        binding.etdetail.setText(it.detail)
                    }
                } else {
                    Toast.makeText(this@EditActivity, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaketWisata>, t: Throwable) {
                Toast.makeText(this@EditActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePaketWisata(paketWisata: PaketWisata) {
        val id = paketWisata._id
        if (id.isNullOrEmpty()) {
            Toast.makeText(this@EditActivity, "ID paket wisata tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val call = ApiClient.api.updatePaketWisata(id, paketWisata)
        call.enqueue(object : Callback<PaketWisata> {
            override fun onResponse(call: Call<PaketWisata>, response: Response<PaketWisata>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditActivity, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("ID_PAKET", paketWisata._id)  // Kirim ID untuk mengupdate data di aktivitas sebelumnya
                    setResult(RESULT_OK, intent)
                    finish()  // Kembali ke AdminActivity setelah data berhasil diperbarui
                } else {
                    Toast.makeText(this@EditActivity, "Gagal memperbarui data. Status: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaketWisata>, t: Throwable) {
                Toast.makeText(this@EditActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


