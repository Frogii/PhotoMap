package com.example.photomap.repository

import android.content.Context
import android.net.Uri
import com.example.photomap.db.MapMarkLocalDatabase
import com.example.photomap.firebase.FirebaseInstance
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.MAP_MARK_FIELD_NAME
import com.example.photomap.util.Constants.ORDER_BY_DESCRIPTION
import com.example.photomap.util.Constants.STORAGE_PATH
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class MapMarkRepository(context: Context, private val database: MapMarkLocalDatabase) {

    companion object {
        @Volatile
        private var instance: MapMarkRepository? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createRepository(context).also {
                instance = it
            }
        }

        private fun createRepository(context: Context) =
            MapMarkRepository(context, MapMarkLocalDatabase.invoke(context))
    }

    suspend fun addMarkToDB(mapMark: MapMark) {
        database.getMapMarkDao().addMapMark(mapMark)
    }

    suspend fun getAllMarksFromDB(filter: MutableList<String>): List<MapMark> {
        return database.getMapMarkDao().getAllMapMarks(filter)
    }


    suspend fun uploadPhotoToFireStorage(imageFile: Uri, imageName: String) {
        FirebaseInstance.fireBaseImageStorage
            .child("${STORAGE_PATH}$imageName")
            .putFile(imageFile)
            .await()
    }

    suspend fun getImageUrlFromFireStorage(imageName: String): String {
        return FirebaseInstance.fireBaseImageStorage.child("$STORAGE_PATH$imageName")
            .downloadUrl.await().toString()
    }

    suspend fun uploadMarkToFirebase(mapMark: MapMark) {
        FirebaseInstance.fireStoreDB.add(mapMark)
    }

    suspend fun getAllMarksFromFirebase(category: String, filter: List<String>): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.whereIn(category, filter).get().await()
    }

    suspend fun getMarkFromFirebase(mapMark: MapMark): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.whereEqualTo(MAP_MARK_FIELD_NAME, mapMark.name).get()
            .await()
    }

    suspend fun updateMapMarkInFirebase(mapMarkQuery: QuerySnapshot, newMapMarkMap: Map<String, Any>) {
        for (document in mapMarkQuery)
            FirebaseInstance.fireStoreDB.document(document.id).set(
                newMapMarkMap,
                SetOptions.merge()
            ).await()
    }

    suspend fun searchMarksInFirebase(
        category: String,
        filter: List<String>,
        query: String
    ): QuerySnapshot {
        return FirebaseInstance.fireStoreDB.whereIn(category, filter).orderBy(ORDER_BY_DESCRIPTION)
            .startAt(query).endAt(query + "\uF8FF").get().await()
    }
}