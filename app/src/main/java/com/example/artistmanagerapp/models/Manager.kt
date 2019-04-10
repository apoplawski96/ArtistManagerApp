package com.example.artistmanagerapp.models

class Manager (firstName : String, lastName : String, val musicProject: MusicProject, artistRole : String, pageRole : String) : Person(firstName, lastName, artistRole, pageRole){

}