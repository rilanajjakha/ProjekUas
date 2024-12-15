package com.example.projekuas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projekuas.database.LocalRoomDatabase
import com.example.projekuas.database.PaketBookmark
import com.example.projekuas.databinding.ItemPaketWisataBinding
import com.example.projekuas.model.PaketWisata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaketUserAdapter(
    private val paketUserList: List<PaketWisata>,
    private val context: Context
) : RecyclerView.Adapter<PaketUserAdapter.PaketUserViewHolder>() {

    inner class PaketUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.nama)
        val deskripsi: TextView = itemView.findViewById(R.id.deskripsi)
        val lokasi: TextView = itemView.findViewById(R.id.lokasi)
        val harga: TextView = itemView.findViewById(R.id.harga)
        val gambar: ImageView = itemView.findViewById(R.id.gambarUrl)
        val btnBookmark: ImageButton = itemView.findViewById(R.id.btnBookmark)
        // Tidak perlu tombol Edit dan Delete untuk User
        val btnEditItem: ImageButton = itemView.findViewById(R.id.btnEditItem)
        val btnDeleteItem: ImageButton = itemView.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaketUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paket_wisata, parent, false)
        return PaketUserViewHolder(itemView)
    }

    // Menghubungkan data dengan ViewHolder
    override fun onBindViewHolder(holder: PaketUserViewHolder, position: Int) {
        val currentItem = paketUserList[position]

        holder.nama.text = currentItem.nama
        holder.deskripsi.text = currentItem.deskripsi
        holder.lokasi.text = currentItem.lokasi
        holder.harga.text = currentItem.harga

        // Load gambar dengan Glide
        Glide.with(context)
            .load(currentItem.gambar_url)
            .into(holder.gambar)

        // Menangani klik pada item untuk membuka DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("_id", currentItem._id ?: "")
                putExtra("nama", currentItem.nama ?: "Tidak Ada Nama")
                putExtra("deskripsi", currentItem.deskripsi ?: "Tidak Ada Deskripsi")
                putExtra("lokasi", currentItem.lokasi ?: "Tidak Ada Lokasi")
                putExtra("harga", currentItem.harga ?: "Tidak Ada Harga")
                putExtra("detail", currentItem.detail ?: "Tidak Ada Detail")
                putExtra("gambar_url", currentItem.gambar_url ?: "")
            }
            context.startActivity(intent)
        }


        // Menyembunyikan tombol Edit dan Delete untuk user
        holder.btnEditItem.visibility = View.GONE
        holder.btnDeleteItem.visibility = View.GONE

        // Memeriksa apakah film sudah ada di bookmark
        isBookmark(currentItem._id) { isBookmarkk ->
            holder.btnBookmark.setImageResource(
                if (isBookmarkk) R.drawable.baseline_bookmark
                else R.drawable.baseline_bookmark_border_24
            )
        }

        // Menangani klik tombol favorit
        holder.btnBookmark.setOnClickListener {
            toggleBookmark(currentItem) { isBookmarkk ->
                holder.btnBookmark.setImageResource(
                    if (isBookmarkk) R.drawable.baseline_bookmark
                    else R.drawable.baseline_bookmark_border_24
                )
            }
        }
    }

    // Mendapatkan jumlah item dalam RecyclerView
    override fun getItemCount(): Int = paketUserList.size

    // Fungsi untuk memeriksa apakah film sudah ada di favorit
    private fun isBookmark(_id: String?, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
            val isBookmarkk = db?.getPaketById(_id) != null
            withContext(Dispatchers.Main) {
                callback(isBookmarkk)
            }
        }
    }

    // Fungsi untuk menambah atau menghapus film dari favorit
    private fun toggleBookmark(paket: PaketWisata, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
            val paketBookmark = PaketBookmark(
                paketId = paket._id.toString()
            )

            val isBookmarkk = db?.getPaketById(paket._id) != null
            if (isBookmarkk) {
                db?.delete(paket._id)
            } else {
                db?.insert(paketBookmark)
            }
            withContext(Dispatchers.Main) {
                callback(!isBookmarkk)
            }
        }
    }
}







