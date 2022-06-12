package com.bangkit.alitomate.network.retrofit

import com.bangkit.alitomate.network.response.ReportResponse
import com.bangkit.alitomate.network.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ApiService {
    @FormUrlEncoded
    @POST("report")
    fun report(
        @Field("nama") nama: String,
        @Field("nomor_telepon") nomorTelepon: String,
        @Field("detail_lokasi") detailLokasi: String,
        @Field("keterangan_kebakaran") keteranganKebakaran: String,
        @Field("location") location: String,
    ) : Call<ReportResponse>

    @Multipart
    @POST("upload")
    fun upload(
        @Part file: MultipartBody.Part,
    ) : Call<UploadResponse>
}