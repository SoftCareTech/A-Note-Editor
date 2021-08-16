package com.softcare.raphnote

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.softcare.raphnote.databinding.FragmentNoteViewBinding
import com.softcare.raphnote.db.NoteApp
import com.softcare.raphnote.db.Schema
import com.softcare.raphnote.model.ChangeObserver
import com.softcare.raphnote.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteViewFragment : Fragment() , View.OnTouchListener {


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
        binding.text.setText(arguments?.getString("text", "text"))
        binding.time.setText(arguments?.getLong("time")?.let { Schema().getTime(it) })
        arguments?.getLong("time")?.let { time = it }
        arguments?.getLong("id")?.let { id = it }
        binding.text.setOnTouchListener(this)
        return binding.root

    }

    private fun searchInText(text: TextView, query: String?) {
        Snackbar.make(text, "Searching  for $query not implemented yet", Snackbar.LENGTH_LONG)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity?)!!.changeMenu(changeObserver = object : ChangeObserver {
            override fun searchText(query: String?) {
                searchInText(binding.text, query)
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

    fun copyToClipboard(text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root, "Copied ", Snackbar.LENGTH_LONG).show()
    }

    fun deleteNote() {
        context?.let {
            val title = "Delete"
            val message = "Delete current note? You won't be able to recover it"
            val button1String = "No"
            val button2String = "Yes"
            val ad: AlertDialog.Builder = AlertDialog.Builder(it)
            ad.setTitle(title)
            ad.setCancelable(false)
            ad.setMessage(message)
            ad.setPositiveButton(
                button1String,
                { dialog, arg1 -> })
            ad.setNegativeButton(
                button2String,
                { dialog, arg1 -> delete() })
            ad.show()
        }
    }

    private fun delete(){

            CoroutineScope(Dispatchers.IO).launch {
                (activity?.application as NoteApp).repository.deleteNote(Note(id, time, ""))

        }

    }
    fun shareText(text:String){
        //https://play.google.com/store/apps/details?id=com.softcare.raphnote
        val contentUri= Uri.parse("android.resource://com.softcare.raphnote/drawable/image_name")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Send to")
            // (Optional) Here we're passing a content URI to an image to be displayed
            data = contentUri
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share with")
        startActivity(shareIntent)
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if(data!=null)
                data.data?.path?.let {exportToFile(it,binding.text.toString()) }
            else context?.let {
                Snackbar.make(binding.root,
                    it.getString(R.string.error_occurred), Snackbar.LENGTH_LONG).show()
            }
        }
    }
    private fun exportToFile(){
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            // putExtra(DocumentsContract.EXTRA_INITIAL_URI, null)
        }
        resultLauncher.launch(intent)
    }
    private fun exportToFile(path:String, text:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(path)
            file.writeText(text, Charsets.UTF_32)

        }

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
                val factor = Math.pow(2.0, scale.toDouble()).toFloat()
                ratio = Math.min(1024.0f, Math.max(0.1f, baseratio * factor))
                binding.text.setTextSize(ratio + 15)
            }
        }
        return true
    }


    // get distance between the touch event

    // get distance between the touch event
    private fun getDistance(event: MotionEvent): Int {
        val dx = (event.getX(0) - event.getX(1)).toInt()
        val dy = (event.getY(0) - event.getY(1)).toInt()
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let { onTouchEvent(it) }
        return false
    }


}