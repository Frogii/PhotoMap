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
        val longCLick = arguments?.getBoolean(
            LONG_CLICK)
        val sources = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.chose_photo_source))
            .setItems(sources) { dialog, which ->
                longCLick?.let {
                    when (which) {
                        0 -> {
                            createPhotoClickListener?.getPhotoFromGallery(it)
                            dialog.dismiss()
                        }
                        1 -> {
                            createPhotoClickListener?.createPhotoFile(it)
                            dialog.dismiss()
                        }
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
            bundle.putBoolean(LONG_CLICK, longCLick)
            val dialogFragment = ChooseImageSourceDialog()
            dialogFragment.arguments = bundle
            return dialogFragment
        }

        private const val LONG_CLICK = "longClick"
    }

    interface CreatePhotoClickListener {

        fun createPhotoFile(longClick: Boolean)

        fun getPhotoFromGallery(longClick: Boolean)
    }
}
