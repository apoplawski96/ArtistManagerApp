package com.example.artistmanagerapp.firebase

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.Comment
import com.example.artistmanagerapp.models.Task
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
    fun addComment (comment : Comment, pathToCommentsCollection : CollectionReference, commentsUpdater: commentsUpdater, option : Option){
        pathToCommentsCollection.document().set(comment).addOnSuccessListener {
            commentsUpdater.onCommentAdded(option)
        }.addOnFailureListener {

        }
    }

    // ************************* WRITE FUNCTIONS/ *************************

    // ************************* \READ FUNCTIONS *************************

    fun parseTasks (pathToCommentsCollection: CollectionReference, commentsUpdater: commentsUpdater){
        var commentsOutput : ArrayList<Comment> = ArrayList()

        pathToCommentsCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        commentsOutput!!
                            .add(Comment(
                                document.get(fbC.COMMENT_CONTENT).toString(),
                                document.get(fbC.COMMENT_CREATED_BY).toString(),
                                document.get(fbC.COMMENT_DATE_CREATED).toString()))
                    }
                    commentsUpdater.onCommentsParsed(CommentsHelper.Option.COMMENTS_PARSED, commentsOutput)
                } else {
                }
            }
    }


    // ************************* READ FUNCTIONS/ *************************

    interface commentsUpdater {
        fun onCommentAdded(option : Option)
        fun onCommentsParsed(option : Option, commentsList : ArrayList<Comment>)
    }

}