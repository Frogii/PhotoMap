package com.example.photomap.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.photomap.R
import com.example.photomap.util.Constants
import com.example.photomap.util.Constants.LONG_CLICK_REQUEST_CODE_IMAGE_PICK
import com.example.photomap.util.Constants.LONG_CLICK_REQUEST_CODE_TAKE_PHOTO
import com.example.photomap.util.Constants.MY_LOCATION_REQUEST_CODE_IMAGE_PICK
import com.example.photomap.util.Constants.MY_LOCATION_REQUEST_CODE_TAKE_PHOTO

class ChooseImageSourceDialog() : DialogFragment() {

    private var createPhotoClickListener: CreatePhotoClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createPhotoClickListener = parentFragment as CreatePhotoClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var createPhotoRequestCode = MY_LOCATION_REQUEST_CODE_TAKE_PHOTO
        var choosePhotoRequestCode = MY_LOCATION_REQUEST_CODE_IMAGE_PICK
        arguments?.let {
            if (it.getBoolean(CLICK)) {
                createPhotoRequestCode = LONG_CLICK_REQUEST_CODE_TAKE_PHOTO
                choosePhotoRequestCode = LONG_CLICK_REQUEST_CODE_IMAGE_PICK
            }
        }
        val sources = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.chose_photo_source))
            .setItems(sources) { dialog, which ->
                when (which) {
                    0 -> {
                        createPhotoClickListener?.getPhotoFromGallery(choosePhotoRequestCode)
                        dialog.dismiss()
                    }
                    1 -> {
                        createPhotoClickListener?.createPhotoFile(createPhotoRequestCode)
                        dialog.dismiss()
                    }
                }
            }
        return builder.create()
    }

    override fun onDetach() {
        super.onDetach()
        createPhotoClickListener = null
    }

    companion object {

        fun getInstance(
            longCLick: Boolean
        ): ChooseImageSourceDialog {
            val bundle = Bundle()
            bundle.putBoolean(CLICK, longCLick)
            val dialogFragment = ChooseImageSourceDialog()
            dialogFragment.arguments = bundle
            return dialogFragment
        }

        const val CLICK = "click"
    }

    interface CreatePhotoClickListener {

        fun createPhotoFile(requestCode: Int)

        fun getPhotoFromGallery(requestCode: Int)
    }
}
