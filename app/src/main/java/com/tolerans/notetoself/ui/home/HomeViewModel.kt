package com.tolerans.notetoself.ui.home

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


class HomeViewModel @ViewModelInject constructor(val noteRepository: NoteRepository) :ViewModel(){

    private var _noteList: MutableLiveData<Resource<List<Note>>> = MutableLiveData()
    val noteList:LiveData<Resource<List<Note>>> = _noteList

    private var _btnFilterVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var btnFilterVisibility:LiveData<Boolean> = _btnFilterVisibility

    private var _layoutFilterVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var layoutFilterVisibility:LiveData<Boolean> = _layoutFilterVisibility

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes()
                    .onStart {
                        _noteList.postValue(Resource.Loading<List<Note>>())
                    }
                .catch { ex->
                    _noteList.postValue(Resource.Failed<List<Note>>(ex.localizedMessage))
                }
                .collect { notes->
                    if (notes.size>0)
                        _noteList.postValue(Resource.Success<List<Note>>(notes))
                    else
                        _noteList.postValue(Resource.Failed<List<Note>>("Hiç not yok"))
                }
        }
    }

    fun getFilterNoteList(position:Int){

        when(position){

            0 ->{
                viewModelScope.launch {
                    noteRepository.getAllNotes()
                            .onStart {
                                _noteList.postValue(Resource.Loading<List<Note>>())
                            }
                            .catch { ex->
                                _noteList.postValue(Resource.Failed<List<Note>>(ex.localizedMessage))
                            }
                            .collect { notes->
                                if (notes.size>0)
                                    _noteList.postValue(Resource.Success<List<Note>>(notes))
                                else
                                    _noteList.postValue(Resource.Failed<List<Note>>("Hiç not yok"))
                            }
                }
            }
            else ->{
                viewModelScope.launch {
                    noteRepository.getFilteredNotes(position-1)
                            .onStart {
                                _noteList.postValue(Resource.Loading<List<Note>>())
                            }
                            .catch { ex->
                                _noteList.postValue(Resource.Failed<List<Note>>(ex.localizedMessage))
                            }
                            .collect { notes->
                                if (notes.size>0)
                                    _noteList.postValue(Resource.Success<List<Note>>(notes))
                                else
                                    _noteList.postValue(Resource.Failed<List<Note>>("Hiç not yok"))
                            }
                }
            }

        }

    }

    fun filterButtonClick(){
        _btnFilterVisibility.postValue(false)
        _layoutFilterVisibility.postValue(true)

    }

    fun filterOkButtonClick(){
        _btnFilterVisibility.postValue(true)
        _layoutFilterVisibility.postValue(false)
    }

}