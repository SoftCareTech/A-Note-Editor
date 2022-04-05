package com.softcare.raphnote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.softcare.raphnote.model.NoteAdapter
import com.softcare.raphnote.databinding.FragmentNoteListBinding
import com.softcare.raphnote.db.NoteApp
import com.softcare.raphnote.model.ChangeObserver
import com.softcare.raphnote.model.ClickObserver
import com.softcare.raphnote.model.NoteListModel
import com.softcare.raphnote.model.NoteListModelFactory
import kotlinx.coroutines.flow.collect

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteListFragment : Fragment() {
    private val viewModel: NoteListModel by activityViewModels {
        NoteListModelFactory(
            (activity?.application as NoteApp).repository
        )
    }

    private val  orderAscending:Boolean
        get(){
            return  (activity as MainActivity?)!!.orderAscending
        }
    private val columnId:Boolean
        get(){
            return return  (activity as MainActivity?)!!.columnId
        }


    private var _binding: FragmentNoteListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val adapter: NoteAdapter = NoteAdapter(
            context, object : ClickObserver {
                override fun click(id: Long) {
                    val bundle = Bundle();
                    bundle.putLong("id", id)
                    findNavController(this@NoteListFragment).navigate(
                        R.id.action_ListNotesTo_ViewNote,
                        bundle
                    )

                }

                override fun click(id: Long, time: Long, text: String) {
                    val bundle = Bundle()
                    bundle.putLong("id", id)
                    bundle.putLong("time", time)
                    bundle.putString("text", text);
                    findNavController(this@NoteListFragment).navigate(
                        R.id.action_ListNotesTo_ViewNote,
                        bundle
                    )
                }
            }
        )

        binding.noteList.adapter = adapter
        binding.noteList.setLayoutManager(LinearLayoutManager(context))

        // binding.root.setOnLongClickListener(View.OnLongClickListener
        lifecycleScope.launchWhenStarted {
            viewModel.noteList.collect() {
                adapter.changeNotes(it)
            }


        }



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)!!.changeMenu(changeObserver = object : ChangeObserver {
            override fun searchNotes(query: String?) {
                if (query != null) {
                    viewModel.searchNotes(orderAscending, columnId, query)
                } else
                    viewModel.getNotes(orderAscending, columnId)
            }
            override fun searchText(query: String?) {

            }

            override fun optionMenu(menuId: Int): Boolean {
                when (menuId) {
                    R.id.action_settings -> startActivity(
                        Intent(
                            context,
                            SettingsActivity::class.java
                        )
                    )
                    R.id.action_open_file -> {
                        val bundle =Bundle()
                        bundle.putBoolean("openFile",true)
                        findNavController(this@NoteListFragment).navigate(  R.id.action_ListNotesTo_ViewNote,
                            bundle  )
                    }
                    R.id.action_share_app -> shareApp()
                    R.id.action_app_guide -> appGuide()
                    R.id.action_more -> more()
                    else -> return false
                }
                return true
            }

            override fun editNote() {
                startActivity(Intent(context, EditActivity::class.java))
            }

        }, isList = true)

    }


    fun shareApp() {
        //https://play.google.com/store/apps/details?id=com.softcare.raphnote
        val contentUri = Uri.parse("android.resource://com.softcare.raphnote/drawable/image_name")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=com.softcare.raphnote"
            )
            type = "text/*"
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Share to")
            // (Optional) Here we're passing a content URI to an image to be displayed
            //data = contentUri
            // flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share with")
        startActivity(shareIntent)

    }

    fun more(){
        val url = "https://raph-ray.blogspot.com/2021/08/a-note-editor.html"
        try {
            this.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (e: Exception) {
            context?.let {
                Snackbar.make(
                    binding.root,
                    it.getString(R.string.error_occurred), Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotes(ascending = orderAscending, orderById = columnId)
    }
private  fun  appGuide() {
    this.context?.startActivity(Intent(Intent(context, AppGuide::class.java)))
}
}