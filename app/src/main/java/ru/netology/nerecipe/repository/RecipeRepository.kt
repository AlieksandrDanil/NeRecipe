package ru.netology.nerecipe.repository

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun save(recipe: Recipe)
    fun removeById(id: Long)
    fun getRecipeById(id: Long): Recipe?
    fun getRecipe(): LiveData<Recipe>
}