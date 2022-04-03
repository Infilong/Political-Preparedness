package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}