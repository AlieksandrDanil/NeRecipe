package ru.netology.nerecipe.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nerecipe.dao.RecipeDao
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.entity.RecipeEntity
import ru.netology.nerecipe.dto.RecipesFilled

class RecipeRepositoryImpl(
    private val dao: RecipeDao,
) : RecipeRepository {
    private var recipes = emptyList<Recipe>() //RecipesFilled.recipesFilled.reversed()
    private val data = MutableLiveData(recipes)
    private val dataRecipe = MutableLiveData(RecipesFilled.empty)

    init {
        // для первоначальной записи первых постов
        //for(recipe in recipes) { dao.save(RecipeEntity.fromDto(recipe)) }

        dao.getInitAll().also { list ->
            list.map {
                recipes = listOf(it.toDto()) + recipes
            }
        }
        data.value = recipes
    }

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    override fun getRecipe(): LiveData<Recipe> = dataRecipe
    override fun getRecipeById(id: Long): Recipe? {
        dataRecipe.value = dao.getById(id).toDto()
        return dataRecipe.value
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        getRecipeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        getRecipeById(id)
    }

    override fun save(recipe: Recipe) {
        dao.save(RecipeEntity.fromDto(recipe))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}