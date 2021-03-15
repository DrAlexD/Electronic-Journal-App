package com.example.electronicdiary.search.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.search.GroupsAdapter;

public class SearchAllGroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_groups, container, false);

        SearchAllGroupsViewModel searchAllGroupsViewModel = new ViewModelProvider(this).get(SearchAllGroupsViewModel.class);
        searchAllGroupsViewModel.downloadAllGroups();

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllGroupsList);
        searchAllGroupsViewModel.getAllGroups().observe(getViewLifecycleOwner(), allGroups -> {
            if (allGroups == null) {
                return;
            }

            if (actionCode == 3) {
                allGroups.remove(getArguments().getString("group"));
            }

            View.OnClickListener onItemClickListener = view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                if (actionCode == 0) {
                    //TODO добавление студента в базу
                    /*getArguments().getString("student")*/

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", actionCode);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("groupTitle", allGroups.get(position));
                    Navigation.findNavController(root).navigate(R.id.action_search_all_groups_to_dialog_group_editing, bundle);
                } else if (actionCode == 2) {
                    //TODO удаление группы из базы

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", actionCode);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 3) {
                    //TODO изменить группу у студента в базе

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 10) {
                    //TODO добавить предмет (пока с 1 группой) к преподавателю
                    /*getArguments().getString("subjectTitle")*/
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_profile);
                } else if (actionCode == 11) {
                    //TODO добавить группу в предмет к преподавателю
                    /*getArguments().getString("subjectTitle")*/
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_profile);
                }
            };

            groupsAdapter = new GroupsAdapter(getContext(), allGroups, onItemClickListener);
            recyclerView.setAdapter(groupsAdapter);
            recyclerView.setHasFixedSize(false);
        });

        final SearchView searchView = root.findViewById(R.id.allGroupsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());

        return root;
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