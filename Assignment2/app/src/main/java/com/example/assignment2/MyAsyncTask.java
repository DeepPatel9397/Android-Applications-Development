package com.example.assignment2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class MyAsyncTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = "MyAsyncTask";
    ArrayList<Note> noteList = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    public MyAsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... longs)
    {
        Log.d(TAG, "doInBackground: Starting background execution");

        Note note = new Note();
        String jsonStr = "";
        try
        {
            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));

            //new code for reading json array
            BufferedReader breader = new BufferedReader(new InputStreamReader(is, mainActivity.getString(R.string.encoding)));
            StringBuilder sbuilder = new StringBuilder();
            String line;
            while ((line = breader.readLine()) != null)
            {
                Log.d(TAG, "loadData: line:" + line);
                sbuilder.append(line);
            }

            jsonStr = sbuilder.toString();
            Log.d(TAG, "doInBackground: "+ jsonStr);

            Log.d(TAG, "doInBackground: Completed background execution");
            return jsonStr;
        }
        catch (FileNotFoundException e)
        {
            Log.d(TAG, "doInBackground: file note found");
            jsonStr = "file not found";
            //Toast.makeText(mainActivity, mainActivity.getString(R.string.no_file), Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            Log.d(TAG, "loadData: error reading file: ");
            ex.printStackTrace();
        }

        return jsonStr;
    }

    @Override
    protected void onPostExecute(String jsonString)
    {
        if(jsonString.equalsIgnoreCase("file not found"))
        {
            noteList.add(new Note("file not found", "", new Date()));
            mainActivity.populateList(noteList);
        }
        else
        {
            try
            {
                if(!(jsonString.isEmpty()))
                {
                    JSONArray jsonarray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonarray.length(); i++)
                    {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String title = jsonobject.getString("title");
                        String description = jsonobject.getString("description");
                        String date = jsonobject.getString("date");

                        noteList.add(new Note(title, description, new Date(date)));
                    }
                    mainActivity.populateList(noteList);
                }

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
