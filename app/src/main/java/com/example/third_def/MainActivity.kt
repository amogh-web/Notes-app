package com.example.third_def

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteClicked {

    private lateinit var btnLogout : ImageButton
    private lateinit var btnAddNote : FloatingActionButton
    private lateinit var lbl_Welcome : TextView

    private lateinit var rcvNotes : RecyclerView
    private lateinit var adapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val Id = prefs.getInt("Id",0)

        if(Id == 0){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        initDatabase()

        lbl_Welcome = findViewById(R.id.lblWelcome)
        lbl_Welcome.text = "Welcome ${prefs.getString("Name","").toString()}"

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener(::click_logout)

        rcvNotes = findViewById(R.id.rcvNotes)

        adapter = NoteAdapter(retrieveNotesFromDatabase(),this)

        rcvNotes.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
        rcvNotes.adapter = adapter

        btnAddNote = findViewById(R.id.btnAddNote)
        btnAddNote.setOnClickListener(::goToCreateNote)
    }

    private fun initDatabase(){
        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE,null)
        database.execSQL(
            """ CREATE TABLE IF NOT EXISTS Users (Id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT Not Null,Email TEXT Not Null,Password TEXT Not Null,Hobbies TEXT Not Null,Gender TEXT Not Null) """
        )

        database.execSQL(
            """ CREATE TABLE IF NOT EXISTS Notes (Id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT Not Null,Datetime DATETIME DEFAULT CURRENT_TIMESTAMP,Content TEXT Not Null,UserId INTEGER Not Null,Foreign Key(UserId) References Users(Id)) """
        )

        database.close()
    }


    private fun click_logout(view: View){

        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("Id")

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()

        editor.apply()
    }

    private fun goToCreateNote(view: View){
        val intent = Intent(this, AddNote::class.java)
        startActivity(intent)
    }

    @SuppressLint("Range")
    private fun retrieveNotesFromDatabase():MutableList<Notes> {

        val notes = mutableListOf<Notes>()

        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val id = prefs.getInt("Id", 0)

        val database = openOrCreateDatabase("notes_db", MODE_PRIVATE, null)

        val cursor = database.rawQuery("SELECT * FROM Notes WHERE UserId = ? Order By Id Desc", arrayOf(id.toString()))

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex("Title"))
            val datetime = cursor.getString(cursor.getColumnIndex("Datetime"))
            val content = cursor.getString(cursor.getColumnIndex("Content"))
            val id = cursor.getLong(cursor.getColumnIndex("Id"))

            notes.add(Notes(id,title,datetime,content))
        }
        cursor.close()
        return notes
        database.close()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateNotes(retrieveNotesFromDatabase())
    }

    override fun onClicked(note: Notes) {
        val intent = Intent(this, EditNote::class.java).apply {
            putExtra("Id",note.id)
            putExtra("title",note.title)
            putExtra("content",note.content)
        }
        startActivity(intent)
    }


}
