package com.softcare.raphnote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.softcare.business.model.NoteAdapter
import com.softcare.raphnote.databinding.FragmentNoteListBinding
import com.softcare.raphnote.model.ClickObserver
import com.softcare.raphnote.model.NoteListModel
import kotlinx.coroutines.flow.collect

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteListFragment : Fragment() {

    private val viewModel: NoteListModel by viewModels()
    private var _binding: FragmentNoteListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?   ): View? {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val adapter:NoteAdapter= NoteAdapter(
            context, object :ClickObserver{
                override fun click(id: Long) {
                    val bundle =  Bundle();
                    bundle.putLong("id", id);
                    findNavController(this@NoteListFragment).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)

                }
                override fun click(id: Long, time: Long, text: String) {
                    val bundle =  Bundle();
                    bundle.putLong("id", id);
                    bundle.putLong("time", time);
                    bundle.putString("text", text);
                    findNavController(this@NoteListFragment).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
                }
            }
        )

        binding.noteList.adapter=adapter
        binding.noteList.setLayoutManager(LinearLayoutManager(context));

        lifecycleScope.launchWhenStarted {
            viewModel.noteListUiState.collect{
                when(it){
                    is NoteListModel.NoteListUiState.Empty->{
                    }
                    is NoteListModel.NoteListUiState.OpenDatabase->{
                        adapter.changeNotes(noteList = it.noteList)
                    }
                    is NoteListModel.NoteListUiState.OpenFolder->{
                        adapter.changeNotes(noteList = it.noteList)
                    }
                }
            }

        }
        viewModel.getNoteList()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        /*
         var id=0L
            val bundle =  Bundle();
            bundle.putLong("id", id);
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
         */
    }
}