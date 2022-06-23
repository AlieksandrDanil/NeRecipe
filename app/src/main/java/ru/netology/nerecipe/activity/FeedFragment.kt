package ru.netology.nerecipe.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.RecipeCardFragment.Companion.idArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.authorArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.nameArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.catArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nerecipe.adapter.OnInteractionListener
import ru.netology.nerecipe.adapter.PostsAdapter
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.viewmodel.RecipeViewModel

class FeedFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostsAdapter(object : OnInteractionListener {
            private fun toNewRecipeFragment(recipe: Recipe) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newRecipeFragment,
                    Bundle().apply {
                        authorArg = recipe.author
                        nameArg = recipe.name
                        catArg = recipe.category
                        textArg = recipe.content
                    }
                )
            }

            private fun toRecipeCardFragment(recipe: Recipe) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_recipeCardFragment,
                    Bundle().apply {
                        idArg = recipe.id.toString()
                    }
                )
            }

            override fun onAuthor(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onName(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onCategory(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onContent(recipe: Recipe) {
                toRecipeCardFragment(recipe)
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
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        setDataAndType(Uri.parse(recipe.video), "video/*")
                    }
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
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_feedFragment_to_newRecipeFragment,
                Bundle().apply {
                    authorArg = null
                    nameArg = null
                    catArg = null
                    textArg = null
                }
            )
        }

        return binding.root
    }
}