package com.example.assignment2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends  RecyclerView.Adapter<NoteViewHolder>
{
    private static final String TAG="NoteAdapter";
    private List<Note> noteList;
    private MainActivity mainActivity;

    public NoteAdapter(List<Note> noteList, MainActivity mainActivity)
    {
        this.noteList = noteList;
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: Making New ");

        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        rowView.setOnClickListener(mainActivity);
        rowView.setOnLongClickListener(mainActivity);

        return new NoteViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: Filling View Holder");

        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date.setText(note.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}