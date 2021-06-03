package com.example.electronic_journal.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SemestersAdapter extends RecyclerView.Adapter<SemestersAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private List<Semester> semesters;

    private List<Semester> originalSemesters;

    public SemestersAdapter(Context context, List<Semester> semesters, View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.semesters = semesters;
    }

    @Override
    @NotNull
    public SemestersAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_semester, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull SemestersAdapter.ViewHolder holder, int position) {
        Semester semester = semesters.get(position);

        holder.semesterYearAndHalfView.setText(semester.getFirstHalf() + " половина " + semester.getYear());
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    @Override
    public int getItemCount() {
        return semesters.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                List<Semester> filterResults = new ArrayList<>();

                if (originalSemesters == null) {
                    originalSemesters = semesters;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Semester semester : originalSemesters) {
                        if ((String.valueOf(semester.getYear()).toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(semester);
                    }
                } else {
                    filterResults = originalSemesters;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                semesters = (List<Semester>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView semesterYearAndHalfView;

        ViewHolder(View view) {
            super(view);
            semesterYearAndHalfView = view.findViewById(R.id.semesterYearAndHalf);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
