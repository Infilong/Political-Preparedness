package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //TODO: Establish live data for representatives and address
    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: MutableLiveData<List<Representative>>
        get() = _representatives

    private var _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    //TODO: Create function to fetch representatives from API from a provided address
    suspend fun getLocalAddressRepresentatives(address: Address) {
        withContext(Dispatchers.IO) {
            try {
                _representatives.value = CivicsApi.retrofitService.getRepresentatives(address)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun getAddressFromGeolocation(): MutableLiveData<Address> {
        return _address
    }
    //TODO: Create function to get address from individual fields

}
