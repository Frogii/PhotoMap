package com.example.photomap.repository

import android.net.Uri
import android.util.Log
import com.example.photomap.db.MapMarkLocalDatabase
import com.example.photomap.firebase.FirebaseInstance
import com.example.photomap.model.MapMark
import com.example.photomap.util.AppDateUtils
import com.example.photomap.util.Constants.MAP_MARK_FIELD_NAME
import com.example.photomap.util.Constants.ORDER_BY_DESCRIPTION
import com.example.photomap.util.Constants.STORAGE_PATH
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.*

class MapMarkRepository(private val database: MapMarkLocalDatabase) {

    suspend fun deleteMark(mapMark: MapMark) {
        FirebaseInstance.fireStoreDB.whereEqualTo(MAP_MARK_FIELD_NAME, mapMark.name).get().addOnSuccessListener {
            val snapshotList = it.documents
            for (document in snapshotList){
                FirebaseInstance.fireStoreDB.document(document.id).delete()
            }
        }
        FirebaseInstance.fireBaseImageStorage.child("$STORAGE_PATH${mapMark.name}").delete()
        database.getMapMarkDao().deleteMapMark(mapMark)
    }

    suspend fun uploadMark(
        imageFile: Uri,
        imageName: String,
        imgLat: Double,
        imgLng: Double
    ): MapMark {
        val markForDB = MapMark(
            name = imageName,
            url = imageFile.toString(),
            date = AppDateUtils.formatDate(
                Date(), AppDateUtils.longPhotoDatePattern
            ),
            imageLatitude = imgLat,
            imageLongitude = imgLng
        )
        addMarkToDB(markForDB)
            imageName.let {
                uploadPhotoToFireStorage(imageFile, imageName)
                val imageUrl = getImageUrlFromFireStorage(imageName)
                val mark = MapMark(
                    name = imageName, url = imageUrl, date = AppDateUtils.formatDate(
                        Date(), AppDateUtils.longPhotoDatePattern
                    ), imageLatitude = imgLat, imageLongitude = imgLng
                )
                uploadMarkToFirebase(mark)
                return mark
            }
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

    suspend fun updateMapMarkInFirebase(
        mapMarkQuery: QuerySnapshot,
        newMapMarkMap: Map<String, Any>
    ) {
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