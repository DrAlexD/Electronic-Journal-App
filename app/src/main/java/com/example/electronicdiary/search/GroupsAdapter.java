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
import com.example.electronicdiary.data_classes.Group;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private final View.OnClickListener onItemClickListener;

    private List<Group> groups;

    private List<Group> originalGroups;

    public GroupsAdapter(Context context, List<Group> groups, View.OnClickListener onItemClickListener) {
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

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public void onBindViewHolder(@NotNull GroupsAdapter.ViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.groupTitleView.setText(group.getTitle());
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
                List<Group> filterResults = new ArrayList<>();

                if (originalGroups == null) {
                    originalGroups = groups;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Group group : originalGroups) {
                        if ((group.getTitle().toLowerCase()).contains(findTextChange.toLowerCase()))
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
                groups = (List<Group>) results.values;
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
