package com.example.kotkin_team.storage.data.mapper

import com.example.kotkin_team.storage.data.api.model.StorageProductDto
import com.example.kotkin_team.storage.data.db.model.StorageProductEntity
import com.example.kotkin_team.storage.domain.model.StorageProduct

class StorageProductMapper {
    fun map(
        storageProductDto: List<StorageProductDto>,
//        storageProductEntity: List<StorageProductEntity>
    ): List<StorageProduct> {
        return storageProductDto.map {
            StorageProduct(
                id = it.id,
                name = it.name,
                image = it.image,
                parentId = it.parentId,
                selected = false
//                selected = storageProductEntity.contains(
//                    StorageProductEntity(
//                        it.id,
//                        it.name,
//                        it.parentId
//                    )
//                ),
            )
        }
    }

    fun mapToEntity(
        storageProduct: StorageProduct,
    ): StorageProductEntity {
        return StorageProductEntity(
            storageProduct.id,
            storageProduct.name,
            storageProduct.parentId
        )
    }
}