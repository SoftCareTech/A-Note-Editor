package com.softcare.raphnote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class  Note(@PrimaryKey
                 @ColumnInfo(name="id") var id:Long,
                 @ColumnInfo(name="time")var time:Long,
                 @ColumnInfo(name="text")var text:String) {
}

data class  NoteFile(var title:String, var text:String) {
}