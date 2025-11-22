package com.example.campusrent.data.repository

import android.util.Log
import com.example.campusrent.data.api.RetrofitClient
import com.example.campusrent.data.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ItemRepository {
    private val api = RetrofitClient.apiService

    fun getItems(): Flow<List<Item>> = flow {
        try {
            val response = api.getItems()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                Log.e("ItemRepository", "GetItems Error: ${response.code()} ${response.message()}")
                emit(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    suspend fun getItem(itemId: String): Item? {
        return try {
             val response = api.getItems()
             response.body()?.find { it.id == itemId }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addItemWithFile(name: String, price: Double, description: String, file: File?): Boolean {
        return try {
            val userId = AuthRepository.currentUserId ?: return false

            val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val userPart = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            if (file != null) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            val response = api.addItem(namePart, pricePart, descPart, userPart, imagePart)
            
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("ItemRepository", "AddItem Error: ${response.code()} $errorBody")
                return false
            }

            response.body()?.status == "success"
        } catch (e: Exception) {
            Log.e("ItemRepository", "AddItem Exception: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