//package com.example.projekuas
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.projekuas.database.LocalRoomDatabase
//import com.example.projekuas.database.PaketBookmark
//import com.example.projekuas.databinding.ItemPaketWisataBinding
//import com.example.projekuas.model.PaketWisata
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class PaketUserAdapter(
//    private val paketUserList: List<PaketWisata>,
//    private val context: Context
//) : RecyclerView.Adapter<PaketUserAdapter.PaketUserViewHolder>() {
//
//    inner class PaketUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nama: TextView = itemView.findViewById(R.id.nama)
//        val gambar: ImageView = itemView.findViewById(R.id.gambarUrl)
//        val btnBookmark: ImageButton = itemView.findViewById(R.id.btnBookmark)
//        // Tidak perlu tombol Edit dan Delete untuk User
//        val btnEditItem: ImageButton = itemView.findViewById(R.id.btnEditItem)
//        val btnDeleteItem: ImageButton = itemView.findViewById(R.id.btnDeleteItem)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaketUserViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_paket_wisata, parent, false)
//        return PaketUserViewHolder(itemView)
//    }
//
//    // Menghubungkan data dengan ViewHolder
//    override fun onBindViewHolder(holder: PaketUserViewHolder, position: Int) {
//        val currentItem = paketUserList[position]
//
//        holder.nama.text = currentItem.nama
//
//        // Load gambar dengan Glide
//        Glide.with(context)
//            .load(currentItem.gambar_url)
//            .into(holder.gambar)
//
//        // Menangani klik pada item untuk membuka DetailActivity
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java).apply {
//                putExtra("_id", currentItem._id ?: "")
//                putExtra("nama", currentItem.nama ?: "Tidak Ada Nama")
//                putExtra("deskripsi", currentItem.deskripsi ?: "Tidak Ada Deskripsi")
//                putExtra("lokasi", currentItem.lokasi ?: "Tidak Ada Lokasi")
//                putExtra("harga", currentItem.harga ?: "Tidak Ada Harga")
//                putExtra("detail", currentItem.detail ?: "Tidak Ada Detail")
//                putExtra("gambar_url", currentItem.gambar_url ?: "")
//            }
//            context.startActivity(intent)
//        }
//
//
//        // Menyembunyikan tombol Edit dan Delete untuk user
//        holder.btnEditItem.visibility = View.GONE
//        holder.btnDeleteItem.visibility = View.GONE
//
//        // Memeriksa apakah film sudah ada di bookmark
//        isBookmark(currentItem._id) { isBookmarkk ->
//            holder.btnBookmark.setImageResource(
//                if (isBookmarkk) R.drawable.baseline_bookmark
//                else R.drawable.baseline_bookmark_border_24
//            )
//        }
//
//        // Menangani klik tombol favorit
//        holder.btnBookmark.setOnClickListener {
//            toggleBookmark(currentItem) { isBookmarkk ->
//                holder.btnBookmark.setImageResource(
//                    if (isBookmarkk) R.drawable.baseline_bookmark
//                    else R.drawable.baseline_bookmark_border_24
//                )
//            }
//        }
//    }
//
//    // Mendapatkan jumlah item dalam RecyclerView
//    override fun getItemCount(): Int = paketUserList.size
//
//    // Fungsi untuk memeriksa apakah film sudah ada di favorit
//    private fun isBookmark(_id: String?, callback: (Boolean) -> Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
//            val isBookmarkk = db?.getPaketById(_id) != null
//            withContext(Dispatchers.Main) {
//                callback(isBookmarkk)
//            }
//        }
//    }
//
//    // Fungsi untuk menambah atau menghapus film dari favorit
//    private fun toggleBookmark(paket: PaketWisata, callback: (Boolean) -> Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
//            val paketBookmark = PaketBookmark(
//                paketId = paket._id.toString()
//            )
//
//            val isBookmarkk = db?.getPaketById(paket._id) != null
//            if (isBookmarkk) {
//                db?.delete(paket._id)
//            } else {
//                db?.insert(paketBookmark)
//            }
//            withContext(Dispatchers.Main) {
//                callback(!isBookmarkk)
//            }
//        }
//    }
//}
