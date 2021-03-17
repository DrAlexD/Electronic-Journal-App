package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.StudentEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<StudentEvent> studentEvents;

    EventsAdapter(Context context, ArrayList<StudentEvent> studentEvents) {
        this.inflater = LayoutInflater.from(context);
        this.studentEvents = studentEvents;
    }

    @Override
    @NotNull
    public EventsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull EventsAdapter.ViewHolder holder, int position) {
        StudentEvent studentEvent = studentEvents.get(position);

        holder.eventTitleView.setText(studentEvent.getTitle());
        holder.eventPointsView.setText(String.valueOf(studentEvent.getPoints()));
    }

    @Override
    public int getItemCount() {
        return studentEvents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView eventTitleView;
        final TextView eventPointsView;

        ViewHolder(View view) {
            super(view);
            eventTitleView = view.findViewById(R.id.eventTitle);
            eventPointsView = view.findViewById(R.id.eventPoints);
        }
    }
}
