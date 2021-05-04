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
import com.example.electronicdiary.search.SubjectsAdapter;

public class SearchAllSubjectsFragment extends Fragment {
    private SubjectsAdapter subjectsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_subjects, container, false);

        SearchAllSubjectsViewModel searchAllSubjectsViewModel = new ViewModelProvider(this).get(SearchAllSubjectsViewModel.class);
        searchAllSubjectsViewModel.downloadAllSubjects();

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllSubjectsList);
        searchAllSubjectsViewModel.getAllSubjects().observe(getViewLifecycleOwner(), allSubjects -> {
            if (allSubjects == null) {
                return;
            }

            View.OnClickListener onItemClickListener = view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                if (actionCode == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectId", allSubjects.get(position).getId());
                    Navigation.findNavController(view).navigate(R.id.action_search_all_subjects_to_dialog_subject_editing, bundle);
                } else if (actionCode == 2) {
                    searchAllSubjectsViewModel.deleteSubject(allSubjects.get(position).getId());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 2);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_subjects_to_admin_actions, bundle);
                } else if (actionCode == 10) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionCode", 10);
                    bundle.putLong("subjectId", allSubjects.get(position).getId());
                    bundle.putLong("professorId", getArguments().getLong("professorId"));
                    bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                    Navigation.findNavController(view).navigate(R.id.action_search_all_subjects_to_search_all_groups, bundle);
                }
            };

            subjectsAdapter = new SubjectsAdapter(getContext(), allSubjects, onItemClickListener);
            recyclerView.setAdapter(subjectsAdapter);
            recyclerView.setHasFixedSize(false);
        });

        final SearchView searchView = root.findViewById(R.id.allSubjectsSearch);
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
                subjectsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}