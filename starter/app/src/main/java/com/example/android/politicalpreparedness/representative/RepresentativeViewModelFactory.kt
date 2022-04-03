package com.example.android.politicalpreparedness.representative

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeViewModelFactory(val context: Context, val representative: Representative) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModelFactory(context, representative) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}
