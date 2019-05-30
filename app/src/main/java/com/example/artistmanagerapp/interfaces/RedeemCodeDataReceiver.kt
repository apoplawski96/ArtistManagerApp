package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.RedeemCode

interface RedeemCodeDataReceiver {
    fun receiveCodeData(redeemCode: RedeemCode?)
    fun receiveCodesList(codesList: ArrayList<RedeemCode>)
}