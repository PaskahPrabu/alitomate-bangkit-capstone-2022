package com.bangkit.alitomate.network.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(

	@field:SerializedName("userreport")
	val userreport: Userreport
)

data class Userreport(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("nomor_telepon")
	val nomorTelepon: String,

	@field:SerializedName("keterangan_kebakaran")
	val keteranganKebakaran: String? = null,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("detail_lokasi")
	val detailLokasi: String? = null
)
