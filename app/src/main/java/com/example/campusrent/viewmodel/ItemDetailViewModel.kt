package com.example.campusrent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusrent.data.model.Item
import com.example.campusrent.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemDetailViewModel : ViewModel() {
    private val repository = ItemRepository()

    private val _item = MutableStateFlow<Item?>(null)
    val item: StateFlow<Item?> = _item.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            val fetchedItem = repository.getItem(itemId)
            _item.value = fetchedItem
        }
    }
}
