/*
package com.example.electronicdiary.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<Subject> subjects;

    private ArrayList<Subject> originalSubjects;

    SubjectsAdapter(Context context, ArrayList<Subject> subjects) {
        this.inflater = LayoutInflater.from(context);
        this.subjects = subjects;
    }

    @Override
    @NotNull
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subject_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull SubjectsAdapter.ViewHolder holder, int position) {
        Subject subject = subjects.get(position);

        holder.subjectImageView.setImageResource(subject.getImage());
        holder.subjectNameView.setText(subject.getName());
        holder.subjectWinRateDiffView.setText(textForWinRateDiffView(subject));
        holder.subjectNewWinRateView.setText(subject.getNewWinRate() + "%");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView subjectImageView;
        final TextView subjectNameView;
        final TextView subjectWinRateDiffView;
        final TextView subjectNewWinRateView;

        ViewHolder(View view) {
            super(view);
            subjectImageView = view.findViewById(R.id.subjectImage);
            subjectNameView = view.findViewById(R.id.subjectName);
            subjectWinRateDiffView = view.findViewById(R.id.subjectWinRateDiff);
            subjectNewWinRateView = view.findViewById(R.id.subjectNewWinRate);
        }
    }

    private String textForWinRateDiffView(Subject subject) {
        String frontSign = "";
        if (subject.getWinRateDiff() > 0.0)
            frontSign = "+";
        return frontSign + subject.getWinRateDiff() + "%";
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
}

*/
