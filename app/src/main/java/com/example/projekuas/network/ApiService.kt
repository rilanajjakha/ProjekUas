package com.example.projekuas.network

import com.example.projekuas.model.PaketWisata
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("XE4jA/packages")
    fun getPaketWisata(): Call<List<PaketWisata>>

    @POST("XE4jA/packages")
    fun createPaketWisata(@Body model: PaketWisata): Call<PaketWisata>

    @DELETE("XE4jA/packages/{id}")
    fun deletePaketWisata(@Path("id") id: String): Call<Void>

    @GET("XE4jA/packages/{id}")
    fun getPaketWisataById(@Path("id") id: String): Call<PaketWisata>

    @POST("XE4jA/packages/{id}")
    fun updatePaketWisata(
        @Path("id") id: String,  // ID harus valid saat update
        @Body paketWisata: PaketWisata
    ): Call<PaketWisata>
}