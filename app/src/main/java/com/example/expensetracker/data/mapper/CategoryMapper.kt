package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.category.CategoryEntity
import com.example.expensetracker.data.local.entity.category.SubcategoryEntity
import com.example.expensetracker.domain.model.category.Category
import com.example.expensetracker.domain.model.category.Subcategory
import com.example.expensetracker.domain.model.transaction.TransactionType

fun CategoryEntity.toDomain(subcategories: List<SubcategoryEntity> = emptyList()): Category = Category(
    id = id,
    name = name,
    type = TransactionType.fromString(type),
    iconName = iconName,
    colorHex = colorHex,
    orderIndex = orderIndex,
    isDefault = isDefault,
    subcategories = subcategories.map { it.toDomain() }
)

fun SubcategoryEntity.toDomain(): Subcategory = Subcategory(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = iconName
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    type = type.name,
    iconName = iconName,
    colorHex = colorHex,
    orderIndex = orderIndex,
    isDefault = isDefault
)

fun Subcategory.toEntity(): SubcategoryEntity = SubcategoryEntity(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = iconName
)
