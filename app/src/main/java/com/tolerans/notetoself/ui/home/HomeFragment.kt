package com.tolerans.notetoself.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tolerans.notetoself.R
import com.tolerans.notetoself.databinding.FragmentHomeBinding
import com.tolerans.notetoself.ui.adapter.NoteListAdapter
import com.tolerans.notetoself.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding?=null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel:HomeViewModel by viewModels()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    fun initViews() = with(binding){
        rvHomeFragment.layoutManager = StaggeredGridLayoutManager(2, 1)
        btnHometogoAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        val items = listOf("Tümü", "Yapılacak", "Yapılıyor","Yapılan")
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,items)
        dropdownHomeFilter.setAdapter(adapter)

      dropdownHomeFilter.setOnItemClickListener { parent, view, position, id ->
          viewModel.getFilterNoteList(position)
      }

        btnFilter.setOnClickListener {
            viewModel.filterButtonClick()
        }
        btnFilterOkHome.setOnClickListener {
            viewModel.filterOkButtonClick()
        }
    }
    fun observeData() = with(binding){

        viewModel.noteList.observe(viewLifecycleOwner, { resource ->
            when (resource) {

                is Resource.Loading -> {
                    progressHome.visibility = View.VISIBLE
                }
                is Resource.Failed -> {
                    progressHome.visibility = View.GONE
                    txtHomeError.visibility = View.VISIBLE
                    txtHomeError.text = resource.message
                    rvHomeFragment.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressHome.visibility = View.GONE
                    txtHomeError.visibility = View.GONE
                    rvHomeFragment.visibility = View.VISIBLE
                    val adapter = NoteListAdapter(resource.data)
                    rvHomeFragment.adapter = adapter
                }

            }

        })

        viewModel.btnFilterVisibility.observe(viewLifecycleOwner,{status->
            if(status) btnFilter.visibility = View.VISIBLE else btnFilter.visibility = View.INVISIBLE
        })

        viewModel.layoutFilterVisibility.observe(viewLifecycleOwner,{status->
            if(status)filterLayout.visibility = View.VISIBLE else filterLayout.visibility = View.INVISIBLE
        })

    }


}