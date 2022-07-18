package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Stage

interface OnInteractionListener {
    fun onLike(recipe: Recipe) {}
    fun onEdit(recipe: Recipe) {}
    fun onRemove(recipe: Recipe) {}
    fun onAdd() {}
    fun onAuthor(recipe: Recipe)
    fun onName(recipe: Recipe)
    fun onCategory(recipe: Recipe)
    fun onContent(recipe: Recipe)
}

class RecipesAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }
}

class RecipeViewHolder(
    private val binding: CardRecipeBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        recipeBinding(recipe, binding, onInteractionListener)
    }
}

fun recipeBinding(
    recipe: Recipe,
    binding: CardRecipeBinding,
    onInteractionListener: OnInteractionListener
) {
    binding.apply {
        msvRecipe.tag = recipe.pos
        author.text = recipe.author
        name.text = recipe.name
        category.text = recipe.category
        //content.text = recipe.content
        favorite.isChecked = recipe.likedByMe
        favorite.text = recipe.likes.toString()

        stageContent.adapter = StagesAdapter(onInteractionStageListener = object : OnInteractionStageListener{
            override fun onClicked(stage: Stage) {
                // TO DO("Not yet implemented")
                //onInteractionListener.onStage(stage)
            }
        })

        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(recipe)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(recipe)
                            true
                        }
//                        R.id.add -> {
//                            onInteractionListener.onAdd()
//                            true
//                        }
                        else -> false
                    }
                }
            }.show()
        }

        author.setOnClickListener {
            onInteractionListener.onAuthor(recipe)
        }
        name.setOnClickListener {
            onInteractionListener.onName(recipe)
        }
        category.setOnClickListener {
            onInteractionListener.onCategory(recipe)
        }
        favorite.setOnClickListener {
            onInteractionListener.onLike(recipe)
        }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}