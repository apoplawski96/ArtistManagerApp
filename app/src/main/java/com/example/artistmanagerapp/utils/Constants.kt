package com.example.artistmanagerapp.utils

object Constants {

    // Roles
    val PAGE_ADMIN : String = "admin"
    val MEMBER : String = "member"

    // Numeric
    val REDEEM_CODE_LENGTH : Int = 7
    val EPK_SHARE_CODE_LENGHT : Int = 5
    val GALLERY = 1
    val OPTION_ARTIST_AVATAR = 0
    val OPTION_USER_AVATAR = 1

    // Strings
    val ALPHABET : String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    // Options
    val ARTIST_PAGE_CREATED : String = "ARTIST_PAGE_CREATED"
    val CODE_SUCCESSFULLY_REDEEMED : String = "CODE_SUCCESSFULLY_REDEEMED"
    val USER_ADDED_TO_DB : String = "USER_ADDED_TO_DB"
    val ARTIST_PAGE_SELECTED : String = "ARTIST_PAGE_SELECTED"
    val CURRENT_ARTIST_PAGE_REMOVED : String = "CURRENT_ARTIST_PAGE_REMOVED"
    val IMAGE_SUCCESSFULLY_UPLOADED : String = "IMAGE_SUCCESSFULLY_UPLOADED"
    val CURRENT_PAGE_NOT_NULL = "CURRENT_PAGE_NOT_NULL"

    // Options - TaskManager ActionToolbar activation
    val TASKS_ON_LONG_CLICKED : String = "TASKS_ON_LONG_CLICKED"
    val CALENDAR_ON_DATE_CHANGED : String = "CALENDAR_ON_DATE_CHANGED"

    // FIREBASE - TASK OBJECT FIELDS NAMES
    val FB_TASK_ISCOMPLETED : String = "completed"

    // ARTIST PAGE BUNDLE FIELD NAMES
    val PAGE_ID_BUNDLE : String = "PAGE_ID"
    val ARTIST_NAME_BUNDLE : String = "ARTIST_NAME"
    val EPK_SHARE_CODE_BUNDLE : String = "EPK_SHARE_CODE"
    val EPK_SHARED_PAGE_ID : String = "EPK_SHARED_PAGE_ID"

    // USER BUNDLE FIELD NAMES
    val USER_ID_BUNDLE : String = "USER_ID"
    val FIRST_NAME_BUNDLE : String = "FIRST_NAME"
    val LAST_NAME_BUNDLE : String = "LAST_NAME"
    val PAGE_ROLE_BUNDLE : String = "PAGE_ROLE"
    val CURRENT_PAGE_BUNDLE : String = "CURRENT_PAGE"

    // USER PROFILE EDIT/CREATE - MODES
    val MODE_KEY : String = "ACTIVITY_MODE"
    val USER_PROFILE_CREATE_MODE : String = "CREATE_MODE"
    val USER_PROFILE_EDIT_MODE : String = "EDIT_MODE"

}