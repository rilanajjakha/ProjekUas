package com.example.projekuas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekuas.model.PaketWisata
import com.example.projekuas.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaketWisataViewModel : ViewModel() {

    private val _paketWisata = MutableLiveData<List<PaketWisata>>()
    val paketWisata: LiveData<List<PaketWisata>> get() = _paketWisata

    // Fungsi untuk mengambil data dari API
    fun fetchPaketWisata() {
        viewModelScope.launch {
            ApiClient.api.getPaketWisata().enqueue(object : Callback<List<PaketWisata>> {
                override fun onResponse(call: Call<List<PaketWisata>>, response: Response<List<PaketWisata>>) {
                    if (response.isSuccessful) {
                        _paketWisata.value = response.body()  // Update LiveData dengan data yang diterima
                    }
                }

                override fun onFailure(call: Call<List<PaketWisata>>, t: Throwable) {
                    // Tangani error, misalnya dengan menampilkan pesan error
                }
            })
        }
    }
}







//package com.example.projekuas.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.projekuas.model.Local
//import com.example.projekuas.network.ApiClient
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class PaketWisataViewModel : ViewModel() {
//
//    private val _paketWisata = MutableLiveData<List<Local>>()
//    val paketWisata: LiveData<List<Local>> get() = _paketWisata
//
//    // Fungsi untuk mengambil data dari API
//    fun fetchPaketWisata() {
//        viewModelScope.launch {
//            ApiClient.getInstance().getPaketWisata().enqueue(object : Callback<List<Local>> {
//                override fun onResponse(call: Call<List<Local>>, response: Response<List<Local>>) {
//                    if (response.isSuccessful) {
//                        _paketWisata.value = response.body()
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Local>>, t: Throwable) {
//                    // Handle error
//                }
//            })
//        }
//    }
//}
