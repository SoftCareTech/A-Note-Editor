package com.softcare.raphnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softcare.raphnote.databinding.ActivityEditBinding
import com.softcare.raphnote.databinding.ActivityMainBinding
import com.softcare.raphnote.model.NoteModel
import kotlinx.coroutines.flow.collect

open class EditActivity : AppCompatActivity() {

    private val viewModel:NoteModel by viewModels()
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launchWhenStarted {
            viewModel.noteUiState.collect{
                when(it){
                    is NoteModel.NoteUiState.Empty->{

                    }
                    is NoteModel.NoteUiState.Open->{

                    }
                    is NoteModel.NoteUiState.Unsaved->{

                    }
                    is NoteModel.NoteUiState.Saved->{

                    }
                    is NoteModel.NoteUiState.Saving->{

                    }
                    is NoteModel.NoteUiState.Error->{

                    }
                }
            }
        }
    }
}