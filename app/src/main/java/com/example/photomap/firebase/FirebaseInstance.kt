package com.example.photomap.firebase

import com.example.photomap.util.Constants.MAP_MARK_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseInstance {

    companion object {
        val auth = FirebaseAuth.getInstance()
        private val uid = auth.uid
        val fireStoreDB = Firebase.firestore.collection("$MAP_MARK_COLLECTION/$uid/marks")
        val fireBaseImageStorage = Firebase.storage.reference
    }
}
