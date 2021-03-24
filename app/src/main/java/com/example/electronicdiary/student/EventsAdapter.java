package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.R;
import com.example.electronicdiary.StudentEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private final ArrayList<Event> events;
    private final ArrayList<StudentEvent> studentEvents;

    EventsAdapter(Context context, ArrayList<Event> events, ArrayList<StudentEvent> studentEvents,
                  View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.events = events;
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
        Event event = events.get(position);
        StudentEvent studentEvent = null;
        int lastAttempt = 0;
        for (int i = 0; i < studentEvents.size(); i++) {
            StudentEvent studentEventTemp = studentEvents.get(i);
            if (event.getId() == studentEventTemp.getEventId() && studentEventTemp.getAttemptNumber() > lastAttempt) {
                studentEvent = studentEventTemp;
                lastAttempt = studentEventTemp.getAttemptNumber();
            }
        }

        if (lastAttempt == 0) {
            holder.eventTitleView.setText(event.getTitle());
            holder.attemptNumberView.setText("");
            holder.allPointsView.setText("Нет данных");
            holder.finishDateView.setText("");
        } else if (!studentEvent.isAttended()) {
            holder.eventTitleView.setText(event.getTitle());
            holder.attemptNumberView.setText("");
            holder.allPointsView.setText("Не посещено");
            holder.finishDateView.setText("");
        } else {
            holder.eventTitleView.setText(event.getTitle());
            holder.attemptNumberView.setText(String.valueOf(studentEvent.getAttemptNumber()));
            holder.allPointsView.setText(String.valueOf(studentEvent.getEarnedPoints() + studentEvent.getBonusPoints()));
            holder.finishDateView.setText(studentEvent.getFinishDate().getDate());

            if (studentEvent.getEarnedPoints() + studentEvent.getBonusPoints() < event.getMinPoints()) {
                holder.allPointsView.setTextColor(inflater.getContext().getColor(R.color.red));
            } else {
                holder.allPointsView.setTextColor(inflater.getContext().getColor(R.color.green));
            }

            if (studentEvent.getFinishDate().after(event.getDeadlineDate())) {
                holder.finishDateView.setTextColor(inflater.getContext().getColor(R.color.red));
            } else {
                holder.finishDateView.setTextColor(inflater.getContext().getColor(R.color.green));
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView eventTitleView;
        final TextView attemptNumberView;
        final TextView allPointsView;
        final TextView finishDateView;

        ViewHolder(View view) {
            super(view);
            eventTitleView = view.findViewById(R.id.eventTitle);
            attemptNumberView = view.findViewById(R.id.attemptNumber);
            allPointsView = view.findViewById(R.id.allPoints);
            finishDateView = view.findViewById(R.id.finishDate);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
