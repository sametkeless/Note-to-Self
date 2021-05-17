package com.tolerans.notetoself.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tolerans.notetoself.R
import com.tolerans.notetoself.databinding.RvItemNotelistBinding
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.ui.detail.NoteDetailFragment
import com.tolerans.notetoself.ui.home.HomeFragmentDirections
import com.tolerans.notetoself.util.AppExtensions.toBitmap

class NoteListAdapter(val noteList:List<Note>): RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {
    inner class NoteListViewHolder(var binding: RvItemNotelistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) = with(binding) {
            txtNoteListItemTitle.text = note.title
            txtNoteListItemDescription.text = note.description
            note.noteImage?.let { base64Data ->
                imgNoteListItem.setImageBitmap(base64Data.toBitmap())
            } ?: run{
             imgNoteListItem.visibility = View.GONE
            }
            when(note.status){
                0->{imgNoteListStatus.setImageResource(R.drawable.ic_blank)}
                1->{imgNoteListStatus.setImageResource(R.drawable.ic_loading)}
                2->{imgNoteListStatus.setImageResource(R.drawable.ic_done)}
            }

            binding.root.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(note.noteID)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder(
            RvItemNotelistBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(
        holder: NoteListViewHolder,
        position: Int) {
        holder.bind(noteList.get(position))
    }

    override fun getItemCount(): Int = noteList.size
}