package com.example.artistmanagerapp.models

import java.sql.Struct

class Artist (firstName : String, lastName : String, val instrument : String, artistRole : String, pageRole : String) : Person(firstName, lastName, artistRole, pageRole) {

}