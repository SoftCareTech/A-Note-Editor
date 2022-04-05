package com.softcare.raphnote.model

interface ChangeObserver {
    fun  searchNotes ( query:String?)
    fun  searchText ( query:String?)
    fun  optionMenu ( menuId:Int):Boolean
    fun editNote()
}