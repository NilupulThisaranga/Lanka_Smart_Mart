package com.lankasmartmart.app.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.lankasmartmart.app.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    private val TIMEOUT = 15000L // 15 seconds timeout

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signIn(email: String, password: String): Result<AuthResult> {
        return try {
            kotlinx.coroutines.withTimeout(TIMEOUT) {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<AuthResult> {
        return try {
            kotlinx.coroutines.withTimeout(TIMEOUT) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                
                if (firebaseUser != null) {
                    // Create user profile in Firestore
                    val user = User(
                        userId = firebaseUser.uid,
                        name = name,
                        email = email
                    )
                    // We don't want to fail the whole signup if firestore fails, but we should try
                    try {
                        firestore.collection("users").document(firebaseUser.uid).set(user).await()
                    } catch (e: Exception) {
                        // Log failure but allow auth success
                    }
                }
                
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            kotlinx.coroutines.withTimeout(TIMEOUT) {
                val document = firestore.collection("users").document(userId).get().await()
                val user = document.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthResult> {
        return try {
            kotlinx.coroutines.withTimeout(TIMEOUT) {
                val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val firebaseUser = result.user

                if (firebaseUser != null) {
                    val userRef = firestore.collection("users").document(firebaseUser.uid)
                    
                    try {
                        val userDoc = userRef.get().await()

                        if (!userDoc.exists()) {
                            val user = User(
                                userId = firebaseUser.uid,
                                name = firebaseUser.displayName ?: "User",
                                email = firebaseUser.email ?: ""
                            )
                            userRef.set(user).await()
                        }
                    } catch (e: Exception) {
                        // Proceed even if Firestore profile check fails
                    }
                }
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
