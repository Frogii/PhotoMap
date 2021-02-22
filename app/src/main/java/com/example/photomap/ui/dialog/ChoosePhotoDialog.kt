package com.example.photomap.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.example.photomap.R
import kotlinx.android.synthetic.main.dialog_choose_photo.*

class ChoosePhotoDialog(
    context: Context,
    var dialogClickListener: DialogClickListener
) : AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_photo)

        this.setTitle("Photo")

        textViewGallery.setOnClickListener {
            dialogClickListener.chooseImage()
            dismiss()
        }

        textViewTakePhoto.setOnClickListener {
            dialogClickListener.takePhoto()
            dismiss()
        }
    }
}