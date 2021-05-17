package com.tolerans.notetoself.ui.detail

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tolerans.notetoself.R
import com.tolerans.notetoself.databinding.FragmentNoteDetailBinding
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.util.AppExtensions.toBase64String
import com.tolerans.notetoself.util.AppExtensions.toBitmap
import com.tolerans.notetoself.util.Const
import com.tolerans.notetoself.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.lang.Exception

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private var _binding : FragmentNoteDetailBinding?=null
    private val binding: FragmentNoteDetailBinding get() = _binding!!

    private val args:NoteDetailFragmentArgs by navArgs()

    private val viewModel: NoteDetailViewModel by viewModels()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Const.SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null && data.data!=null){
            try{
                val imageUri: Uri = data.data!!
                val imageStream: InputStream? = requireActivity().contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)

                viewModel.addPicturetoNote(selectedImage)
            }catch (exp : Exception){

            }

        }
    }



    fun observeData()  = with(binding){
        viewModel.getDetail(args.noteID)
        viewModel.noteDetail.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBarNoteDetail.visibility = View.VISIBLE
                }
                is Resource.Failed -> {
                    progressBarNoteDetail.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    progressBarNoteDetail.visibility = View.GONE
                    initViews(resource.data)
                }
            }

        })

        viewModel.deleteStatus.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBarNoteDetail.visibility = View.VISIBLE
                }
                is Resource.Failed -> {
                    progressBarNoteDetail.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    progressBarNoteDetail.visibility = View.GONE
                    Toast.makeText(requireContext(), "Başarılı", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_noteDetailFragment_to_homeFragment)
                }
            }
        })

        viewModel.updateStatus.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBarNoteDetail.visibility = View.VISIBLE
                }
                is Resource.Failed -> {
                    progressBarNoteDetail.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    progressBarNoteDetail.visibility = View.GONE
                    Toast.makeText(requireContext(), "Başarılı", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_noteDetailFragment_to_homeFragment)
                }
            }
        })

        viewModel.btnCancelVisibility.observe(viewLifecycleOwner, { status ->
            if (status) btnNoteDetailCancel.visibility = View.VISIBLE else btnNoteDetailCancel.visibility = View.GONE
        })
        viewModel.btnDeleteVisibility.observe(viewLifecycleOwner, { status ->
            if (status) btnNoteDetailEdit.visibility = View.VISIBLE else btnNoteDetailEdit.visibility = View.GONE
        })
        viewModel.btnCompleteVisibility.observe(viewLifecycleOwner, { status ->
            if (status) btnNoteDetailDone.visibility = View.VISIBLE else btnNoteDetailDone.visibility = View.GONE
        })
        viewModel.btnEditVisibility.observe(viewLifecycleOwner, { status ->
            if (status) btnNoteDetailEdit.visibility = View.VISIBLE else btnNoteDetailEdit.visibility = View.GONE
        })
        viewModel.txtTitleVisibility.observe(viewLifecycleOwner, { status ->
            if (status) txtNoteDetailTitle.visibility = View.VISIBLE else txtNoteDetailTitle.visibility = View.GONE
        })
        viewModel.txtDescriptionVisibility.observe(viewLifecycleOwner, { status ->
            if (status) txtNoteDetailDesc.visibility = View.VISIBLE else txtNoteDetailDesc.visibility = View.GONE
        })
        viewModel.edtTitleVisibility.observe(viewLifecycleOwner, { status ->
            if (status) txtInputLayoutNoteDetailTitle.visibility = View.VISIBLE else txtInputLayoutNoteDetailTitle.visibility = View.GONE
        })
        viewModel.edtDescriptionVisibility.observe(viewLifecycleOwner, { status ->
            if (status) txtInputLayoutNoteDetailDescription.visibility = View.VISIBLE else txtInputLayoutNoteDetailDescription.visibility = View.GONE
        })

        viewModel.btnAddPhotoVisibility.observe(viewLifecycleOwner,{status ->
            if (status) btnAddPhoto.visibility = View.VISIBLE else btnAddPhoto.visibility = View.GONE
        })
        viewModel.btnRemovePhotoVisibility.observe(viewLifecycleOwner,{status ->
            if (status) btnRemovePhoto.visibility = View.VISIBLE else btnRemovePhoto.visibility = View.GONE
        })

        viewModel.radioGroupEnabled.observe(viewLifecycleOwner,{status->
            if(status){
                radioNoteDetailToDo.setEnabled(true)
                radioNoteDetailBeingDone.setEnabled(true)
                radioNoteDetailDone.setEnabled(true)
            }else{
                radioNoteDetailToDo.setEnabled(false)
                radioNoteDetailBeingDone.setEnabled(false)
                radioNoteDetailDone.setEnabled(false)
            }
        })

        viewModel.noteImage.observe(viewLifecycleOwner,{bitmap->
            imgNoteDetail.setImageBitmap(bitmap)
        })

    }
    fun initViews(note: Note) = with(binding){

        txtNoteDetailTitle.setText(note.title)
        txtNoteDetailDesc.setText(note.description)

        txtInputEdittextNoteDetailTitle.setText(note.title)
        txtInputEdittextNoteDetailDescription.setText(note.description)

        when(note.status){
            0->{radioGroupNoteDetail.check(R.id.radioNoteDetailToDo)}
            1->{radioGroupNoteDetail.check(R.id.radioNoteDetailBeingDone)}
            2->{radioGroupNoteDetail.check(R.id.radioNoteDetailDone)}
        }

        note.noteImage?.let { base64Data->
            imgNoteDetail.setImageBitmap(base64Data.toBitmap())
        }
        btnNoteDetailCancel.setOnClickListener {
            viewModel.cancelNote()
        }
        btnNoteDetailDelete.setOnClickListener {
            viewModel.deleteNote(note)
        }
        btnNoteDetailDone.setOnClickListener {

            val radioButtonID: Int = radioGroupNoteDetail.getCheckedRadioButtonId()
            val radioButton: View = radioGroupNoteDetail.findViewById(radioButtonID)
            val statusIndex: Int = radioGroupNoteDetail.indexOfChild(radioButton)
            var newNote = Note(noteID = note.noteID, title = txtInputEdittextNoteDetailTitle.text.toString(), description = txtInputEdittextNoteDetailDescription.text.toString(), status = statusIndex)

            viewModel.noteImage.observe(viewLifecycleOwner,{bitmap->
                bitmap?.let {
                    newNote = Note(noteID = note.noteID, title = txtInputEdittextNoteDetailTitle.text.toString(), description = txtInputEdittextNoteDetailDescription.text.toString(), status = statusIndex,noteImage = bitmap.toBase64String())

                }
                viewModel.completeNote(newNote)
            })

        }
        btnNoteDetailEdit.setOnClickListener {
            viewModel.updateNote(note)
        }
        btnRemovePhoto.setOnClickListener {
            viewModel.removePhoto()
        }

        btnAddPhoto.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent, Const.SELECT_PHOTO)
        }

    }
}