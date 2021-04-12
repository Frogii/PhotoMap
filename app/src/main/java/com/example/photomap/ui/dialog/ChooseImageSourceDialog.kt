package com.example.photomap.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.photomap.R

class ChooseImageSourceDialog() : DialogFragment() {

    private var createPhotoClickListener: CreatePhotoClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createPhotoClickListener = parentFragment as CreatePhotoClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val createPhotoRequestCode = arguments?.getInt(CREATE_PHOTO_CODE)
        val choosePhotoRequestCode = arguments?.getInt(CHOOSE_PHOTO_CODE)
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
                                if (choosePhotoRequestCode != null) {
                                    parentFragment?.startActivityForResult(
                                        it,
                                        choosePhotoRequestCode
                                    )
                                }
                            }
                        dialog.dismiss()
                    }
                    1 -> {
                        if (createPhotoRequestCode != null) {
                            createPhotoClickListener?.createPhotoFile(createPhotoRequestCode)
                        }
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
            createPhotoRequestCode: Int,
            choosePhotoRequestCode: Int
        ): ChooseImageSourceDialog {
            val bundle = Bundle()
            bundle.putInt(CREATE_PHOTO_CODE, createPhotoRequestCode)
            bundle.putInt(CHOOSE_PHOTO_CODE, choosePhotoRequestCode)
            val dialogFragment = ChooseImageSourceDialog()
            dialogFragment.arguments = bundle
            return dialogFragment
        }

        const val CREATE_PHOTO_CODE = "createPhotoCode"
        const val CHOOSE_PHOTO_CODE = "choosePhotoCode"
    }
}
