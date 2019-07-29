package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.User

interface UsersListListener {

    fun updateList(usersOutput : ArrayList<User>)

}