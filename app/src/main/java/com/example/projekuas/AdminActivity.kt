package com.example.projekuas

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekuas.databinding.ActivityAdminBinding
import com.example.projekuas.model.PaketWisata
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var paketWisataAdapter: PaketWisataAdapter
    private var paketWisataList = mutableListOf<PaketWisata>()

    // Define ActivityResultLaunchers for Add and Edit Activities
    private lateinit var editActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and set up the adapter
        initializeRecyclerView()

        // Initialize ActivityResultLaunchers
        initializeActivityResultLaunchers()

        // Check if there is an internet connection, then fetch data
        checkInternetAndFetchData()

        // Setup listeners for FAB button (to add a new Paket Wisata)
        setupListeners()
    }

    // Initializes RecyclerView and sets up the adapter
    private fun initializeRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        paketWisataAdapter = PaketWisataAdapter(
            paketWisataList,
            isAdmin = true,
            onEditClick = { paket ->
                paket._id?.let { id ->
                    // Create an intent to pass the ID for editing
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra("ID_PAKET", id)
                    // Launch EditActivity using ActivityResultLauncher
                    editActivityResultLauncher.launch(intent)
                }
            },
            onDeleteClick = { paket ->
                paket._id?.let { id ->
                    deletePaketWisata(id)
                }
            },
            onCardClick = { paket ->
                paket._id?.let { id ->
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("ID_PAKET", id)
                    startActivity(intent)
                }
            }
        )
        binding.recyclerView.adapter = paketWisataAdapter
    }

    // Initializes the ActivityResultLaunchers for Add and Edit Activities
    private fun initializeActivityResultLaunchers() {
        // Launch EditActivity and handle the result
        editActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh data after editing
                fetchPaketWisataFromServer()
            }
        }

        // Launch AddActivity and handle the result
        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh data after adding a new Paket Wisata
                fetchPaketWisataFromServer()
            }
        }
    }

    // Checks if the device is connected to the internet and fetches data
    private fun checkInternetAndFetchData() {
        if (isInternetAvailable()) {
            fetchPaketWisataFromServer()
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetches the list of Paket Wisata from the server using Retrofit
    private fun fetchPaketWisataFromServer() {
        ApiClient.api.getPaketWisata().enqueue(object : Callback<List<PaketWisata>> {
            override fun onResponse(call: Call<List<PaketWisata>>, response: Response<List<PaketWisata>>) {
                if (response.isSuccessful) {
                    val paketWisata = response.body() ?: emptyList()
                    paketWisataList.clear()
                    paketWisataList.addAll(paketWisata)
                    paketWisataAdapter.notifyDataSetChanged() // Notify adapter that data has been updated
                } else {
                    Toast.makeText(this@AdminActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PaketWisata>>, t: Throwable) {
                Toast.makeText(this@AdminActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Deletes the Paket Wisata using Retrofit
    private fun deletePaketWisata(id: String) {
        ApiClient.api.deletePaketWisata(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminActivity, "Paket wisata successfully deleted", Toast.LENGTH_SHORT).show()
                    fetchPaketWisataFromServer() // Refresh data after deletion
                } else {
                    Toast.makeText(this@AdminActivity, "Failed to delete paket wisata", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Sets up listeners, such as clicking on the FAB to add a new Paket Wisata
    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            // Launch AddActivity using ActivityResultLauncher
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        binding.btnLogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Checks if the internet is available on the device
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
