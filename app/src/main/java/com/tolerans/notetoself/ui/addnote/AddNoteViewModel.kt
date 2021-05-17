package com.tolerans.notetoself.ui.addnote

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.repository.NoteRepository
import com.tolerans.notetoself.util.AppExtensions.toBase64String
import com.tolerans.notetoself.util.EnumNoteStatus
import com.tolerans.notetoself.util.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddNoteViewModel @ViewModelInject constructor(val noteRepository: NoteRepository) : ViewModel(){

    private var _noteImage :MutableLiveData<Bitmap> = MutableLiveData()
    val noteImage:LiveData<Bitmap> = _noteImage

    private var _addImageButtonVisibility:MutableLiveData<Boolean> = MutableLiveData()
    val addImageButtonVisibility:LiveData<Boolean> = _addImageButtonVisibility

    private var _removeImageButtonVisibility:MutableLiveData<Boolean> = MutableLiveData()
    val removeImageButtonVisibility:LiveData<Boolean> = _removeImageButtonVisibility

    private var _addImageViewVisibility:MutableLiveData<Boolean> = MutableLiveData()
    val addImageViewVisibility:LiveData<Boolean> = _addImageViewVisibility

    private var _status :MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val status:LiveData<Resource<Boolean>> = _status

    fun addPicturetoNote(bitmap: Bitmap){
        bitmap?.let {
            _noteImage.postValue(bitmap)
            _addImageButtonVisibility.postValue(false)
            _removeImageButtonVisibility.postValue(true)
            _addImageViewVisibility.postValue(true)
        } ?: run {
            _noteImage.postValue(null)
            _addImageButtonVisibility.postValue(true)
            _removeImageButtonVisibility.postValue(false)
            _addImageViewVisibility.postValue(false)
        }
    }

    fun removePicturetoNote(){
        _noteImage.postValue(null)
        _addImageButtonVisibility.postValue(true)
        _removeImageButtonVisibility.postValue(false)
        _addImageViewVisibility.postValue(false)
    }

    fun saveNote(note:Note){
        viewModelScope.launch {
                _noteImage.value?.let { bitmap->
                    note.noteImage = bitmap.toBase64String()
                }
               noteRepository.saveNote(note)
                    .onStart {
                        _status.postValue(Resource.Loading<Boolean>())
                    }
                    .catch {exp->
                        _status.postValue(Resource.Failed<Boolean>(exp.localizedMessage))
                    }
                    .collect {status->
                        if(status){
                            _status.postValue(Resource.Success<Boolean>(status))
                        }else{
                            _status.postValue(Resource.Failed<Boolean>("Bir hata oluştu"))
                        }
                    }


        }
    }

}