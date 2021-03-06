package com.example.artistmanagerapp.constants

object FirebaseConstants {

    // FIELDS NAMES - USER
    val USERS_COLLECTION_NAME = "users"
    val ID = "id"
    val EMAIL = "emailAddress"
    val FIRST_NAME = "firstName"
    val LAST_NAME = "lastName"
    val PROFILE_COMPLETION_STATUS = "profileCompletionStatus"
    val CURRENT_ARTIST_PAGE = "currentArtistPageId"
    val ARTIST_ROLE = "artistRole"
    val PAGE_ROLE = "pageRole"
    val ROLE_CATEGORY = "roleCategory"
    val TASKS_COMPLETED = "tasksCompleted"
    val EVENTS_ATTENDED = "eventsAttended"
    val EVENTS_CREATED = "eventsCreated"
    val ASSIGNMENTS_PENDING = "assignmentsPending"

    // FIELDS VALUES - USER
    val V_PROFILE_STATUS_STARTED = "started"
    val V_PROFILE_STATUS_COMPLETED = "completed"

    // FIELD NAMES - ARTIST PAGES
    val ARTIST_PAGES_COLLECTION_NAME = "artistPages"
    val ARTIST_NAME = "artistName"
    val ARTIST_TASKS = "tasks"
    val ARTIST_PAGE_ID = "artistPageId"
    val ARTIST_PAGE_ADMIN_ID = "artistPageAdminId"
    val ARTIST_BIO = "biography"
    val ARTIST_GENRE = "genre"
    val ARTIST_FB = "facebookLink"
    val ARTIST_IG = "instagramLink"
    val ARTIST_CONTACT = "contact"
    val ARTIST_SHARE_CODE = "epkShareCode"
    val ARTIST_NEW_TASKS = "newTasks"
    val ARTIST_UPCOMING_EVENTS = "upcomingEvents"
    val ARTIST_DATE_CREATED = "dateCreated"
    val ARTIST_TIME_CREATED = "timeCreated"
    val ARTIST_CREATED_BY_ID = "createdById"
    val ARTIST_CREATED_BY_DISPLAY_NAME = "createdByDisplayName"
    val ARTIST_CATEGORY = "pageCategory"
    val ARTIST_MEMBERS_AND_ROLES = "membersAndRoles"
    val PAGE_ADMINS = "pageAdmins"


    // FIELDS NAMES - COMMENTS
    val COMMENT_CONTENT = "content"
    val COMMENT_CREATED_BY = "createdBy"
    val COMMENT_DATE_CREATED = "dateCreated"
    val CREATED_BY_DISPLAY_NAME = "createdByDisplayName"
    val AUTHOR_FIRST_NAME = "authorFirstName"
    val AUTHOR_LAST_NAME = "authorLastName"

}