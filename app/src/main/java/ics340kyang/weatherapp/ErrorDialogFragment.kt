package ics340kyang.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Invalid US zip code.")
            .setPositiveButton(R.string.ok, null)
            .create()

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}