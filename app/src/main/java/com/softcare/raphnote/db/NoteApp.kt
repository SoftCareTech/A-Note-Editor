package com.softcare.raphnote.db


import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApp : Application() {
    private val applicationScope= CoroutineScope(SupervisorJob() )
    val database by lazy { NoteDb.getDatabase(this,applicationScope )  }
    val repository by lazy { Repos(database.noteDao())
    
    }
}