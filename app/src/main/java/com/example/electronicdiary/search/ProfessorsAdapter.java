package com.example.electronicdiary.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.Professor;
import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfessorsAdapter extends RecyclerView.Adapter<ProfessorsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private ArrayList<Professor> professors;

    private ArrayList<Professor> originalProfessors;

    public ProfessorsAdapter(Context context, ArrayList<Professor> professors, View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.professors = professors;
    }

    @Override
    @NotNull
    public ProfessorsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_professor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ProfessorsAdapter.ViewHolder holder, int position) {
        Professor professor = professors.get(position);

        holder.professorNameView.setText(professor.getFullName());
    }

    @Override
    public int getItemCount() {
        return professors.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Professor> filterResults = new ArrayList<>();

                if (originalProfessors == null) {
                    originalProfessors = professors;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Professor professor : originalProfessors) {
                        if ((professor.getFullName().toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(professor);
                    }
                } else {
                    filterResults = originalProfessors;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                professors = (ArrayList<Professor>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView professorNameView;

        ViewHolder(View view) {
            super(view);
            professorNameView = view.findViewById(R.id.professorName);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
