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

/* check for  String buffer for textview
check and use read next
get images of the program and add it slider
enable assending and decending
Collasping bar image to reall design

download face, whatApp , chrom and   lock images to add to design
 test on android 11.

buid aab file and upload

redo note app the cover all areas




 */