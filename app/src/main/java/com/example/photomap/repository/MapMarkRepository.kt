package com.example.photomap.repository

import android.net.Uri
import com.example.photomap.firebase.FirebaseInstance
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.STORAGE_PATH
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class MapMarkRepository private constructor() {

    private object HOLDER {
        val INSTANCE = MapMarkRepository()
    }

    companion object {
        val instance: MapMarkRepository by lazy { HOLDER.INSTANCE }
    }

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

    suspend fun getAllMapMarks(category: String, filter: List<String>): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.whereIn(category, filter).get().await()
    }

    suspend fun getMapMark(mapMark: MapMark): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.whereEqualTo("name", mapMark.name).get().await()
    }

    suspend fun updateMapMark(mapMarkQuery: QuerySnapshot, newMapMarkMap: Map<String, Any>) {
        for (document in mapMarkQuery)
            FirebaseInstance.fireStoreDB.document(document.id).set(
                newMapMarkMap,
                SetOptions.merge()
            ).await()
    }
}