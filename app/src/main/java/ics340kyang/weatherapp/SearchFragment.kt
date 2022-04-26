package ics340kyang.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.FragmentSearchBinding
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val REQLOCATIONCODE = 10001
    val CHANNEL_ID = "channelID"
    private var notifOn = false

    @Inject
    lateinit var viewModel: SearchViewModel

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        binding = FragmentSearchBinding.bind(view)
        binding.toolbar.title = "Search"
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            }

        viewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.searchButton.isEnabled = enable
        }
        viewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.searchInputTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newZip = p0?.toString()
                newZip?.let { viewModel.updateZipCode(it) }
            }
        })

        binding.searchButton.setOnClickListener() {
            val zipInput = binding.searchInputTextBox.text.toString()
            viewModel.updateZipCode(zipInput)
            viewModel.submitZipButton()
            System.out.println(viewModel.showErrorDialog.value)
            val currentConditions = viewModel.currentConditionCall.value
            val action = currentConditions?.let { currentConditions ->
                SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                    currentConditions
                )
            }
            if (action != null && viewModel.showErrorDialog.value == false) {
                findNavController().navigate(action)
            }
        }
        binding.locReqButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                submitLastLocation()
            } else {
                askLocPermission()
            }
        }
        setNotifButtonText();
        binding.notifButton.setOnClickListener {
            notifOn = !notifOn
            //Ask for notifation permission, ask for location permission if not on
            setNotifButtonText();
        }
    }

    private fun setNotifButtonText() {
        if (!notifOn) {
            binding.notifButton.text = "Turn on Notifications"
        }
        else {
            binding.notifButton.text = "Turn off Notifications";
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val channelName = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun askLocPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /*Permission not granted*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                showLocationPermissionRationale()
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQLOCATIONCODE
                )
            }
        } else {
            // Permission is granted
            submitLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun submitLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.updateLocation(it.latitude, it.longitude)
            viewModel.submitLocationButton()
            val currentConditions = viewModel.currentConditionCall.value
            val action = currentConditions?.let { currentConditions ->
                SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                    currentConditions
                )
            }
            if (action != null && viewModel.showErrorDialog.value == false) {
                findNavController().navigate(action)
            }
        }
    }

    private fun showLocationPermissionRationale() {
        AlertDialog.Builder(this.activity)
            .setTitle("Rationale")
            .setMessage("Location needed to find local weather conditions")
            .setNeutralButton("Ok") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
            .show()
    }

}