package com.example.kotkin_team.storage.domain.model

// модель данных
data class StorageProduct(
    val id: Int,
    val name: String,
    val image: Int,
    val parentId: Int,
    val selected: Boolean
)