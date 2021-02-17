package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Event> events;

    EventsAdapter(Context context, ArrayList<Event> events) {
        this.inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @Override
    @NotNull
    public EventsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull EventsAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventTitleView.setText(event.getTitle());
        holder.eventPointsView.setText(String.valueOf(event.getPoints()));
    }

    @Override
    public int getItemCount() {
        return events.size();
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
