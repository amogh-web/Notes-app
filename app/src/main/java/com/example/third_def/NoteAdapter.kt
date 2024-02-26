package com.example.third_def

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes: MutableList<Notes>, private val listener: NoteClicked) : RecyclerView.Adapter<NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        val viewHolder = NotesViewHolder(view)
        view.setOnClickListener {
            listener.onClicked(notes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    fun updateNotes(newNotes: List<Notes>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val titleView:TextView = itemView.findViewById(R.id.lblTitle)
    private val dateTimeView:TextView = itemView.findViewById(R.id.lblDatetime)
    private val contentView :TextView = itemView.findViewById(R.id.lblContent)

    fun bind(note: Notes){
        titleView.text = note.title
        dateTimeView.text = note.datetime
        contentView.text = note.content
    }
}

interface NoteClicked{
    fun onClicked(note: Notes)
}