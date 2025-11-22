package com.example.campusrent.data.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("description") val description: String = "",
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("user_id") val userId: String = ""
)
