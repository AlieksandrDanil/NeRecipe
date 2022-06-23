package ru.netology.nerecipe.activity

import android.net.Uri
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.authorArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.nameArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.catArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nerecipe.adapter.*
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.util.StringArg
import ru.netology.nerecipe.viewmodel.RecipeViewModel

class RecipeCardFragment : Fragment() {

    companion object {
        var Bundle.idArg: String? by StringArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardRecipeBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.idArg?.let {
            viewModel.getRecipeById(it.toLong())
        }?.let {

            val onInteractionListener = object : OnInteractionListener {
                private fun toNewRecipeFragment(recipe: Recipe) {
                    findNavController().navigate(
                        R.id.action_recipeCardFragment_to_newRecipeFragment,
                        Bundle().apply {
                            authorArg = recipe.author
                            nameArg = recipe.name
                            catArg = recipe.category
                            textArg = recipe.content
                        }
                    )
                }

                override fun onAuthor(recipe: Recipe) {
                    viewModel.edit(recipe)
                    toNewRecipeFragment(recipe)
                }

                override fun onName(recipe: Recipe) {
                    viewModel.edit(recipe)
                    toNewRecipeFragment(recipe)
                }

                override fun onCategory(recipe: Recipe) {
                    viewModel.edit(recipe)
                    toNewRecipeFragment(recipe)
                }

                override fun onContent(recipe: Recipe) {
                    viewModel.edit(recipe)
                    toNewRecipeFragment(recipe)
                }

                override fun onEdit(recipe: Recipe) {
                    viewModel.edit(recipe)
                    toNewRecipeFragment(recipe)
                }

                override fun onLike(recipe: Recipe) {
                    viewModel.likeById(recipe.id)
                }

                override fun onRemove(recipe: Recipe) {
                    viewModel.removeById(recipe.id)
                    findNavController().navigateUp()
                }

                override fun onShare(recipe: Recipe) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, recipe.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_recipe))
                    startActivity(shareIntent)

                    viewModel.shareById(recipe.id)
                }

                override fun onPlayVideo(recipe: Recipe) {
                    if (Uri.parse(recipe.video).isHierarchical) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.video))
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_VIEW
//                        setDataAndType(Uri.parse(post.video), "video/*")
//                    }
                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_share_recipe))
                        startActivity(shareIntent)

                    } else {
                        Snackbar.make(
                            binding.root, R.string.error_ref_entry,
                            BaseTransientBottomBar.LENGTH_INDEFINITE
                        )
                            .setAction(android.R.string.ok) {
                                findNavController().navigateUp()
                            }
                            .show()
                    }
                }
            }

            viewModel.dataRecipe.observe(viewLifecycleOwner) {
                recipeBinding(it, binding, onInteractionListener)
            }
        }

        return binding.root
    }
}