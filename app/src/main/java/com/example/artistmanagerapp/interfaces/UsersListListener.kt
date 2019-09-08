package com.example.artistmanagerapp.interfaces

import android.view.View
import com.example.artistmanagerapp.models.User

interface UsersListListener {

    fun updateList(usersOutput : ArrayList<User>)
    fun controlAdminPanel(user : User, itemView : View)

}