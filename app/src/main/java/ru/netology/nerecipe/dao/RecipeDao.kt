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

    @Query("UPDATE RecipeEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0L) insert(recipe) else updateContentById(recipe.id, recipe.content)

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