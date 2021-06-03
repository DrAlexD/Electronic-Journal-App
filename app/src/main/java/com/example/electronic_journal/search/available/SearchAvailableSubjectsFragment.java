package com.example.electronic_journal.search.available;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.SubjectInfo;
import com.example.electronic_journal.search.SubjectsAdapter;

import java.util.List;
import java.util.Map;

public class SearchAvailableSubjectsFragment extends Fragment {
    private SubjectsAdapter subjectsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_subjects, container, false);

        SearchAvailableSubjectsViewModel searchAvailableSubjectsViewModel = new ViewModelProvider(this).get(SearchAvailableSubjectsViewModel.class);
        searchAvailableSubjectsViewModel.downloadAvailableSubjects(getArguments().getLong("professorId"), getArguments().getLong("semesterId"));

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableSubjectsList);
        searchAvailableSubjectsViewModel.getAvailableSubjects().observe(getViewLifecycleOwner(), availableSubjects -> {
            if (availableSubjects != null) {
                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 11) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("actionCode", 11);
                        bundle.putLong("subjectId", subjectsAdapter.getSubjects().get(position).getId());
                        bundle.putLong("professorId", getArguments().getLong("professorId"));
                        bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                        Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_search_all_groups, bundle);
                    } else if (actionCode == 12) {
                        searchAvailableSubjectsViewModel.downloadAvailableSubjectsWithGroups(getArguments().getLong("professorId"), getArguments().getLong("semesterId"));
                        LiveData<Map<String, List<SubjectInfo>>> availableSubjectsWithGroupsLiveData = searchAvailableSubjectsViewModel.getAvailableSubjectsWithGroups();
                        LiveData<Boolean> answerLiveData = Transformations.switchMap(availableSubjectsWithGroupsLiveData, availableSubjectsWithGroups -> {
                            for (SubjectInfo s : availableSubjectsWithGroups.get(String.valueOf(subjectsAdapter.getSubjects().get(position).getId()))) {
                                searchAvailableSubjectsViewModel.deleteAvailableSubject(s.getId(), getArguments().getLong("professorId"));
                            }

                            return searchAvailableSubjectsViewModel.getAnswer();
                        });

                        answerLiveData.observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_profile);
                            }
                        });
                    } else if (actionCode == 13) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("subjectId", subjectsAdapter.getSubjects().get(position).getId());
                        bundle.putLong("professorId", getArguments().getLong("professorId"));
                        bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                        Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_search_available_groups_in_subject, bundle);
                    }
                };

                subjectsAdapter = new SubjectsAdapter(getContext(), availableSubjects, onItemClickListener);
                recyclerView.setAdapter(subjectsAdapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        final SearchView searchView = root.findViewById(R.id.availableSubjectsSearch);
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