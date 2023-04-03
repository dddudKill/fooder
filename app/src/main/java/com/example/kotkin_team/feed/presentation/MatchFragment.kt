package com.example.kotkin_team.feed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.kotkin_team.R
import com.example.kotkin_team.common.view_binding.viewBinding
import com.example.kotkin_team.databinding.FragmentMatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.fragment_match) {

    private val binding by viewBinding(FragmentMatchBinding::bind)
    private var recipeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            recipeId = it.getInt(RECIPE_ID)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.backToFeedButton
        val startButton = binding.startButton
        backButton.setOnClickListener {
            openNewFragment(FeedFragment())
        }

        startButton.setOnClickListener {
            openNewFragment(FullRecipeFragment())
        }
    }

    private fun openNewFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.let {
            val transaction = it.beginTransaction()
            transaction
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    companion object {
        private const val RECIPE_ID = "recipe_id"

        fun newInstance(recipe_id: Int) = MatchFragment().apply {
            arguments = bundleOf(
                RECIPE_ID to recipe_id,
            )
        }
    }
}
