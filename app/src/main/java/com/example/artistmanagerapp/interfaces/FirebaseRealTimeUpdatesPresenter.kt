package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.enums.RealTimeUpdateType

interface FirebaseRealTimeUpdatesPresenter {
    fun presentNewData(data : Any?, updateType : RealTimeUpdateType)
}