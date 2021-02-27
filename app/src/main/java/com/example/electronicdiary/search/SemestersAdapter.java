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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class SemestersAdapter extends RecyclerView.Adapter<SemestersAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private ArrayList<Semester> semesters;

    private ArrayList<Semester> originalSemesters;

    SemestersAdapter(Context context, ArrayList<Semester> semesters, View.OnClickListener onItemClickListener) {
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

    @Override
    public int getItemCount() {
        return semesters.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Semester> filterResults = new ArrayList<>();

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
                semesters = (ArrayList<Semester>) results.values;
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
