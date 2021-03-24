package com.example.electronicdiary.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Subject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private ArrayList<Subject> subjects;

    private ArrayList<Subject> originalSubjects;

    public SubjectsAdapter(Context context, ArrayList<Subject> subjects, View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.subjects = subjects;
    }

    @Override
    @NotNull
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull SubjectsAdapter.ViewHolder holder, int position) {
        Subject subject = subjects.get(position);

        holder.subjectTitleView.setText(subject.getTitle());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Subject> filterResults = new ArrayList<>();

                if (originalSubjects == null) {
                    originalSubjects = subjects;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Subject subject : originalSubjects) {
                        if ((subject.getTitle().toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(subject);
                    }
                } else {
                    filterResults = originalSubjects;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                subjects = (ArrayList<Subject>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView subjectTitleView;

        ViewHolder(View view) {
            super(view);
            subjectTitleView = view.findViewById(R.id.subjectTitle);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
