package com.example.fancy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    // create variables for the edit text fields and buttons
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button

    // create a variable for the firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // initialize the edit text fields and buttons
        nameEditText = findViewById(R.id.registerTextName)
        emailEditText = findViewById(R.id.registerTextEmailAddress)
        passwordEditText = findViewById(R.id.registerTextPassword1)
        password2EditText = findViewById(R.id.registerTextPassword2)
        registerButton = findViewById(R.id.registerButton)
        cancelButton = findViewById(R.id.cancelButton)

        // initialize the firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // set a click listener on the register button
        registerButton.setOnClickListener {
            // get the name, email and passwords from the edit text fields
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val password2 = password2EditText.text.toString()


            // check if the name, email and passwords are not empty
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                // check if the email is valid
                if (!isValidEmail(email)) {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // check if the email contains "@admin"
                if (email.contains("@admin")) {
                    Toast.makeText(
                        this,
                        "Emails with '@admin' are not allowed for registration",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // check if the password is at least 6 characters long
                if (password.length < 6) {
                    Toast.makeText(
                        this,
                        "Password should be at least 6 characters long",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // check if the passwords match
                if (password != password2) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // create a new user with the email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        // if the registration is successful display a message
                        if (task.isSuccessful) {
                            // display the message
                            Toast.makeText(
                                this, "Registration OK",
                                Toast.LENGTH_SHORT
                            ).show()

                            // redirect to the login page
                            finish()
                        } else {
                            // if the registration fails display a message
                            Toast.makeText(
                                this, "Registration failed. Check your email and password.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // if the name, email or passwords are empty display a message
                Toast.makeText(this, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // set a click listener on the cancel button
        cancelButton.setOnClickListener {
            // redirect to MainActivity
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

    }

    // function to check if the email is valid
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}