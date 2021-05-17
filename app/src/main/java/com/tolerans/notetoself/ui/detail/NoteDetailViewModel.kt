package com.tolerans.notetoself.ui.detail

import android.graphics.Bitmap
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.repository.NoteRepository
import com.tolerans.notetoself.util.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NoteDetailViewModel @ViewModelInject constructor(val noteRepository: NoteRepository) :ViewModel() {

    private var _noteDetail :MutableLiveData<Resource<Note>> = MutableLiveData()
    val noteDetail :LiveData<Resource<Note>> = _noteDetail

    private var _noteImage :MutableLiveData<Bitmap> = MutableLiveData()
    val noteImage:LiveData<Bitmap> = _noteImage

    private var _updateStatus :MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updateStatus :LiveData<Resource<Boolean>> = _updateStatus

    private var _deleteStatus :MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val deleteStatus :LiveData<Resource<Boolean>> = _deleteStatus


    private var _btnDeleteVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnDeleteVisibility :LiveData<Boolean> = _btnDeleteVisibility

    private var _btnEditVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnEditVisibility :LiveData<Boolean> = _btnEditVisibility

    private var _btnCancelVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnCancelVisibility :LiveData<Boolean> = _btnCancelVisibility

    private var _btnCompleteVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnCompleteVisibility :LiveData<Boolean> = _btnCompleteVisibility

    private var _txtTitleVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val txtTitleVisibility:LiveData<Boolean> = _txtTitleVisibility
    private var _txtDescriptionVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val txtDescriptionVisibility:LiveData<Boolean> = _txtDescriptionVisibility
    private var _edtTitleVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val edtTitleVisibility:LiveData<Boolean> = _edtTitleVisibility
    private var _edtDescriptionVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val edtDescriptionVisibility:LiveData<Boolean> = _edtDescriptionVisibility

    private var _radioGroupEnabled :MutableLiveData<Boolean> = MutableLiveData()
    val radioGroupEnabled:LiveData<Boolean> = _radioGroupEnabled

    private var _btnAddPhotoVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnAddPhotoVisibility:LiveData<Boolean> = _btnAddPhotoVisibility

    private var _btnRemovePhotoVisibility :MutableLiveData<Boolean> = MutableLiveData()
    val btnRemovePhotoVisibility:LiveData<Boolean> = _btnRemovePhotoVisibility

    fun getDetail(noteID:Int){
        viewModelScope.launch {
            noteRepository.getNoteDetail(noteID)
                .onStart {
                    _noteDetail.postValue(Resource.Loading<Note>())
                }
                .catch {ex->
                    _noteDetail.postValue(Resource.Failed<Note>(ex.localizedMessage))
                }
                .collect {note->
                    if(note!=null && note.noteID>0)
                        _noteDetail.postValue(Resource.Success<Note>(note))
                    else
                        _noteDetail.postValue(Resource.Failed<Note>("Not bulunamadı."))
                }
        }
    }

    fun completeNote(note:Note){
        viewModelScope.launch {
            noteRepository.updateNote(note)
                .onStart {
                    _updateStatus.postValue(Resource.Loading<Boolean>())
                }
                .catch {ex->
                    _updateStatus.postValue(Resource.Failed<Boolean>(ex.localizedMessage))
                }
                .collect {status->
                    _btnCancelVisibility.postValue(false)
                    _btnCompleteVisibility.postValue(false)
                    _btnDeleteVisibility.postValue(true)
                    _btnEditVisibility.postValue(true)
                    _txtTitleVisibility.postValue(true)
                    _txtDescriptionVisibility.postValue(true)
                    _edtTitleVisibility.postValue(false)
                    _edtDescriptionVisibility.postValue(false)
                    _radioGroupEnabled.postValue(false)

                    if(status)
                        _updateStatus.postValue(Resource.Success<Boolean>(true))
                    else
                        _updateStatus.postValue(Resource.Failed<Boolean>("Bir hata oluştu"))
                }
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch {
            _btnCancelVisibility.postValue(true)
            _btnCompleteVisibility.postValue(true)
            _btnDeleteVisibility.postValue(false)
            _btnEditVisibility.postValue(false)
            _txtTitleVisibility.postValue(false)
            _txtDescriptionVisibility.postValue(false)
            _edtTitleVisibility.postValue(true)
            _edtDescriptionVisibility.postValue(true)
            _radioGroupEnabled.postValue(true)
            note.noteImage?.let {
                _btnRemovePhotoVisibility.postValue(true)
                _btnAddPhotoVisibility.postValue(false)
            }?: run{
                _btnAddPhotoVisibility.postValue(true)
                _btnRemovePhotoVisibility.postValue(false)
            }
        }
    }

    fun cancelNote(){
        viewModelScope.launch {
            _btnCancelVisibility.postValue(false)
            _btnCompleteVisibility.postValue(false)
            _btnDeleteVisibility.postValue(true)
            _btnEditVisibility.postValue(true)
            _txtTitleVisibility.postValue(true)
            _txtDescriptionVisibility.postValue(true)
            _edtTitleVisibility.postValue(false)
            _edtDescriptionVisibility.postValue(false)
            _radioGroupEnabled.postValue(false)

            _btnRemovePhotoVisibility.postValue(false)
            _btnAddPhotoVisibility.postValue(false)
        }
    }

    fun deleteNote(note:Note){
        viewModelScope.launch {
            viewModelScope.launch {
                noteRepository.deleteNote(note)
                        .onStart {
                            _deleteStatus.postValue(Resource.Loading<Boolean>())
                        }
                        .catch {ex->
                            _deleteStatus.postValue(Resource.Failed<Boolean>(ex.localizedMessage))
                        }
                        .collect {status->
                            _btnCancelVisibility.postValue(true)
                            _btnCompleteVisibility.postValue(true)
                            _btnDeleteVisibility.postValue(false)
                            _btnEditVisibility.postValue(false)
                            _txtTitleVisibility.postValue(true)
                            _txtDescriptionVisibility.postValue(true)
                            _edtTitleVisibility.postValue(false)
                            _edtDescriptionVisibility.postValue(false)
                            _radioGroupEnabled.postValue(false)
                            _btnRemovePhotoVisibility.postValue(false)
                            _btnAddPhotoVisibility.postValue(false)
                            if(status)
                                _deleteStatus.postValue(Resource.Success<Boolean>(true))
                            else
                                _deleteStatus.postValue(Resource.Failed<Boolean>("Bir hata oluştu"))
                        }
            }
        }
    }

    fun removePhoto(){
        viewModelScope.launch {
            _noteImage.postValue(null)
            _btnRemovePhotoVisibility.postValue(false)
            _btnAddPhotoVisibility.postValue(true)
        }
    }


    fun addPicturetoNote(bitmap: Bitmap){
        bitmap?.let {
            _noteImage.postValue(bitmap)
            _btnRemovePhotoVisibility.postValue(true)
            _btnAddPhotoVisibility.postValue(false)

        } ?: run {
            _noteImage.postValue(null)
            _btnRemovePhotoVisibility.postValue(false)
            _btnAddPhotoVisibility.postValue(true)
        }
    }

}