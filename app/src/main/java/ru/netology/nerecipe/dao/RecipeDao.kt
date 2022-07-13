package ru.netology.nerecipe.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nerecipe.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeEntity ORDER BY id DESC")
    fun getInitAll(): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM RecipeEntity WHERE id = :id")
    fun getById(id: Long): RecipeEntity

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query("UPDATE RecipeEntity SET author = :author, name = :name, category = :category, content = :content WHERE id = :id")
    fun updateById(id: Long, author: String, name: String, category: String, content: String)

//    @Query("UPDATE RecipeEntity SET name = :name WHERE id = :id")
//    fun updateNameById(id: Long, name: String)
//
//    @Query("UPDATE RecipeEntity SET category = :category WHERE id = :id")
//    fun updateCategoryById(id: Long, category: String)
//
//    @Query("UPDATE RecipeEntity SET content = :content WHERE id = :id")
//    fun updateContentById(id: Long, content: String)

    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0L) insert(recipe) else {
            updateById(recipe.id, recipe.author, recipe.name, recipe.category, recipe.content)
//            updateNameById(recipe.id, recipe.name)
//            updateCategoryById(recipe.id, recipe.category)
//            updateContentById(recipe.id, recipe.content)
        }

    @Query(
        """
        UPDATE RecipeEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query(
        """
           UPDATE RecipeEntity SET
               shared = shared + 1
           WHERE id = :id;
        """
    )
    fun shareById(id: Long)

    @Query("DELETE FROM RecipeEntity WHERE id = :id")
    fun removeById(id: Long)
}