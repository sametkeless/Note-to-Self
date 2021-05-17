package com.tolerans.notetoself.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolerans.notetoself.db.entities.User
import com.tolerans.notetoself.repository.UserRepository
import com.tolerans.notetoself.util.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class   LoginViewModel @ViewModelInject constructor(val userRepository: UserRepository):ViewModel(){

    private var _user = MutableLiveData<Resource<User>>()
    val user:LiveData<Resource<User>> = _user


    fun login(loginUser: User){


        viewModelScope.launch {

            if(loginUser.username.isEmpty() || loginUser.password.isEmpty())
                _user.postValue(Resource.Failed<User>("Lütfen kullanıcı adı ve şifre alanlarını doldurunuz"))
            else {
                userRepository.login(loginUser)
                        .onStart {
                            _user.postValue(Resource.Loading<User>())
                        }
                        .catch { e ->
                            _user.postValue(Resource.Failed<User>(e.localizedMessage))
                        }
                        .collect { user ->
                            if (user.userID > 0)
                                _user.postValue(Resource.Success<User>(user))
                            else
                                _user.postValue(Resource.Failed<User>("Kullanıcı adı veya şifre hatalı"))
                        }
            }
        }

    }
}

