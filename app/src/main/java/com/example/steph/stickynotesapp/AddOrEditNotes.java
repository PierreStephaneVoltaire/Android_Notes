package com.example.steph.stickynotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Note activity
 * it allows the user to view, edit, save, lock and delete Notes
 */
public class AddOrEditNotes extends AppCompatActivity {
    /**
     * The Titleedt.
     */
    EditText titleedt, /**
     * The Descriptioedt.
     */
    descriptioedt, /**
     * The Contentedt.
     */
    contentedt;
    /**
     * The Date.
     */
    Date date;
    /**
     * The Oldtitle.
     */
    String oldtitle;
    int locked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleedt = (EditText) findViewById(R.id.titleNote);
        descriptioedt = (EditText) findViewById(R.id.Description);
        contentedt = (EditText) findViewById(R.id.notecontentedtext);
        Intent intent = getIntent();
        date = null;
         locked=1;
        setUpEditableNotes(intent);


    }

    /**
     * setUpEditableNotes
     * gets intent from previous activity.
     * if the intent has a note object as an extra it will appropriately fill the edit Texts
     *
     * @param intent the intent
     */
    public void setUpEditableNotes(Intent intent) {
        if (intent.getExtras() != null) {
            Note note = intent.getExtras().getParcelable(MainActivity.Note);
            if (note != null) {

                String content = note.getContent();

                String title = note.getTitle();
                oldtitle = title;
                String Description = note.getDescription();

                descriptioedt.setText(Description);
                titleedt.setText(title);
                contentedt.setText(content);
                descriptioedt.setEnabled(false);
                titleedt.setEnabled(false);
                contentedt.setEnabled(false);
                locked=note.getLocked();

                date = note.getCreateDate();
            }
        }
    }

    /**
     * put an actionbar in the activity
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);

        return true;
    }

    /**
     * handles menu events
     **/
    public boolean onOptionsItemSelected(MenuItem item) {
        NoteDB noteDB = new NoteDB(this);
        long result;
        String title = titleedt.getText().toString();
        String description = descriptioedt.getText().toString();
        String content = contentedt.getText().toString();
        if (title.trim().length() != 0 || title != null) {

            Note note = new Note(title, description, content, date, Calendar.getInstance().getTime(),locked);
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_edit:
                    descriptioedt.setEnabled(true);
                    titleedt.setEnabled(true);
                    contentedt.setEnabled(true);
                    return true;
                case R.id.action_save:
                    if (note.getCreateDate() == null) {
                        note.setCreateDate(Calendar.getInstance().getTime());

                        result = noteDB.insertNote(note);
                    } else {
                        result = noteDB.editNote(note, oldtitle);
                    }
                    if (result > 0) {
                        Toast.makeText(AddOrEditNotes.this, note.getTitle() + " was saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddOrEditNotes.this, note.getTitle() + "could not be saved", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.action_delete:
                    if (note.getCreateDate() != null) {

                        result = noteDB.removeNotes(note);
                        if (result > 0) {
                            Toast.makeText(AddOrEditNotes.this, note.getTitle() + " was deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddOrEditNotes.this, note.getTitle() + "could not be deleted", Toast.LENGTH_SHORT).show();
                        }


                    }


                    return true;
                case R.id.action_lock:
                    note.setLocked(0);

                    if (note.getCreateDate() != null) {

                        result = noteDB.editNote(note, oldtitle);
                        if (result > 0) {
                            Toast.makeText(AddOrEditNotes.this, note.getTitle() + " was locked", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddOrEditNotes.this, note.getTitle() + "could not be locked", Toast.LENGTH_SHORT).show();
                        }

                    }
                 else{
                        Toast.makeText(AddOrEditNotes.this, note.getTitle() + "please save the note before locking it", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            Toast.makeText(AddOrEditNotes.this, "a title wasn't set", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }

    }
}
