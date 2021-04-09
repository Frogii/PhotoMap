package com.example.photomap.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.photomap.R
import com.example.photomap.util.Constants.DEFAULT_CATEGORY
import com.example.photomap.util.Constants.FRIENDS_CATEGORY
import com.example.photomap.util.Constants.NATURE_CATEGORY

class ChangeCategoryDialog(var changeCategoryClickListener: ChangeCategoryClickListener) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val categoryArray = arrayOf(FRIENDS_CATEGORY, NATURE_CATEGORY, DEFAULT_CATEGORY)
        return AlertDialog.Builder(context)
            .setTitle(getString(R.string.dialog_change_category_title))
            .setSingleChoiceItems(categoryArray, -1) { dialogInterface, i ->
                changeCategoryClickListener.changeCategory(categoryArray[i])
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_change_category_cancel)
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
    }
}