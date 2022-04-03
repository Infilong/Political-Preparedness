package com.example.android.politicalpreparedness.representative.model

import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse

data class Representative(
    val official: Official,
    val office: Office
)

