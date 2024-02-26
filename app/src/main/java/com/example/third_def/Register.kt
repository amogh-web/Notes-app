package com.example.third_def

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText

class Register : AppCompatActivity() {

    private lateinit var txtName: TextInputEditText
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var txtConPassword: TextInputEditText

    private lateinit var Gender : RadioGroup
    private lateinit var rdbMale : RadioButton
    private lateinit var rdbFemale : RadioButton

    private lateinit var chkReading : CheckBox
    private lateinit var chkBlogging : CheckBox
    private lateinit var chkSwimming : CheckBox
    private lateinit var chkCoding : CheckBox

    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtConPassword = findViewById(R.id.txtConPassword)

        Gender = findViewById(R.id.Gender)
        rdbMale = findViewById(R.id.rdbMale)
        rdbFemale = findViewById(R.id.rdbFemale)

        chkReading = findViewById(R.id.chkReading)
        chkBlogging = findViewById(R.id.chkBlogging)
        chkSwimming = findViewById(R.id.chkSwimming)
        chkCoding = findViewById(R.id.chkCoding)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(::goToLogin)

        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener(::Submit)

    }

    private fun goToLogin(view: View){
        finish()
    }

    private fun Submit(view: View){

        val name = txtName.text.toString()
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        val conPassword = txtConPassword.text.toString()

        val gender = if(rdbMale.isChecked) "Male" else "Female"

        var hobbies = ""

        for(chkHobbies in arrayOf(chkReading,chkSwimming,chkBlogging,chkCoding)){
            if(chkHobbies.isChecked){
                hobbies += "${chkHobbies.text},"
            }
        }

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || conPassword.isEmpty()){
            Toast.makeText(this, "Please fill all fieald !", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != conPassword){
            Toast.makeText(this, "Not matched Password & ConfirmPassword", Toast.LENGTH_SHORT).show()
        }

        //select email if this email alrady exisist so display error

        val database = openOrCreateDatabase("notes_db",MODE_PRIVATE,null)

        val cursor = database.rawQuery("SELECT *FROM Users WHERE Email = ?", arrayOf(email))
        if(cursor.count == 1){

            cursor.close()
            database.close()

            Toast.makeText(this, "This email is alrady exisits", Toast.LENGTH_SHORT).show()
            return
        }

        database.execSQL("INSERT INTO Users(Name,Email,Password,Hobbies,Gender) VALUES(?,?,?,?,?)", arrayOf(name,email,password,hobbies,gender))
        database.close()


        finish()
    }
}