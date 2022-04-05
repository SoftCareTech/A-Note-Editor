package com.softcare.raphnote.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.softcare.raphnote.R
import com.softcare.raphnote.db.Schema
import java.util.*

class PageAdapter(var context: Context?) :  RecyclerView.Adapter<PageAdapter.NoteViewHolder>() {
    var noteList:List<Page>
    init {
        noteList=ArrayList<Page>()
    }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(context?.applicationContext)
            .inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.text.text =  noteList[position].text
        holder.index.text= noteList[position].index.toString()
    }

    fun changeNotes(noteList: List<Page>) {
        if (noteList != null) {
            this.noteList = noteList
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
         return    noteList.size
    }

    class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val index: TextView
        val text: TextView
        init {
            index = itemView.findViewById(R.id.index)
            text = itemView.findViewById(R.id.text)
        }
    }


}

data class  Page(val text:String,  val index:Int)