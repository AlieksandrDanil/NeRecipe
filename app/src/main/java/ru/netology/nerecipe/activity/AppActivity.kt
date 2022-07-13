package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.authorArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.nameArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.catArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nerecipe.databinding.FragmentFeedBinding

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_feedFragment_to_newRecipeFragment,
                Bundle().apply {
                    authorArg = "Author"
                    nameArg = "Name"
                    catArg = "Category"
                    textArg = text
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            val binding = FragmentFeedBinding.bind(findViewById(R.id.clFragmentFeed))
            when (item.itemId) {
                R.id.search -> {
                    clicked = !clicked
                    if (clicked) {
                        binding.topAppBar.visibility = 0
                        binding.groupSearch.visibility = 0
                        binding.groupCategory.visibility = 8
                    } else {
                        binding.topAppBar.visibility = 8
                        binding.groupSearch.visibility = 8
                        binding.groupCategory.visibility = 8
                    }
                }
                R.id.filter -> {
                    clicked = !clicked
                    if (clicked) {
                        //binding.recipeCategory.adapter = categoryAdapter
                        binding.topAppBar.visibility = 0
                        binding.groupSearch.visibility = 8
                        binding.groupCategory.visibility = 0
                    } else {
                        binding.topAppBar.visibility = 8
                        binding.groupSearch.visibility = 8
                        binding.groupCategory.visibility = 8
                    }
                }
            }

        } catch (e: Exception) {
            val view: View? = this.currentFocus?.rootView
            if (view != null) {
                Snackbar.make(
                    view, R.string.error_search_filter,5000
                )
                    .setAction(android.R.string.ok) {}
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}