package com.byeduck.shoppinglist.login

import com.google.firebase.auth.FirebaseUser

class LoggedInUser(
    val name: String,
    val id: String
) {
    companion object {
        fun fromFirebaseUser(firebaseUser: FirebaseUser) = LoggedInUser(
            extractUserNameFromMail(firebaseUser.email), firebaseUser.uid
        )

        private fun extractUserNameFromMail(email: String?) = email?.takeWhile { it != '@' } ?: ""
    }
}