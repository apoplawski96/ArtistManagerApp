package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.Person

interface UserDataPresenter {
    fun showUserData(userData : Person)
}