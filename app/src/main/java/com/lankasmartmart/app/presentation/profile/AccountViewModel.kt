package com.lankasmartmart.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lankasmartmart.app.data.model.User
import com.lankasmartmart.app.data.repository.AuthRepository
import com.lankasmartmart.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userState: StateFlow<Resource<User>> = _userState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val user = authRepository.currentUser
            if (user != null) {
                val result = authRepository.getUserProfile(user.uid)
                _userState.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull()!!)
                } else {
                    Resource.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } else {
                _userState.value = Resource.Error("User not logged in")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
