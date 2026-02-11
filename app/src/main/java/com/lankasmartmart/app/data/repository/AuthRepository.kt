package com.lankasmartmart.app.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.lankasmartmart.app.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    
    suspend fun signIn(email: String, password: String): Result<AuthResult>
    
    suspend fun signUp(email: String, password: String, name: String): Result<AuthResult>
    
    suspend fun signOut()
    
    suspend fun getUserProfile(userId: String): Result<User>
    
    suspend fun resetPassword(email: String): Result<Unit>
    
    suspend fun signInWithGoogle(idToken: String): Result<AuthResult>
}
