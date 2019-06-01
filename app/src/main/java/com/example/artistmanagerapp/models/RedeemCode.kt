package com.example.artistmanagerapp.models

class RedeemCode {

    var codeString : String? = null
    var wasUsed : Boolean? = false
    var artistPageId : String? = null
    var generatedById : String? = null
    var redeemedById : String? = null

    // RedeemCode (redeemCodeString, false, artistPageId, userId, null)

    constructor(mWasUsed: Boolean?, mRedeemedById: String?) {
        this.wasUsed = mWasUsed
        this.redeemedById = mRedeemedById
    }

    constructor(mCodeString: String?, mWasUsed: Boolean?, mArtistPageId: String?, mGeneratedById: String?, mRedeemedById: String?) {
        this.codeString = mCodeString
        this.wasUsed = mWasUsed
        this.artistPageId = mArtistPageId
        this.generatedById = mGeneratedById
        this.redeemedById = mRedeemedById
    }
}