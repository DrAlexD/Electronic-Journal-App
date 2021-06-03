package com.example.electronic_journal.search.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.search.SubjectsAdapter;

import java.util.ArrayList;
import java.util.List;

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
            if (allSubjects != null) {
                List<Subject> allSubjects2 = new ArrayList<>();
                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 2) {
                        searchAllSubjectsViewModel.deleteSubject(subjectsAdapter.getSubjects().get(position).getId());
                        searchAllSubjectsViewModel.getAnswer().observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешное удаление предмета", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", 2);
                                Navigation.findNavController(view).navigate(R.id.action_search_all_subjects_to_admin_actions, bundle);
                            }
                        });
                    } else if (actionCode == 10) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("actionCode", 10);
                        bundle.putLong("subjectId", subjectsAdapter.getSubjects().get(position).getId());
                        bundle.putLong("professorId", getArguments().getLong("professorId"));
                        bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                        Navigation.findNavController(view).navigate(R.id.action_search_all_subjects_to_search_all_groups, bundle);
                    }
                };

                if (actionCode == 10) {
                    searchAllSubjectsViewModel.downloadAvailableSubjects(getArguments().getLong("professorId"),
                            getArguments().getLong("semesterId"));

                    searchAllSubjectsViewModel.getAvailableSubjects().observe(getViewLifecycleOwner(), availableSubjects -> {
                        if (allSubjects2.isEmpty()) {
                            if (availableSubjects == null) {
                                allSubjects2.addAll(allSubjects);
                            } else {
                                for (Subject subject1 : allSubjects) {
                                    boolean isOk = true;
                                    for (Subject subject2 : availableSubjects) {
                                        if (subject1.getId() == subject2.getId()) {
                                            isOk = false;
                                            break;
                                        }
                                    }
                                    if (isOk)
                                        allSubjects2.add(subject1);
                                }
                            }
                        }

                        subjectsAdapter = new SubjectsAdapter(getContext(), allSubjects2, onItemClickListener);
                        recyclerView.setAdapter(subjectsAdapter);
                        recyclerView.setHasFixedSize(false);
                    });
                } else {
                    allSubjects2.addAll(allSubjects);
                    subjectsAdapter = new SubjectsAdapter(getContext(), allSubjects2, onItemClickListener);
                    recyclerView.setAdapter(subjectsAdapter);
                    recyclerView.setHasFixedSize(false);
                }
            }
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