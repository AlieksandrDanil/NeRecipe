package ru.netology.nerecipe.dto

data class Category(
    val id: Int,
    val cat: String
)

enum class EnumCategory {
    Eurasian, Asian, Panaasian, Eastern, American, Russian, Mediterranean
}