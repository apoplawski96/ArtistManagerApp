package com.example.artistmanagerapp.interfaces

interface UserInterfaceUpdater {
    fun updateUI(option : String)
    fun initializeUI()
    fun showProgress()
    fun hideProgress()
}