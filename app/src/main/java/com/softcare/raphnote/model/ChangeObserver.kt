package com.softcare.raphnote.model

interface ChangeObserver {
    fun  searchNotes (ascending: Boolean, orderColumn:String, query:String?)
    fun getNoteList (ascending: Boolean, orderColumn:String)
    fun  searchText ( query:String?)
    fun  optionMenu ( menuId:Int):Boolean
    fun editNote()
}