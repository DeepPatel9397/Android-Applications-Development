package com.example.assignment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final int B_REQUEST_CODE = 1;

    private static final String TAG = "MainActivity";
    List<Note> noteList = new ArrayList<>();
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    Note removeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(Objects.requireNonNull(getSupportActionBar())).setTitle("Multi Notes(" + noteList.size() + ")");
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Start async task");
        new MyAsyncTask(this).execute();
        Log.d(TAG, "onCreate: end async task");
    }

    private void loadData()
    {
        Log.d(TAG, "loadData: Reading JSON file");

        Note note = new Note();
        try
        {
            InputStream istream = getApplicationContext().openFileInput(getString(R.string.file_name));

            BufferedReader bfreader = new BufferedReader(new InputStreamReader(istream, getString(R.string.encoding)));
            StringBuilder sbuilder = new StringBuilder();
            String line;
            while((line = bfreader.readLine()) != null ){
                Log.d(TAG, "loadData: line:"+ line);
                sbuilder.append(line);
            }

            String jsonStr = sbuilder.toString();
            Log.d(TAG, "loadData: jsonStr: " + jsonStr);
            if(!(sbuilder.toString().isEmpty()))
            {
                //JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jsonarray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String title = jsonobject.getString("title");
                    String description = jsonobject.getString("description");
                    String date = jsonobject.getString("date");

                    noteList.add(new Note(title, description, new Date(date)));
                }
            }

        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            Log.d(TAG, "loadData: error reading file: ");
            ex.printStackTrace();
        }

    }

    @Override
    protected void onPause()
    {
        saveNote();
        super.onPause();
    }

    protected void saveNote()
    {
        Log.d(TAG, "saveNote: Saving JSON File");
        try {

            FileOutputStream fileoutstream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileoutstream, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();

            for(int i=0; i<noteList.size(); i++)
            {
                writer.beginObject();
                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("description").value(noteList.get(i).getDescription());
                writer.name("date").value(noteList.get(i).getDate().toString());
                writer.endObject();
                Log.d(TAG, "saveNote: writer: "+ i);
            }
            writer.endArray();
            writer.close();

            Log.d(TAG, "saveNote: list: "+ noteList);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.addNote:
                Note note = new Note();
                addNote(note);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Multi Notes(" + noteList.size() + ")");
                return true;
            case R.id.aboutus:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                getSupportActionBar().setTitle("Multi Notes(" + noteList.size() + ")");
                return true;
            default:
                getSupportActionBar().setTitle("Multi Notes(" + noteList.size() + ")");
                return super.onOptionsItemSelected(item);
        }
    }

    protected void addNote(Note note)
    {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("noteObject", note);
        startActivityForResult(intent, B_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == B_REQUEST_CODE) {
            if (resultCode == -1) {
                Note updatedNote = (Note) data.getSerializableExtra("updatedNote");
                noteList.add(0, updatedNote);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Changes saved  ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onActivityResult: Added new Note: " + resultCode);
            }
            //This is when user press cancel.
            else if (resultCode == 0) {
                Toast.makeText(this, "Changes discarded ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onActivityResult: Discarded result Code: " + resultCode);
            } else {
                Note updatedNote = (Note) data.getSerializableExtra("updatedNote");
                noteList.add(0, updatedNote);
                boolean remove = noteList.remove(removeNote);
                noteAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Changes Updated  ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onActivityResult: Changes Updated result Code: " + resultCode);
            }
        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("Multi Notes(" + noteList.size() + ")");
    }
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note note = noteList.get(pos);
        removeNote = note;
        addNote(note);
    }

    @Override
    public boolean onLongClick(View v) {
        checkDialogBox(v);
        getSupportActionBar().setTitle("Multi Notes(" + noteList.size() + ")");
        return false;
    }

    void checkDialogBox(final View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                int pos = recyclerView.getChildLayoutPosition(v);
                Note note = noteList.get(pos);
                noteList.remove(note);
                noteAdapter.notifyDataSetChanged();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Multi Notes(" + noteList.size() + ")");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // Do Nothing.
            }
        });

        builder.setMessage("Are you sure you want to delete this item?");
        builder.setTitle("DELETE?");

        AlertDialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Multi Notes(" + noteList.size() + ")");
    }

    protected void populateList(ArrayList<Note> list)
    {
        this.noteList = list;

        if(list.get(0).getTitle().equalsIgnoreCase("file not found"))
        {
            Toast.makeText(this, this.getString(R.string.no_file), Toast.LENGTH_LONG).show();
            noteList.remove(0);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("Multi Notes(" + noteList.size() + ")");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#191970")));

        recyclerView = findViewById(R.id.recyclerView);
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}