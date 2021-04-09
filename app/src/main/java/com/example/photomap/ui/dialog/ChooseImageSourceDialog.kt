package com.example.photomap.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.photomap.R
import com.example.photomap.util.Constants.MY_LOCATION_REQUEST_CODE_IMAGE_PICK

class ChooseImageSourceDialog(
    private val createPhotoClickListener: CreatePhotoClickListener,
    private val createPhotoRequestCode: Int,
    private val choosePhotoRequestCode: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sources = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.chose_photo_source))
            .setItems(sources) { dialog, which ->
                when (which) {
                    0 -> {
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                            .also {
                                parentFragment?.startActivityForResult(
                                    it,
                                    choosePhotoRequestCode
                                )
                            }
                        dialog.dismiss()
                    }
                    1 -> {
                        createPhotoClickListener.createPhotoFile(createPhotoRequestCode)
                        dialog.dismiss()
                    }
                }
            }
        return builder.create()
    }
}
