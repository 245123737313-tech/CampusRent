package com.example.campusrent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusrent.data.model.Item
import com.example.campusrent.data.repository.AuthRepository
import com.example.campusrent.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val itemRepository = ItemRepository()
    private val authRepository = AuthRepository()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    init {
        loadItems()
    }

    // Changed to public so it can be called from UI
    fun loadItems() {
        viewModelScope.launch {
            itemRepository.getItems().collect { itemList ->
                _items.value = itemList
            }
        }
    }
    
    fun logout() {
        authRepository.logout()
    }
}
