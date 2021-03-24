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

            View.OnClickListener onItemClickListener = view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                if (actionCode == 0) {
                    String studentName = getArguments().getString("studentName");
                    String studentSecondName = getArguments().getString("studentSecondName");
                    String studentLogin = getArguments().getString("studentLogin");
                    String studentPassword = getArguments().getString("studentPassword");

                    searchAllGroupsViewModel.addStudent(studentName, studentSecondName, allGroups.get(position).getId(), studentLogin, studentPassword);

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", actionCode);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("groupId", allGroups.get(position).getId());
                    Navigation.findNavController(root).navigate(R.id.action_search_all_groups_to_dialog_group_editing, bundle);
                } else if (actionCode == 2) {
                    searchAllGroupsViewModel.deleteGroup(allGroups.get(position).getId());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", actionCode);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 3) {
                    if (getArguments().getInt("groupId") != allGroups.get(position).getId()) {
                        searchAllGroupsViewModel.changeStudentGroup(getArguments().getInt("studentId"),
                                allGroups.get(position).getId());

                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", 1);
                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                } else if (actionCode == 10) {
                    searchAllGroupsViewModel.addAvailableSubject(getArguments().getInt("professorId"),
                            "isLecturer", allGroups.get(position).getId(), getArguments().getInt("subjectId"),
                            getArguments().getInt("semesterId"), "isExam");

                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_profile);
                } else if (actionCode == 11) {
                    searchAllGroupsViewModel.addGroupInAvailableSubject("lecturerId",
                            "seminarianId", allGroups.get(position).getId(), getArguments().getInt("subjectId"),
                            getArguments().getInt("semesterId"), "isExam");

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