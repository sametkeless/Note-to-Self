package com.tolerans.notetoself.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolerans.notetoself.db.entities.User
import com.tolerans.notetoself.repository.UserRepository
import com.tolerans.notetoself.util.Resource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class RegisterViewModel @ViewModelInject constructor(val userRepository: UserRepository) :ViewModel(){

    private var _status = MutableLiveData<Resource<Boolean>>()
    val status :LiveData<Resource<Boolean>> = _status

    fun saveUser(user: User){
        viewModelScope.launch {
           userRepository.saveUser(user)
                   .onStart {
                       _status.postValue(Resource.Loading<Boolean>())
                   }
                   .catch { ex->
                       _status.postValue(Resource.Failed<Boolean>(ex.localizedMessage))
                   }
                   .collect { status->
                       if(status)
                           _status.postValue(Resource.Success<Boolean>(status))
                       else
                           _status.postValue(Resource.Failed<Boolean>("Bir Hata Oluştu"))
                   }
        }
    }

}