package ru.netology.nerecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nerecipe.dao.RecipeDao
import ru.netology.nerecipe.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app8.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}