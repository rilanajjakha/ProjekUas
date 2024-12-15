package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityAddBinding
import com.example.projekuas.model.PaketWisata
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val deskripsi = binding.etDeskripsi.text.toString()
            val lokasi = binding.etLokasi.text.toString()
            val harga = binding.etHarga.text.toString()
            val gambarUrl = binding.etGambarUrl.text.toString()
            val detail = binding.etdetail.text.toString()

            if (nama.isNotEmpty() && deskripsi.isNotEmpty() && lokasi.isNotEmpty() && harga.isNotEmpty() && gambarUrl.isNotEmpty()) {
                val paketWisata = PaketWisata(
                    _id = null,
                    nama = nama,
                    deskripsi = deskripsi,
                    lokasi = lokasi,
                    harga = harga,
                    gambar_url = gambarUrl,
                    detail = detail
                )
                tambahPaketWisata(paketWisata)
            } else {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tambahPaketWisata(paketWisata: PaketWisata) {
        val call = ApiClient.api.createPaketWisata(paketWisata)

        call.enqueue(object : Callback<PaketWisata> {
            override fun onResponse(call: Call<PaketWisata>, response: Response<PaketWisata>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddActivity, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("ID_PAKET", response.body()?._id)  // Kirim ID baru jika perlu
                    setResult(RESULT_OK, intent)
                    finish()  // Kembali ke AdminActivity setelah data berhasil ditambahkan
                } else {
                    Toast.makeText(this@AddActivity, "Gagal menambahkan data. Status: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaketWisata>, t: Throwable) {
                Toast.makeText(this@AddActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
