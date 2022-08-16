package com.example.android.unscramble.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.android.unscramble.R
import com.example.android.unscramble.ui.game.GameViewModel

class LetterAdapter(
    private val context: Context,
    private val viewModel: GameViewModel
) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    private var letters: String = viewModel.currentScrambledWord.value.toString()

    class LetterViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.letter_item, parent, false)
        return LetterViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        Log.d("lol", "${viewModel.currentScrambledWord.value!!.length}")
        holder.button.text = letters[position].toString()
    }

    override fun getItemCount(): Int {
        return viewModel.currentScrambledWord.value!!.length
    }

    fun updateScrambledWord(newLetters: String) {
        letters = newLetters
        notifyDataSetChanged()
    }
}