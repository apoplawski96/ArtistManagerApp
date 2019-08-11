package com.example.artistmanagerapp.utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.example.artistmanagerapp.models.ArtistPage

class Communicator : ViewModel() {

    val message = MutableLiveData<Any>()
    val pageUpdateData = MutableLiveData<ArtistPage>()

    fun setMsgCommunicator(msg:String){
        message.setValue(msg)
    }

    fun setArtistPageInfoCommunicator(pageInfo : ArtistPage){
        pageUpdateData.value = pageInfo
    }
}