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
import com.example.electronicdiary.search.GroupsAdapter;

public class SearchAvailableGroupsInSubjectFragment extends Fragment {
    private GroupsAdapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_groups_in_subject, container, false);

        SearchAvailableGroupsInSubjectViewModel searchAvailableGroupsInSubjectViewModel = new ViewModelProvider(this).
                get(SearchAvailableGroupsInSubjectViewModel.class);
        searchAvailableGroupsInSubjectViewModel.downloadAvailableGroupsInSubject(getArguments().getInt("professorId"),
                getArguments().getInt("subjectId"), getArguments().getInt("semesterId"));

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableGroupsInSubjectList);
        searchAvailableGroupsInSubjectViewModel.getAvailableGroupsInSubject().observe(getViewLifecycleOwner(),
                availableGroupsInSubject -> {
                    if (availableGroupsInSubject == null) {
                        return;
                    }

                    View.OnClickListener onItemClickListener = view -> {
                        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                        int position = viewHolder.getAdapterPosition();

                        searchAvailableGroupsInSubjectViewModel.deleteGroupInAvailableSubject(getArguments().getInt("professorId"),
                                availableGroupsInSubject.get(position).getId(), getArguments().getInt("subjectId"),
                                getArguments().getInt("semesterId"));
                        Navigation.findNavController(view).navigate(R.id.action_search_available_groups_in_subject_to_profile);
                    };

            groupsAdapter = new GroupsAdapter(getContext(), availableGroupsInSubject, onItemClickListener);
            recyclerView.setAdapter(groupsAdapter);
            recyclerView.setHasFixedSize(false);
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