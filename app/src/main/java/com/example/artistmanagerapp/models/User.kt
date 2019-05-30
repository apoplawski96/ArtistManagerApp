package com.example.artistmanagerapp.models

data class User (val userId : String){
    constructor (userId: String, pageRole : String) : this(userId)
    constructor (firstName: String, lastName: String, artistRole: String, pageRole:String) : this(firstName, pageRole)
    constructor (userId: String, firstName : String, lastName : String, artistRole : String, pageRole : String) : this(userId, pageRole)
}