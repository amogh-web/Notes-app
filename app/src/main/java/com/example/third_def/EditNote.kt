package com.example.third_def

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class EditNote : AppCompatActivity() {

    private lateinit var txtTitle: TextInputEditText
    private lateinit var txtContent: TextInputEditText
    private lateinit var clickDelete: ImageButton
    private lateinit var btnUpdate: Button
    private lateinit var icBack: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        val dataFromHome = intent
        val title = dataFromHome.getStringExtra("title")
        val content = dataFromHome.getStringExtra("content")

        txtTitle = findViewById(R.id.txtEditTitle)
        txtContent = findViewById(R.id.txtEditDescription)

        txtTitle.setText(title)
        txtContent.setText(content)

        clickDelete = findViewById(R.id.delete)
        clickDelete.setOnClickListener(::deleteNote)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnUpdate.setOnClickListener(::updateNote)

        icBack = findViewById(R.id.icBack)
        icBack.setOnClickListener(::backClicked)
    }

    private fun backClicked(view: View) {
        finish()
    }

    private fun deleteNote(view: View) {
        val dataFromHome = intent
        val id = dataFromHome.getLongExtra("Id", 0)

        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE, null)
        database.execSQL("DELETE FROM Notes WHERE ID = ?", arrayOf(id))
        finish()
        database.close()

    }

    private fun updateNote(view: View) {
        val dataFromHome = intent
        val id = dataFromHome.getLongExtra("Id", 0)

        val title = txtTitle.text.toString()
        val description = txtContent.text.toString()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all field !", Toast.LENGTH_SHORT).show()
            return
        }

        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE, null)
        database.execSQL(
            "UPDATE Notes SET Title = ?,Content = ? WHERE ID = ?",
            arrayOf(title, description, id)
        )
        finish()
        database.close()
    }
}