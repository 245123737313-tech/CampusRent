package com.example.campusrent.data.repository

import com.example.campusrent.data.api.RetrofitClient

class AuthRepository {
    private val api = RetrofitClient.apiService
    
    // Simple in-memory storage for current user ID (in a real app, use DataStore or SharedPreferences)
    companion object {
        var currentUserId: Int? = null
    }

    fun isUserLoggedIn(): Boolean {
        return currentUserId != null
    }

    suspend fun login(email: String, pass: String): Result<Unit> {
        return try {
            val response = api.login(email, pass)
            if (response.isSuccessful && response.body()?.status == "success") {
                currentUserId = response.body()?.user_id
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, pass: String): Result<Unit> {
        return try {
            val response = api.signup(email, pass)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Signup failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun logout() {
        currentUserId = null
    }
}
