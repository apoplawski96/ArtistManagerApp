package com.example.artistmanagerapp.models

class ShareEpkCode {

    var codeString : String? = null
    //var wasUsed : Boolean? = false
    var connectedPageId : String? = null
    //var generatedById : String? = null
    //var redeemedById : String? = null


    constructor(mCodeString : String?, mConnectedPageId : String?) {
        this.codeString = mCodeString
        this.connectedPageId = mConnectedPageId
    }

}