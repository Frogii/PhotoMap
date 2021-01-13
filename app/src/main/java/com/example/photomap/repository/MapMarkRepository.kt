package com.example.photomap.repository

import android.net.Uri
import com.example.photomap.firebase.FirebaseInstance
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.STORAGE_PATH
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class MapMarkRepository {

    suspend fun uploadPhoto(imageFile: Uri, imageName: String) {
        FirebaseInstance.fireBaseImageStorage
            .child("${STORAGE_PATH}$imageName")
            .putFile(imageFile)
            .await()
    }

    suspend fun getImageUrl(imageName: String): String {
        return FirebaseInstance.fireBaseImageStorage.child("$STORAGE_PATH$imageName")
            .downloadUrl.await().toString()
    }

    suspend fun uploadMapMark(mapMark: MapMark) {
        FirebaseInstance.fireStoreDB.add(mapMark)
    }

    suspend fun getAllMapMarks(): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.get().await()
    }
}