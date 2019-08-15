package com.example.artistmanagerapp.interfaces

interface UserInterfaceUpdater {
    fun updateUI(option : String, data : Any?)
    fun initializeUI()
    fun showProgress()
    fun hideProgress()
}