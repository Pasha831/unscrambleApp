/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.unscramble.R
import com.example.android.unscramble.adapter.FreeLettersAdapter
import com.example.android.unscramble.adapter.SelectedLettersAdapter
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment
    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // RecyclerView with letters
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Setting up a RecyclerView for free letters adapter
        val freeLettersAdapter = FreeLettersAdapter(requireContext(), viewModel).apply {
            updateScrambledWord(viewModel.currentScrambledWord.value.toString().toMutableList())
        }
        recyclerView = binding.rvFreeLetters
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        recyclerView.adapter = freeLettersAdapter

        // Setting up a RecyclerView for selected letters adapter
        val selectedLettersAdapter = SelectedLettersAdapter(requireContext(), viewModel).apply {
            updateScrambledWord()
        }
        recyclerView = binding.rvSelectedLetters
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        recyclerView.adapter = selectedLettersAdapter

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        viewModel.currentScrambledWord.observe(viewLifecycleOwner) { newWord ->
            freeLettersAdapter.updateScrambledWord(newWord.toString().toMutableList())
            selectedLettersAdapter.updateScrambledWord()
        }

        viewModel.currentSelectedLetters.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                selectedLettersAdapter.updateLetters(it.last())
            }
        }

        viewModel.currentFreeLetters.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                freeLettersAdapter.updateLetters(it.last())
            }
        }
    }

    /**
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() {
//        TODO: change this method
//        val playerWord = binding.textInputEditText.text.toString()
//
//        if (viewModel.isUserWordCorrect(playerWord)) {
//            setErrorTextField(false)
//            if (!viewModel.nextWord()) {
//                showFinalScoreDialog()
//            }
//        } else {
//            setErrorTextField(true)
//        }
    }

    /**
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        if (!viewModel.nextWord()) {
            showFinalScoreDialog()
        }
    }

    /**
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
    }

    /**
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }
}
