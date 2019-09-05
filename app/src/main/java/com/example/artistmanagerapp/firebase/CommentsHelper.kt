package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.Comment
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.google.firebase.firestore.CollectionReference

object CommentsHelper : BaseActivity() {

    val fbC = FirebaseConstants

    enum class Option{
        INIT_COMMENT,
        NEW_COMMENT,
        COMMENTS_PARSED
    }

    // ************************* \WRITE FUNCTIONS *************************
    fun addComment (author : User?, commentContent : String?, currentDate : String?, pathToCommentsCollection : CollectionReference, commentsUpdater: CommentsUpdater?, option : Option?){
        pathToCommentsCollection.document().set(Comment (commentContent, author?.id, currentDate, author?.getDisplayName(), author?.firstName, author?.lastName)).addOnSuccessListener {
            commentsUpdater?.onCommentAdded(option)
        }.addOnFailureListener {

        }
    }

    /*fun addInitComment (task : Task, userId : String?, createdByDisplayName: String?, currentDate : String?, pathToCommentsCollection : CollectionReference, commentsUpdater: CommentsUpdater?, option : Option?){
        val commentContent = "Task created by $createdByDisplayName"
        pathToCommentsCollection.document().set(Comment (commentContent, userId, currentDate, createdByDisplayName)).addOnSuccessListener {
            commentsUpdater?.onCommentAdded(option)
        }.addOnFailureListener {

        }
    }*/

    // ************************* WRITE FUNCTIONS/ *************************

    // ************************* \READ FUNCTIONS *************************

    fun parseComments (pathToCommentsCollection: CollectionReference?, commentsUpdater: CommentsUpdater){
        var commentsOutput : ArrayList<Comment> = ArrayList()
        Log.d("PARSE COMMENTS", "METHOD ENTERED")

        pathToCommentsCollection?.get()
            ?.addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Log.d("FIREBASE - COMMENTS", "DOCUMENT EXISTS")

                    for (document in documents){
                        // Need to work on assignees list later
                        commentsOutput!!
                            .add(Comment(
                                document.get(fbC.COMMENT_CONTENT).toString(),
                                document.get(fbC.COMMENT_CREATED_BY).toString(),
                                document.get(fbC.COMMENT_DATE_CREATED).toString(),
                                document.get(fbC.CREATED_BY_DISPLAY_NAME).toString(),
                                document.get(fbC.AUTHOR_FIRST_NAME).toString(),
                                document.get(fbC.AUTHOR_LAST_NAME).toString()))
                        Log.d("FIREBASE - COMMENTS", document.get(fbC.COMMENT_CONTENT).toString())
                    }
                    commentsUpdater.onCommentsParsed(CommentsHelper.Option.COMMENTS_PARSED, commentsOutput)
                } else {
                    Log.d("FIREBASE - COMMENTS", "DOCUMENT IS EMPTY")
                }
            }?.addOnFailureListener {
                Log.d("FIREBASE - COMMENTS", "cos poszlo nie tak")
            }
    }


    // ************************* READ FUNCTIONS/ *************************

    interface CommentsUpdater {
        fun onCommentAdded(option : Option?)
        fun onCommentsParsed(option : Option?, commentsList : ArrayList<Comment>)
    }

}