package com.example.android.politicalpreparedness.election

import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.TAG
import com.example.android.politicalpreparedness.network.models.Election
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import retrofit2.HttpException

class ElectionsRepository(private val database: ElectionDatabase) {
    var electionsList: LiveData<List<Election>> = database.electionDao.getElections()
    var savedElectionsList: LiveData<List<Election>> = database.electionDao.getFollowedElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                val electionResponse = CivicsApi.retrofitService.getElections()
                val elections = electionResponse.body()?.elections
                if (elections != null) {
                    database.electionDao.insertAll(*elections.toTypedArray())
                }else{}
            } catch (e: IOException) {
                Log.e(TAG, "IOException, please check your internet connection")
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, please check your internet connection")
            }
        }
    }
}

