package com.example.assignment2;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder
{
    public TextView title;
    public TextView description;
    public TextView date;

    public NoteViewHolder(View view)
    {
        super(view);

        title = view.findViewById(R.id.titleTextView);
        description = view.findViewById(R.id.descriptionTextView);
        date = view.findViewById(R.id.dateTextView);

    }
}
