package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.constants.RealTimeUpdateType

interface FirebaseRealTimeUpdatesPresenter {
    fun presentNewData(data : Any?, updateType : RealTimeUpdateType)
}