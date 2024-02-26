package com.example.third_def

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    private lateinit var btnLogin : Button
    private lateinit var btnGoToRegister: Button

    private lateinit var txtEmail : TextInputEditText
    private lateinit var txtPassword : TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(::loginClicked)

        btnGoToRegister = findViewById(R.id.btnGoToRegister)
        btnGoToRegister.setOnClickListener(::GoToRegister)

    }

    private fun GoToRegister(view: View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    @SuppressLint("Range")
    private fun loginClicked(view: View){

        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fieald", Toast.LENGTH_SHORT).show()
            return
        }

        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE,null)
        val cursor = database.rawQuery("SELECT *FROM Users WHERE Email = ? AND Password = ?", arrayOf(email,password))

        if(cursor.count == 0){
            Toast.makeText(this, "Please Enter valid information", Toast.LENGTH_SHORT).show()
            return
        }

        cursor.moveToFirst()
        val userId = cursor.getInt(0)
        val name = cursor.getString(cursor.getColumnIndex("Name"))

        cursor.close()
        database.close()

        val prefs   = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor  = prefs.edit()
        editor.putInt("Id",userId)
        editor.putString("Name",name)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Id",userId)
        startActivity(intent)
        finish()

    }
}