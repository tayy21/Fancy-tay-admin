package com.example.fancy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    // Declare the variables
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    // onCreate() is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the variables
        // findViewById() is used to get the views from the layout resource file.
        emailEditText = findViewById(R.id.loginTextEmailAddress)
        passwordEditText = findViewById(R.id.loginTextPassword)
        loginButton = findViewById(R.id.loginButton)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set a click listener on the login button
        loginButton.setOnClickListener {
            // Get the email and password from the input fields
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if the email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {


                // If not an admin, proceed with regular login
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this, "Authentication Passed!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Check if the email contains "@admin"
                            if (email.contains("@admin")) {
                                // Redirect to the admin page
                                val adminIntent = Intent(this, AdminDashboardActivity::class.java)
                                startActivity(adminIntent)
                            }
                        } else {
                            // Login failed, display an error message to the user.
                            Toast.makeText(
                                this, "Authentication failed. Check your email and password.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // if email or password is empty
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    // onRegisterClick() is called when the register button is clicked.
    fun onRegisterClick(view: android.view.View) {
        // Handle the registration link click, e.g., navigate to the registration activity
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registrationIntent)
    }



}