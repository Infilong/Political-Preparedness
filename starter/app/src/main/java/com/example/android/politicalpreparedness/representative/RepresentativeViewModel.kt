package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.representatives
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    //TODO: Establish live data for representatives and address

    var representatives = MutableLiveData<List<Representative>>()

    var address = MutableLiveData<Address>()

    //TODO: Create function to fetch representatives from API from a provided address
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

    suspend fun getLocalAddressRepresentatives(address: Address) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    CivicsApi.retrofitService.getRepresentatives(address.toString())
                representatives.value = response.body()?.representatives
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
