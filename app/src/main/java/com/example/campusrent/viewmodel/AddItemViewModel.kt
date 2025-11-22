package com.example.campusrent.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusrent.data.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AddItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ItemRepository()

    private val _uploadStatus = MutableStateFlow<Result<Boolean>?>(null)
    val uploadStatus: StateFlow<Result<Boolean>?> = _uploadStatus.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun addItem(name: String, price: String, description: String, imageUri: Uri?) {
        Log.d("AddItemViewModel", "Starting addItem: $name, URI: $imageUri")
        
        if (name.isBlank() || price.isBlank() || description.isBlank()) {
            _uploadStatus.value = Result.failure(Exception("Please fill all fields"))
            return
        }
        
        val priceDouble = price.toDoubleOrNull()
        if (priceDouble == null) {
             _uploadStatus.value = Result.failure(Exception("Invalid price"))
             return
        }

        viewModelScope.launch {
            _isLoading.value = true
            
            // Convert Uri to File
            val file: File? = if (imageUri != null) {
                withContext(Dispatchers.IO) {
                    val resultFile = uriToFile(imageUri)
                    if (resultFile == null) {
                        Log.e("AddItemViewModel", "Failed to convert URI to File")
                    } else {
                        Log.d("AddItemViewModel", "File created successfully: ${resultFile.absolutePath}, size: ${resultFile.length()}")
                    }
                    resultFile
                }
            } else {
                Log.d("AddItemViewModel", "Image URI is null")
                null
            }

            val success = repository.addItemWithFile(name, priceDouble, description, file)
            if (success) {
                Log.d("AddItemViewModel", "Item added successfully")
                _uploadStatus.value = Result.success(true)
            } else {
                Log.e("AddItemViewModel", "Repository returned failure")
                _uploadStatus.value = Result.failure(Exception("Failed to add item"))
            }
            _isLoading.value = false
        }
    }
    
    private fun uriToFile(uri: Uri): File? {
        return try {
            val context = getApplication<Application>()
            val inputStream = context.contentResolver.openInputStream(uri) 
            
            if (inputStream == null) {
                 Log.e("AddItemViewModel", "ContentResolver returned null InputStream for URI: $uri")
                 return null
            }
            
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            Log.e("AddItemViewModel", "Exception in uriToFile: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    fun resetStatus() {
        _uploadStatus.value = null
    }
}
