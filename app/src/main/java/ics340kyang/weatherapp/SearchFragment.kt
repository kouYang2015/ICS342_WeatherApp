package ics340kyang.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
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
            executeSearch()
        }
        binding.locReqButton.setOnClickListener {
            if (hasFineLocPermission()) {
                submitLastLocation()
            } else {
                askLocPermission()
            }
        }
        setNotifButtonText()
        binding.notifButton.setOnClickListener {
            if (hasFineLocPermission()) {
                notifOn = !notifOn
                setNotifButtonText()
                if (notifOn) {
                    startLocNotifService()
                } else {
                    stopLocNotifService()
                }
            } else {
                askLocNotifPermission()
            }
        }
    }

    private fun askLocNotifPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /*Permission not granted*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showLocationPermissionRationale()
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQLOCATIONCODE
                )
            }
        } else {
            // Permission is granted
            startLocNotifService()
        }
    }

    private fun executeSearch() {
        val zipInput = binding.searchInputTextBox.text.toString()
        viewModel.updateZipCode(zipInput)
        viewModel.submitZipButton()
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

    private fun stopLocNotifService() {
        requireActivity().stopService(Intent(context, LocNotifService::class.java))
        //Also turn off location permission?
    }

    private fun startLocNotifService() {
        requireActivity().startService(Intent(context, LocNotifService::class.java))
    }

    private fun hasFineLocPermission() =
        ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Used to switch between text when notification are on/off
     */
    private fun setNotifButtonText() {
        if (!notifOn) {
            binding.notifButton.text = "Turn on Notifications"
        } else {
            binding.notifButton.text = "Turn off Notifications"
        }
    }

    private fun askLocPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /*Permission not granted*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showLocationPermissionRationale()
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
            .setTitle("Location Permission Request")
            .setMessage("Location needed to find local weather conditions")
            .setNeutralButton("Ok") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
            .show()
    }

}