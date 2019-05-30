package com.example.artistmanagerapp.models

data class RedeemCode (val codeString : String, var wasUsed : Boolean, var artistPageId : String, var generatedById : String, var redeemedById : String?)