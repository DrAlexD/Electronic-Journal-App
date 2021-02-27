package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.admin.editing.GroupEditingDialogFragment;

import java.util.ArrayList;

public class SearchGroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;
    private ArrayList<String> groups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_groups, container, false);

        downloadData();

        int actionCode = getArguments().getInt("actionCode");

        if (actionCode == -1) {
            groups.remove(getArguments().getString("group"));
        }

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 0) {
                //TODO добавление студента в базу
                /*getArguments().getString("student")*/

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", actionCode);
                Navigation.findNavController(view).navigate(R.id.action_search_groups_to_admin_actions, bundle);
            } else if (actionCode == 2) {
                //TODO удаление группы из базы

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", actionCode);
                Navigation.findNavController(view).navigate(R.id.action_search_groups_to_admin_actions, bundle);
            } else if (actionCode == -1) {
                //TODO изменить группу у студента в базе

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", 1);
                Navigation.findNavController(view).navigate(R.id.action_search_groups_to_admin_actions, bundle);
            } else if (actionCode == 1) {
                GroupEditingDialogFragment groupEditingDialogFragment = new GroupEditingDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupTitle", groups.get(position));

                groupEditingDialogFragment.setArguments(bundle);
                groupEditingDialogFragment.show(getChildFragmentManager(), "groupEditing");
            }
        };

        groupsAdapter = new GroupsAdapter(getContext(), groups, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedGroupsList);
        recyclerView.setAdapter(groupsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.groupsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск всех групп
        groups = new ArrayList<>();
        groups.add("1ИУ9-11");
        groups.add("2ИУ9-21");
        groups.add("3ИУ9-31");
        groups.add("4ИУ9-41");
        groups.add("5ИУ9-51");
        groups.add("6ИУ9-61");
        groups.add("7ИУ9-71");
        groups.add("8ИУ9-81");
        groups.add("9ИУ9-91");
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                groupsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}