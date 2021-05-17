package com.example.electronicdiary.search.available;

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

public class SearchAvailableGroupsInSubjectFragment extends Fragment {
    private GroupsAdapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_groups_in_subject, container, false);

        SearchAvailableGroupsInSubjectViewModel searchAvailableGroupsInSubjectViewModel = new ViewModelProvider(this).
                get(SearchAvailableGroupsInSubjectViewModel.class);
        searchAvailableGroupsInSubjectViewModel.downloadAvailableGroupsInSubject(getArguments().getLong("professorId"),
                getArguments().getLong("subjectId"), getArguments().getLong("semesterId"));

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableGroupsInSubjectList);
        searchAvailableGroupsInSubjectViewModel.getAvailableGroupsInSubject().observe(getViewLifecycleOwner(),
                availableGroupsInSubject -> {
                    if (availableGroupsInSubject != null) {
                        List<Group> groups = new ArrayList<>();
                        for (SubjectInfo subjectInfo : availableGroupsInSubject) {
                            groups.add(subjectInfo.getGroup());
                        }

                        View.OnClickListener onItemClickListener = view -> {
                            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                            int position = viewHolder.getAdapterPosition();

                            Long subjectInfoId = null;
                            for (SubjectInfo si : availableGroupsInSubject) {
                                if (si.getGroup().getId() == groupsAdapter.getGroups().get(position).getId()) {
                                    subjectInfoId = si.getId();
                                    break;
                                }
                            }

                            searchAvailableGroupsInSubjectViewModel.deleteSubjectInfo(subjectInfoId, getArguments().getLong("professorId"));
                            searchAvailableGroupsInSubjectViewModel.getAnswer().observe(getViewLifecycleOwner(), answer -> {
                                if (answer != null) {
                                    Navigation.findNavController(view).navigate(R.id.action_search_available_groups_in_subject_to_profile);
                                }
                            });
                        };

                        groupsAdapter = new GroupsAdapter(getContext(), groups, onItemClickListener);
                        recyclerView.setAdapter(groupsAdapter);
                        recyclerView.setHasFixedSize(false);
                    }
                });

        final SearchView searchView = root.findViewById(R.id.availableGroupsInSubjectSearch);
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