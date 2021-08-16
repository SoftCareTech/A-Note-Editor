package com.softcare.raphnote

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.softcare.raphnote.databinding.ActivityEditBinding
import com.softcare.raphnote.db.NoteApp
import com.softcare.raphnote.model.Note
import com.softcare.raphnote.model.NoteModel
import com.softcare.raphnote.model.NoteModelFactory
import kotlinx.coroutines.flow.collect


open class EditActivity : AppCompatActivity(), View.OnClickListener {
    var id = 0L
    private val viewModel: NoteModel by viewModels {
        NoteModelFactory((application as NoteApp).repository)
    }

    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getLongExtra("id", 0L)
        if(id==0L) {
           val  path=intent.getStringExtra("path")
            if(path!=null){
                viewModel.openFile(path)
            } else
            binding.textEdit.setText(intent.getStringExtra("text"))
        }
        else viewModel.openNote(id)
        binding.back.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        lifecycleScope.launchWhenStarted {
            viewModel.noteUiState.collect {
                when (it) {
                    is NoteModel.NoteUiState.Empty -> {

                    }
                    is NoteModel.NoteUiState.NoteOpened-> {
                        binding.textEdit.setText(it.note.text)
                    }
                    is NoteModel.NoteUiState.FileOpened-> {
                        binding.textEdit.setText(it.text)
                    }
                    is NoteModel.NoteUiState.Saved -> {
                        val s: SharedPreferences = getSharedPreferences(
                            "RaphNote",
                            MODE_PRIVATE
                        )
                       if( s.getBoolean("save_then_exit",true)){
                           Toast.makeText(this@EditActivity,"save",Toast.LENGTH_LONG).show()
                           exit()
                       }  else
                        Snackbar.make(binding.root,"Saved",Snackbar.LENGTH_LONG).show()

                    }
                    is NoteModel.NoteUiState.Saving -> {

                        Snackbar.make(binding.root,"Saving",Snackbar.LENGTH_INDEFINITE).show()
                    }
                    is NoteModel.NoteUiState.Error -> {
  Snackbar.make(binding.root,it.message,Snackbar.LENGTH_INDEFINITE).show()
                    }
                    NoteModel.NoteUiState.Opening -> { Snackbar.make(binding.root,"Opening",Snackbar.LENGTH_INDEFINITE).show()}
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == binding.save)
            save()
        else if (v == binding.back) {
            exitNote()
        }

    }

    override fun onBackPressed() {
        if(exit)super.onBackPressed()
    }
    private fun exitNote() {
            val title = "Exit "
            val message = "You are leaving edit mode. Will like to save changes"
            val button1String = "No"
            val button2String = "Yes"
            val ad: AlertDialog.Builder = AlertDialog.Builder(this)
            ad.setTitle(title)
            ad.setMessage(message)
            ad.setPositiveButton(
                button1String,
                { dialog, arg1 -> exit()})
            ad.setNegativeButton(
                button2String
            ) { dialog, arg1 -> save() }
        ad.show()

    }
  private  var exit=false
    private  fun exit(){
        exit=true
        onBackPressed()
    }
    private fun  save(){
        if (id == 0L)
            id = System.currentTimeMillis()
        val time = System.currentTimeMillis()
        viewModel.save(
            Note(
                id,
                time,
                binding.textEdit.text.toString()
            )
        )
    }
}