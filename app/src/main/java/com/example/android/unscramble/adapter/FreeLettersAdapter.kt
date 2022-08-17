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

class FreeLettersAdapter(
    private val context: Context,
    private val viewModel: GameViewModel
) : RecyclerView.Adapter<FreeLettersAdapter.LetterViewHolder>() {

    private lateinit var letters: MutableList<Char>

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
        holder.button.text = letters[position].toString()
        holder.button.setOnClickListener {
            viewModel.addNewSelectedLetter(holder.button.text[0])
            viewModel.deleteFreeLetter(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    fun updateScrambledWord(newLetters: MutableList<Char>) {
        letters = newLetters
        viewModel.addAllNewFreeLetters(letters)
        notifyDataSetChanged()
    }

    fun updateLetters(newLetters: MutableList<Char>) {
        letters = newLetters
        notifyDataSetChanged()
    }
}