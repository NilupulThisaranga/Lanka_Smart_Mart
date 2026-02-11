package com.lankasmartmart.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lankasmartmart.app.data.repository.AuthRepository
import com.lankasmartmart.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val loginState: StateFlow<Resource<Boolean>> = _loginState.asStateFlow()

    private val _signupState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val signupState: StateFlow<Resource<Boolean>> = _signupState.asStateFlow()

    private val _currentUserState = MutableStateFlow(repository.currentUser != null)
    val currentUserState: StateFlow<Boolean> = _currentUserState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Please enter email and password")
            return
        }

        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            val result = repository.signIn(email, password)
            if (result.isSuccess) {
                _loginState.value = Resource.Success(true)
                _currentUserState.value = true
            } else {
                _loginState.value = Resource.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun signup(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _signupState.value = Resource.Error("Please fill all fields")
            return
        }

        viewModelScope.launch {
            _signupState.value = Resource.Loading()
            val result = repository.signUp(email, password, name)
            if (result.isSuccess) {
                _signupState.value = Resource.Success(true)
                _currentUserState.value = true
            } else {
                _signupState.value = Resource.Error(result.exceptionOrNull()?.message ?: "Signup failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.signOut()
            _currentUserState.value = false
        }
    }
    
    fun resetState() {
        _loginState.value = Resource.Success(false)
        _signupState.value = Resource.Success(false)
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            val result = repository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                _loginState.value = Resource.Success(true)
                _currentUserState.value = true
            } else {
                _loginState.value = Resource.Error(result.exceptionOrNull()?.message ?: "Google Sign-In failed")
            }
        }
    }
}
