package com.byeduck.shoppinglist.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoggedInUser(
    var userName: String,
    var id: String
) {

    companion object {
        private var loggedInUser: LoggedInUser? = null

        fun getUser(): LoggedInUser {
            val tmpUser = loggedInUser
            if (tmpUser != null) {
                return tmpUser
            }
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val restoredUser =
                fromFirebaseUser(firebaseUser ?: throw RuntimeException("User not logged in!"))
            loggedInUser = restoredUser
            return restoredUser
        }

        private fun extractUserNameFromMail(email: String?) = email?.takeWhile { it != '@' } ?: ""

        private fun fromFirebaseUser(firebaseUser: FirebaseUser) = LoggedInUser(
            extractUserNameFromMail(firebaseUser.email), firebaseUser.uid
        )
    }

}