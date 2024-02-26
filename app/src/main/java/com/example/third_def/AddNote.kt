package com.example.third_def

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddNote : AppCompatActivity() {

    private lateinit var icBack: ImageButton
//    private lateinit var submit: ImageView
    private lateinit var btnAddNote: Button
    private lateinit var txtTitle: TextInputEditText
    private lateinit var txtDescription: TextInputEditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)


        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDescription)



        icBack = findViewById(R.id.icBack)
        icBack.setOnClickListener(::goToBack)

        btnAddNote = findViewById(R.id.btnAddNote)
        btnAddNote.setOnClickListener(::submitNote)
    }

    private fun goToBack(view: View) {
        finish()
    }

    private fun submitNote(view: View) {
        addNote()
    }

    private fun addNote() {

        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val userId = prefs.getInt("Id", 0)

        val title = txtTitle.text.toString()
        val description = txtDescription.text.toString()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all field !", Toast.LENGTH_SHORT).show()
            return
        }

        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE, null)
        database.execSQL("INSERT INTO Notes (Title,Content,UserId) VALUES (?,?,?)", arrayOf(title, description, userId))
        finish()
        database.close()
    }
}