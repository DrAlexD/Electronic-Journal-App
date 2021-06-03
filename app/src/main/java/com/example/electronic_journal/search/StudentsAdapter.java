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
import com.example.electronic_journal.data_classes.Student;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;
    private int color = R.color.white;
    private List<Student> students;

    private List<Student> originalStudents;

    public StudentsAdapter(Context context, List<Student> students, View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    @NotNull
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull StudentsAdapter.ViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(inflater.getContext().getColor(color));

        Student student = students.get(position);

        holder.studentNameView.setText(student.getFullName());
        holder.studentGroupView.setText(student.getGroup().getTitle());
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
                List<Student> filterResults = new ArrayList<>();

                if (originalStudents == null) {
                    originalStudents = students;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Student student : originalStudents) {
                        if ((student.getFullName().toLowerCase()).contains(findTextChange.toLowerCase()) ||
                                (student.getGroup().getTitle().toLowerCase()).contains(findTextChange.toLowerCase()))
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
                students = (List<Student>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView studentNameView;
        final TextView studentGroupView;

        ViewHolder(View view) {
            super(view);
            studentNameView = view.findViewById(R.id.studentName);
            studentGroupView = view.findViewById(R.id.studentGroup);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
