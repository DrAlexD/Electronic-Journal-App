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
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.SubjectInfo;
import com.example.electronicdiary.search.GroupsAdapter;

import java.util.ArrayList;
import java.util.List;

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
            if (allGroups != null) {
                List<Group> allGroups2 = new ArrayList<>();

                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 0) {
                        String studentName = getArguments().getString("studentName");
                        String studentSecondName = getArguments().getString("studentSecondName");
                        String studentLogin = getArguments().getString("studentLogin");
                        String studentPassword = getArguments().getString("studentPassword");

                        searchAllGroupsViewModel.downloadGroupById(allGroups2.get(position).getId());

                        searchAllGroupsViewModel.getGroup().observe(getViewLifecycleOwner(), group -> {
                            if (group != null) {
                                searchAllGroupsViewModel.addStudent(studentName, studentSecondName, group, studentLogin, studentPassword);

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", actionCode);
                                Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                            }
                        });
                    } else if (actionCode == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("groupId", allGroups2.get(position).getId());
                        Navigation.findNavController(root).navigate(R.id.action_search_all_groups_to_dialog_group_editing, bundle);
                    } else if (actionCode == 2) {
                        searchAllGroupsViewModel.deleteGroup(allGroups2.get(position).getId());

                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", actionCode);
                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                    } else if (actionCode == 3) {
                        if (getArguments().getLong("groupId") != allGroups2.get(position).getId()) {
                            String studentName = getArguments().getString("studentFirstName");
                            String studentSecondName = getArguments().getString("studentSecondName");
                            String studentLogin = getArguments().getString("studentUsername");
                            String studentPassword = getArguments().getString("studentPassword");

                            searchAllGroupsViewModel.downloadGroupById(allGroups2.get(position).getId());

                            searchAllGroupsViewModel.getGroup().observe(getViewLifecycleOwner(), group -> {
                                if (group != null) {
                                    searchAllGroupsViewModel.changeStudentGroup(getArguments().getLong("studentId"), studentName, studentSecondName,
                                            group, studentLogin, studentPassword);

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("openPage", 1);
                                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                                }
                            });
                        }
                    } else if (actionCode == 10 || actionCode == 11) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("professorId", getArguments().getLong("professorId"));
                        bundle.putLong("groupId", allGroups2.get(position).getId());
                        bundle.putLong("subjectId", getArguments().getLong("subjectId"));
                        bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_dialog_subject_info_adding, bundle);
                    }
                };

                if (actionCode == 10 || actionCode == 11) {
                    searchAllGroupsViewModel.downloadAvailableGroupsInSubject(getArguments().getLong("professorId"),
                            getArguments().getLong("subjectId"), getArguments().getLong("semesterId"));

                    searchAllGroupsViewModel.getAvailableGroupsInSubject().observe(getViewLifecycleOwner(), availableGroupsInSubject -> {
                        if (availableGroupsInSubject == null) {
                            allGroups2.addAll(allGroups);
                        } else {
                            List<Group> groups = new ArrayList<>();
                            for (SubjectInfo subjectInfo : availableGroupsInSubject) {
                                groups.add(subjectInfo.getGroup());
                            }

                            for (Group group1 : allGroups) {
                                boolean isOk = true;
                                for (Group group2 : groups) {
                                    if (group1.getId() == group2.getId()) {
                                        isOk = false;
                                        break;
                                    }
                                }
                                if (isOk)
                                    allGroups2.add(group1);
                            }
                        }

                        groupsAdapter = new GroupsAdapter(getContext(), allGroups2, onItemClickListener);
                        recyclerView.setAdapter(groupsAdapter);
                        recyclerView.setHasFixedSize(false);
                    });
                } else {
                    allGroups2.addAll(allGroups);
                    groupsAdapter = new GroupsAdapter(getContext(), allGroups2, onItemClickListener);
                    recyclerView.setAdapter(groupsAdapter);
                    recyclerView.setHasFixedSize(false);
                }
            }
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