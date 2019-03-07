package com.example.artistmanagerapp.models

import java.sql.Struct

class Artist (firstName : String, lastName : String, val instrument : String, role : String) : Person(firstName, lastName, role) {

}