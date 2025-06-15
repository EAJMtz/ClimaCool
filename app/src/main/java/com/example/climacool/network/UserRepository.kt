package com.example.climacool.network

import com.example.climacool.core.ResultWrapper
import com.example.climacool.core.safeCall
import com.example.climacool.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){
    private val userCollection = firestore.collection("Users")

    suspend fun login(email: String, password: String): ResultWrapper<FirebaseUser> = safeCall {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user ?: throw Exception("usuario no encontrado")
    }

    suspend fun requestSignUp(email: String, password: String): ResultWrapper<FirebaseUser> = safeCall {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user ?: throw Exception("No se pudo crear el usuario")
    }

    suspend fun createUser(user: User): ResultWrapper<Void> = safeCall {
        if (user.id.isEmpty()) throw Exception("Error: userId está vacío")
        userCollection.document(user.id).set(user).await()
    }

    suspend fun getUser(id: String): ResultWrapper<User> = safeCall {
        val snapshot = userCollection.document(id).get().await()
        snapshot.toObject(User::class.java)?: throw Exception("Usuario no encontrado")
    }

    suspend fun updateUser(user: User): ResultWrapper<Void> = safeCall {
        userCollection.document(user.id).set(user).await()
    }

    suspend fun deleteUser(id: String): ResultWrapper<Void> = safeCall {
        userCollection.document(id).delete().await()
    }
}