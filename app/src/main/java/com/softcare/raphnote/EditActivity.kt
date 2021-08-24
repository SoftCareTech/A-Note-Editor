package com.softcare.raphnote
import android.content.Intent
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
import com.softcare.raphnote.model.NoteEditModel
import com.softcare.raphnote.model.NoteEditModelFactory
import kotlinx.coroutines.flow.collect


open class EditActivity : AppCompatActivity(), View.OnClickListener {
    var id = 0L
    private val viewModel: NoteEditModel by viewModels {
        NoteEditModelFactory((application as NoteApp).repository)
    }

    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getLongExtra("id", 0L)
        if(id==0L) {
            val textReceived = intent.getStringExtra(Intent.EXTRA_TEXT)

            if (textReceived!=null){
                binding.textEdit.setText(textReceived)
            } else {

                val path = intent.getStringExtra("path")
                if (path != null) {
                    viewModel.openFile(path)
                } else
                    binding.textEdit.setText(intent.getStringExtra("text"))
            }
        }
        else viewModel.openNote(id)
        binding.back.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        lifecycleScope.launchWhenStarted {
            viewModel.noteUiState.collect {
                when (it) {
                    is NoteEditModel.NoteUiState.Empty -> {

                    }
                    is NoteEditModel.NoteUiState.NoteOpened-> {
                        binding.textEdit.setText(it.note.text)
                    }
                    is NoteEditModel.NoteUiState.FileOpened-> {
                        binding.textEdit.setText(it.text)
                    }
                    is NoteEditModel.NoteUiState.Saved -> {
                        val s: SharedPreferences = getSharedPreferences(
                            "RaphNote",
                            MODE_PRIVATE
                        )
                       if( s.getBoolean("save_then_exit",true)){
                           Toast.makeText(this@EditActivity,getString(R.string.saved),Toast.LENGTH_LONG).show()
                           exit()
                       }  else
                        Snackbar.make(binding.root,getString(R.string.saved),Snackbar.LENGTH_LONG).show()

                    }
                    is NoteEditModel.NoteUiState.Saving -> {

                        Snackbar.make(binding.root,getString(R.string.saving),Snackbar.LENGTH_INDEFINITE).show()
                    }
                    is NoteEditModel.NoteUiState.Error -> {
  Snackbar.make(binding.root,it.message,Snackbar.LENGTH_INDEFINITE).show()
                    }
                    NoteEditModel.NoteUiState.Opening -> { Snackbar.make(binding.root,getString(R.string.opening),Snackbar.LENGTH_INDEFINITE).show()}
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
        else exitNote()
    }
    private fun exitNote() {
            val title = getString(R.string.exit_editing)
            val message = getString(R.string.exit_edit_msg)
        val button1String = getString(R.string.yes)
        val button2String = getString(R.string.no)
            val ad: AlertDialog.Builder = AlertDialog.Builder(this)
            ad.setTitle(title)
            ad.setMessage(message)
            ad.setPositiveButton(
                button1String,
                { dialog, arg1 -> save() })
            ad.setNegativeButton(
                button2String
            ) { dialog, arg1 ->  exit() }
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