package ru.netology.nerecipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.RecipesFilled
import ru.netology.nerecipe.repository.*

private val empty = RecipesFilled.empty

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        AppDb.getInstance(context =  application).recipeDao()
    )
    val data = repository.getAll()
    val dataRecipe = repository.getRecipe()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        toEmpty()
    }

    fun toEmpty() {
        edited.value = empty
    }

    fun edit(recipe: Recipe) {
        edited.value = recipe
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun getRecipeById(id: Long) = repository.getRecipeById(id)
}


class DraftContentRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository = RecipeRepositorySharedPrefsImpl(application)

    fun save(content: String) {
        val draftRecipe = Recipe(
            id = getConstDraftPostId(),
            pos = 0,
            author = "",
            name = "",
            category = "",
            content = content,
            likedByMe = false,
        )
        repository.save(draftRecipe)
    }

    fun getRecipeById(id: Long) = repository.getRecipeById(id)
}