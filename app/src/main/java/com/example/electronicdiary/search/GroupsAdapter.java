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

class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private ArrayList<String> groups;

    private ArrayList<String> originalGroups;

    GroupsAdapter(Context context, ArrayList<String> groups, View.OnClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.groups = groups;
    }

    @Override
    @NotNull
    public GroupsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.holder_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull GroupsAdapter.ViewHolder holder, int position) {
        String group = groups.get(position);

        holder.groupTitleView.setText(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<String> filterResults = new ArrayList<>();

                if (originalGroups == null) {
                    originalGroups = groups;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (String group : originalGroups) {
                        if ((group.toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(group);
                    }
                } else {
                    filterResults = originalGroups;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                groups = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView groupTitleView;

        ViewHolder(View view) {
            super(view);
            groupTitleView = view.findViewById(R.id.groupTitle);

            view.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
