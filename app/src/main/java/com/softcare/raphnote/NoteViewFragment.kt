package com.softcare.raphnote

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.softcare.raphnote.databinding.FragmentNoteViewBinding
import com.softcare.raphnote.db.NoteApp
import com.softcare.raphnote.db.Schema
import com.softcare.raphnote.model.ChangeObserver
import com.softcare.raphnote.model.NoteViewModel
import com.softcare.raphnote.model.NoteViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteViewFragment : Fragment() , View.OnTouchListener {
    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((activity?.application as NoteApp).repository)
    }

    private var id = 0L
    private var time = 0L

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentNoteViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteViewBinding.inflate(inflater, container, false)
       // binding.text.setText(arguments?.getString("text", "text"))
      //  binding.time.setText(arguments?.getLong("time")?.let { Schema().getTime(it) })
        //arguments?.getLong("time")?.let { time = it }
       // arguments?.getLong("id")?.let { id = it }
        binding.text.setOnTouchListener(this)
        id = arguments?.getLong("id", 0L)!!
        if(id==0L) {
            val  path=arguments?.getString("path")
            if(path!=null){
                viewModel.openFile(path)
            } else {
                binding.text.text = arguments?.getString("text")
                arguments?.getLong("time")?.let { time = it }
            }
        }
        else viewModel.openNote(id)


        lifecycleScope.launchWhenStarted {
            viewModel.noteUiState.collect {
                when (it) {

                    is NoteViewModel.NoteUiState.NoteOpened -> {
                        binding.text.text = it.note.text
                        binding.time.text=Schema().getTime(it.note.time)
                           time = it.note.time
                          id = it.note.id
                    }
                    is NoteViewModel.NoteUiState.FileOpened -> {
                        binding.text.text=it.text
                    }

                    is NoteViewModel.NoteUiState.Error -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_INDEFINITE).show()
                    }
                    NoteViewModel.NoteUiState.Empty -> {}
                    NoteViewModel.NoteUiState.Opening -> {
                        Snackbar.make(binding.root, getString(R.string.opening), Snackbar.LENGTH_INDEFINITE).show()
                    }
                    NoteViewModel.NoteUiState.Deleting -> {
                        Snackbar.make(binding.root, getString(R.string.deleting), Snackbar.LENGTH_INDEFINITE).show()
                    }
                    is NoteViewModel.NoteUiState.Exported -> {
                        Snackbar.make(binding.root, getString(R.string.exported_to)+" "+it.path, Snackbar.LENGTH_LONG).show()
                    }
                    NoteViewModel.NoteUiState.Exporting -> {
                        Snackbar.make(binding.root, getString(R.string.exporting), Snackbar.LENGTH_INDEFINITE).show()
                    }
                    NoteViewModel.NoteUiState.NoteDeleted -> {
                        Toast.makeText(context, getString(R.string.note_deleted), Toast.LENGTH_LONG).show()

                    }
                }  }   }



        return binding.root

    }

    private fun searchInText(textView: TextView, query: String) {
      /*  val WordtoSpan: Spannable = SpannableString("partial colored text")
        WordtoSpan.setSpan(
            ForegroundColorSpan(Color.BLUE),
            2,
            4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = WordtoSpan*/
        textView.text=textView.text.toString()
        val tvt: String = textView.text.toString()
        var ofe: Int = tvt.indexOf(query, 0)
        val wordToSpan: Spannable = SpannableString(textView.text)
        var ofs = 0
        while (ofs < tvt.length && ofe != -1){
            ofs = ofe + 1
            ofe = tvt.indexOf(query, ofs)
            if (ofe == -1)
                break
            else {
                // set color here
                wordToSpan.setSpan( //ForegroundColorSpan(Color.BLUE)
                     BackgroundColorSpan(Color.GRAY)
                    , ofe, ofe + query.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textView.setText(wordToSpan, TextView.BufferType.SPANNABLE)
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity?)!!.changeMenu(changeObserver = object : ChangeObserver {
            override fun searchText(query: String?) {
                query?.let { searchInText(binding.text, it) }
            }

            override fun optionMenu(menuId: Int): Boolean {
                when (menuId) {
                    R.id.action_copy_all -> {
                        copyToClipboard(binding.text.text.toString())
                    }
                    R.id.action_export -> {
                        exportToFile()
                    }
                    R.id.action_delete -> {
                        deleteNote()
                    }
                    R.id.action_share_text -> {
                        shareText(binding.text.toString())
                    }
                    else -> return false
                }
                return  true
            }

            override fun editNote() {
                val intent = Intent(context, EditActivity::class.java)
                intent.putExtra("text", binding.text.text.toString())
                intent.putExtra("id", id)
                intent.putExtra("time", time)
                startActivity(intent)
            }


            override fun searchNotes(ascending: Boolean, orderColumn: String, query: String?) {
            }

            override fun getNoteList(ascending: Boolean, orderColumn: String) {
            }

        }, isList = false)


    }

  private  fun copyToClipboard(text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root, getString(R.string.copied), Snackbar.LENGTH_LONG).show()
    }

  private  fun deleteNote() {
        context?.let {
            val title = getString(R.string.delete)
            val message = getString(R.string.delete_note_msg)

            val button1String = getString(R.string.yes)
            val button2String = getString(R.string.no)
            val ad: AlertDialog.Builder = AlertDialog.Builder(it)
            ad.setTitle(title)
            ad.setCancelable(false)
            ad.setMessage(message)
            ad.setPositiveButton(
                button1String,
                { dialog, arg1 -> })
            ad.setNegativeButton(
                button2String,
                { dialog, arg1 ->viewModel.deleteNote() })
            ad.show()
        }
    }
/*
    private fun delete(){
            CoroutineScope(Dispatchers.IO).launch {
                (activity?.application as NoteApp).repository.deleteNote(Note(id, time, ""))

        }    }
    */
    fun shareText(text:String){
        //https://play.google.com/store/apps/details?id=com.softcare.raphnote
        val contentUri= Uri.parse("android.resource://com.softcare.raphnote/drawable/image_name")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/*"
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Send to")
            // (Optional) Here we're passing a content URI to an image to be displayed
            data = contentUri
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share with")
        startActivity(shareIntent)
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if(data!=null)
                data.data?.path?.let {viewModel.exportToFile(it,binding.text.toString()) }
            else context?.let {
                Snackbar.make(binding.root,
                    it.getString(R.string.error_occurred), Snackbar.LENGTH_LONG).show()
            }
        }
    }
    private fun exportToFile(){
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
            putExtra(Intent.EXTRA_TITLE, getString(R.string.default_save_name))

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            // putExtra(DocumentsContract.EXTRA_INITIAL_URI, null)
        }
        resultLauncher.launch(intent)
    }




















    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    val move = 200f

    var ratio = 1.0f

    var bastDst = 0

    var baseratio = 0f
    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
            val action = event.action
            val mainaction = action and MotionEvent.ACTION_MASK
            if (mainaction == MotionEvent.ACTION_POINTER_DOWN) {
                bastDst = getDistance(event)
                baseratio = ratio
            } else {

                // if ACTION_POINTER_UP then after finding the distance

                // we will increase the text size by 15
                val scale: Float = (getDistance(event) - bastDst) / move
                val factor = 2.0.pow(scale.toDouble()).toFloat()
                ratio = min(1024.0f, max(0.1f, baseratio * factor))
                binding.text.textSize = ratio + 15
            }
        }
        return true
    }


    // get distance between the touch event

    // get distance between the touch event
    private fun getDistance(event: MotionEvent): Int {
        val dx = (event.getX(0) - event.getX(1)).toInt()
        val dy = (event.getY(0) - event.getY(1)).toInt()
        return sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let { onTouchEvent(it) }
        return false
    }


}