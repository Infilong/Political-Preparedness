package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var contxt: Context
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        //TODO: Add Constant for Location request
        private const val TURN_DEVICE_LOCATION_ON_REQUEST_CODE = 34
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 36
        private const val TAG = "RepresentativeFragment"
        private const val LOCATION_PERMISSION_INDEX = 0
    }

    //TODO: Declare ViewModel
    val viewModel = RepresentativeViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contxt = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Establish bindings
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        //TODO: Define and assign Representative adapter
        val adapter = RepresentativeListAdapter()

        //TODO: Populate Representative adapter
        binding.representatives.adapter = adapter

        //Spinner
        val stateList = resources.getStringArray(R.array.states)
        val spinnerArrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateList)
        binding.state.adapter = spinnerArrayAdapter
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        //TODO: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {

        }

        binding.buttonLocation.setOnClickListener {
            val currentAddress = viewModel.getAddressFromGeolocation()
            binding.addressLine1.setText(currentAddress.value?.line1)
            binding.addressLine2.setText(currentAddress.value?.line2)
            binding.city.setText(currentAddress.value?.city)
            binding.zip.setText(currentAddress.value?.zip)
            binding.state.setSelection(stateList.indexOf(currentAddress.value?.state))
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (grantResults.isEmpty() || grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED) {
            Snackbar.make(binding.root, R.string.location_request_denied, Snackbar.LENGTH_LONG)
                .setAction(R.string.settings) {
                    requestLocationPermissions()
                }
                .show()
        } else {
            getLocation()
        }
    }

    private fun requestLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            var permissionsArray = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            val requestCode = REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            requestPermissions(permissionsArray, requestCode)
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return (
                PackageManager.PERMISSION_GRANTED ==
                        ContextCompat.checkSelfPermission(
                            contxt,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(resolve: Boolean = true) {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        val locationRequest =
            LocationRequest.create().apply { priority = LocationRequest.PRIORITY_LOW_POWER }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(contxt)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        TURN_DEVICE_LOCATION_ON_REQUEST_CODE,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(binding.root, R.string.turn_on_location, Snackbar.LENGTH_LONG).show()
            }
        }

        locationSettingsResponseTask.addOnSuccessListener {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location ->
                    // Got last known location
                    viewModel.address.observe(this) { geoCodeLocation(location) }
                }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(contxt, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}