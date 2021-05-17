package com.tolerans.notetoself.ui.addnote

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.ETC1.encodeImage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tolerans.notetoself.databinding.FragmentAddNoteBinding
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.util.Const.SELECT_PHOTO
import com.tolerans.notetoself.util.EnumNoteStatus
import com.tolerans.notetoself.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.lang.Exception
import java.util.zip.ZipEntry
import javax.inject.Inject


@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding : FragmentAddNoteBinding?=null
    private val binding: FragmentAddNoteBinding get() = _binding!!

    private val viewModel:AddNoteViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observerData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.data!=null){
            try{
                val imageUri: Uri = data.data!!
                val imageStream: InputStream? = requireActivity().contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)

                viewModel.addPicturetoNote(selectedImage)
            }catch (exp :Exception){

            }

        }
    }
    fun observerData() = with(binding){
        viewModel.noteImage.observe(viewLifecycleOwner, Observer { bitmap ->
            imgNote.setImageBitmap(bitmap)
        })
        viewModel.addImageViewVisibility.observe(viewLifecycleOwner,  { status ->
            imgNote.visibility = if (status) View.VISIBLE else View.GONE
        })
        viewModel.addImageButtonVisibility.observe(viewLifecycleOwner,  { status ->
            btnAddPhoto.visibility = if (status) View.VISIBLE else View.INVISIBLE
        })
        viewModel.removeImageButtonVisibility.observe(viewLifecycleOwner,  { status ->
            btnRemovePhoto.visibility = if (status) View.VISIBLE else View.INVISIBLE
        })

        viewModel.status.observe(viewLifecycleOwner,{ resource->

            when(resource){
                is Resource.Loading->{
                    progressBarAddNote.visibility = View.VISIBLE
                }
                is Resource.Failed->{
                    progressBarAddNote.visibility = View.GONE
                    Toast.makeText(requireContext(),resource.message,Toast.LENGTH_SHORT).show()
                }
                is Resource.Success->{
                    progressBarAddNote.visibility = View.GONE
                    Toast.makeText(requireContext(),"Başarılı",Toast.LENGTH_SHORT).show()
                }
            }

        })

    }



    fun initViews() = with(binding){

        btnAddPhoto.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent, SELECT_PHOTO)
        }
        btnRemovePhoto.setOnClickListener {
            viewModel.removePicturetoNote()
        }

        btnSaveNote.setOnClickListener {
            val note = Note(title = txtInputEdittextAddNoteTitle.text.toString(),description = txtInputEdittextAddNoteDescription.text.toString(),status = EnumNoteStatus.YAPILACAK.ordinal)
            viewModel.saveNote(note)
        }

    }




}