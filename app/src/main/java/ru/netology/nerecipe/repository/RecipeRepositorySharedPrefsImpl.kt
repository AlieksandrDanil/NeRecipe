package ru.netology.nerecipe.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.RecipesFilled

const val DRAFT_POST_ID = 999_999_999_911L

class RecipeRepositorySharedPrefsImpl(
    context: Context,
) : RecipeRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)
    private val dataPost = MutableLiveData(RecipesFilled.empty)

    init {
        prefs.getString(key, null)?.let {
            recipes = gson.fromJson(it, type)
            data.value = recipes
        }
    }

    override fun getAll(): LiveData<List<Recipe>> = data

    override fun getRecipe(): LiveData<Recipe> = dataPost
    override fun getRecipeById(id: Long): Recipe? {
        dataPost.value = recipes.find {
            it.id == id
        }
        return dataPost.value
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == DRAFT_POST_ID) {
            recipes = listOf(
                recipe.copy(
                    id = recipe.id,
                    content = recipe.content,
                    author = "Me",
                    likedByMe = false,
                    name = "now"
                )
            ) + recipes
            data.value = recipes
            sync()
            return
        }

        if (recipe.id == 0L) {
            // remove hardcoded author & published
            recipes = listOf(
                recipe.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    name = "now"
                )
            ) + recipes
            data.value = recipes
            sync()
            return
        }

        recipes = recipes.map {
            if (it.id != recipe.id)
                it
            else
                it.copy(content = recipe.content)
        }
        data.value = recipes
        sync()
    }

    override fun likeById(id: Long) {
        recipes = recipes.map {
            if (it.id != id)
                it
            else
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
        }
        data.value = recipes
        sync()
    }

    override fun shareById(id: Long) {
        recipes = recipes.map {
            if (it.id != id)
                it
            else
                it.copy(shared = it.shared + 1)
        }
        data.value = recipes
        sync()
    }

    override fun removeById(id: Long) {
        recipes = recipes.filter { it.id != id }
        data.value = recipes
        sync()
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(recipes))
            apply()
        }
    }
}

fun getConstDraftPostId(): Long = DRAFT_POST_ID
