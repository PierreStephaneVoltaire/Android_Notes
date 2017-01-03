package com.example.steph.stickynotesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * main activity
 **/
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    public static final String Note = "note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);


    }

    /**
     * populates and updates the lisview on activity resume
     **/
    @Override
    protected void onResume() {
        NoteDB db = new NoteDB(this);
        String title = null;
        ArrayList<Note> noteArrayList = db.getNote(title);
        List<Note> list = new ArrayList<Note>();
        if (noteArrayList != null) {
            int size = noteArrayList.size();
            for (int i = 0; i < size; i++) {
                list.add(noteArrayList.get(i));
            }
            Note[] dataSource = list.toArray(new Note[list.size()]);
            for (int i = 0; i < size; i++) {
                dataSource[i] = noteArrayList.get(i);
            }
            ArrayAdapter adapter = new NoteAdapter(MainActivity.this, R.layout.row, noteArrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(this);
        }
        super.onResume();
    }

    /**
     * puts an actionBar in the activity
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * handles menu events
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(this, AddOrEditNotes.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * handles listview events and send the user to the AddOrEdit activity
     **/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        TextView textView1 = (TextView) view.findViewById(R.id.notetitle);
        TextView textView2 = (TextView) view.findViewById(R.id.noteDescription);
        Intent intent = new Intent(this, AddOrEditNotes.class);
        NoteDB noteDB = new NoteDB(this);
        ArrayList<Note> notes = noteDB.getNote(textView1.getText().toString());
        Note note = new Note(textView1.getText().toString(), textView2.getText().toString(), notes.get(0).getContent(), notes.get(0).getCreateDate(), notes.get(0).getLastModified());
        intent.putExtra(Note, note);
        startActivity(intent);
    }

    /**
     * sends the user to the AddOrEdit activity
     **/
    public void goToAdd(View view) {
        Intent intent = new Intent(this, AddOrEditNotes.class);
        startActivity(intent);
    }

    /**
     * custom array adapter to handle Note objects
     **/
    public static class NoteAdapter extends ArrayAdapter<Note> {
        private final Context context;
        private final ArrayList<Note> data;
        private final int layoutResourceId;

        public NoteAdapter(Context context, int layoutResourceId, ArrayList<Note> data) {
            super(context, layoutResourceId, data);
            this.context = context;
            this.data = data;
            this.layoutResourceId = layoutResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.textView1 = (TextView) row.findViewById(R.id.notetitle);
                holder.textView2 = (TextView) row.findViewById(R.id.noteDescription);
                holder.textView3 = (TextView) row.findViewById(R.id.notemodified);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            Note note = data.get(position);
            holder.textView1.setText(note.getTitle());
            holder.textView2.setText(note.getDescription());
            if (note.getCreateDate().compareTo(note.getLastModified()) == 0)
                holder.textView3.setText("created: " + note.getCreateDate().toString());
            else
                holder.textView3.setText("Last modified: " + note.getLastModified().toString());
            return row;
        }

        static class ViewHolder {
            TextView textView1;
            TextView textView2;
            TextView textView3;
        }
    }
}