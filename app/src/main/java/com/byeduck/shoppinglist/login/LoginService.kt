package com.byeduck.shoppinglist.login

import com.google.firebase.auth.FirebaseAuth

class LoginService {

    companion object {
        private var loggedInUser: LoggedInUser? = null

        fun getUser(): LoggedInUser {
            val tmpUser = loggedInUser
            if (tmpUser != null) {
                return tmpUser
            }
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val restoredUser =
                LoggedInUser.fromFirebaseUser(
                    firebaseUser ?: throw RuntimeException("User not logged in!")
                )
            loggedInUser = restoredUser
            return restoredUser
        }

        fun logOut() {
            FirebaseAuth.getInstance().signOut()
            loggedInUser = null
        }
    }

}