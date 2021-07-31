package com.softcare.business.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.softcare.raphnote.R
import com.softcare.raphnote.model.ClickObserver
import com.softcare.raphnote.model.Note
import java.util.*

class NoteAdapter(var context: Context?, var clickObserver: ClickObserver?) :  RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var noteList:List<Note>
    init {
        noteList=ArrayList<Note>()
    }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(context?.applicationContext)
            .inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.time.text = noteList[position].time.toString()
        holder.text.text = noteList[position].text
        holder.root.setOnClickListener(View.OnClickListener {
              clickObserver?.click(noteList[position].id)
        })
    }

    fun changeNotes(noteList: List<Note>) {
        if (noteList != null) {
            this.noteList = noteList
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return if (this.noteList != null) {
            noteList.size
        } else 0
    }

    class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val time :TextView
        val text: TextView
        val root: ConstraintLayout
        init {
            time = itemView.findViewById(R.id.time)
            text = itemView.findViewById(R.id.text)
            root = itemView.findViewById(R.id.root)
        }
    }


}