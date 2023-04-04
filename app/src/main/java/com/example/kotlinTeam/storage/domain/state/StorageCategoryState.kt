package com.example.kotlinTeam.storage.domain.state

import com.example.kotlinTeam.storage.domain.model.StorageCategory

data class StorageCategoryState(
    val isLoading: Boolean = false,
    val storageCategory: List<StorageCategory> = emptyList(),
    val error: String = ""
)