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

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private ArrayList<Student> students;

    private ArrayList<Student> originalStudents;

    StudentsAdapter(Context context, ArrayList<Student> students) {
        this.inflater = LayoutInflater.from(context);
        this.students = students;
    }

    @Override
    @NotNull
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentsAdapter.ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.studentNameView.setText(student.getName());
        holder.studentGroupView.setText(student.getGroup());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Student> filterResults = new ArrayList<>();

                if (originalStudents == null) {
                    originalStudents = students;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Student student : originalStudents) {
                        if ((student.getName().toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(student);
                    }
                } else {
                    filterResults = originalStudents;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                students = (ArrayList<Student>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView studentNameView;
        final TextView studentGroupView;

        ViewHolder(View view) {
            super(view);
            studentNameView = view.findViewById(R.id.studentName);
            studentGroupView = view.findViewById(R.id.studentGroup);
        }
    }
}
