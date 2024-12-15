package com.example.projekuas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projekuas.databinding.ItemPaketWisataBinding
import com.example.projekuas.model.PaketWisata

class PaketWisataAdapter(
    private var paketWisataList: List<PaketWisata>, // Make the list mutable
    private val isAdmin: Boolean,
    private val onEditClick: (PaketWisata) -> Unit,
    private val onDeleteClick: (PaketWisata) -> Unit,
    private val onCardClick: (PaketWisata) -> Unit // Listener untuk klik card
) : RecyclerView.Adapter<PaketWisataAdapter.PaketWisataViewHolder>() {

    inner class PaketWisataViewHolder(val binding: ItemPaketWisataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(paketWisata: PaketWisata) {
            with(binding) {
                // Set data ke tampilan
                nama.text = paketWisata.nama
                deskripsi.text = paketWisata.deskripsi
                lokasi.text = paketWisata.lokasi
                harga.text = paketWisata.harga

                // Load gambar dengan Glide (menambahkan placeholder dan error handling)
                Glide.with(root.context)
                    .load(paketWisata.gambar_url)
                    .into(gambarUrl)

                // Konfigurasi tombol Edit dan Delete
                if (isAdmin) {
                    btnEditItem.visibility = View.VISIBLE
                    btnDeleteItem.visibility = View.VISIBLE
                    btnBookmark.visibility = View.GONE

                    btnEditItem.setOnClickListener { onEditClick(paketWisata) }
                    btnDeleteItem.setOnClickListener { onDeleteClick(paketWisata) }
                } else {
                    btnEditItem.visibility = View.GONE
                    btnDeleteItem.visibility = View.GONE
                    btnBookmark.visibility = View.VISIBLE
                }

                // Konfigurasi klik card untuk membuka DetailActivity
                root.setOnClickListener {
                    onCardClick(paketWisata) // Memanggil listener untuk klik card
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaketWisataViewHolder {
        val binding =
            ItemPaketWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaketWisataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaketWisataViewHolder, position: Int) {
        holder.bind(paketWisataList[position])
    }

    override fun getItemCount(): Int = paketWisataList.size

    // Method untuk memperbarui data di adapter
    fun updateData(newList: List<PaketWisata>) {
        paketWisataList = newList // Update the list with new data
        notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }
}
