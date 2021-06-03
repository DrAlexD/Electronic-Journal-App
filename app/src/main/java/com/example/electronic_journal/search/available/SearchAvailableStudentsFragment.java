package com.example.electronic_journal.search.available;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.electronic_journal.R;
import com.example.electronic_journal.Repository;
import com.example.electronic_journal.search.StudentsAdapter;

public class SearchAvailableStudentsFragment extends Fragment {
    private StudentsAdapter studentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_students, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        long semesterId = Long.parseLong(sharedPreferences.getString(getString(R.string.current_semester), "-1"));

        SearchAvailableStudentsViewModel searchAvailableStudentsViewModel = new ViewModelProvider(this).get(SearchAvailableStudentsViewModel.class);
        searchAvailableStudentsViewModel.downloadAvailableStudents(Repository.getInstance().getUser().getId(), semesterId);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableStudentsList);
        searchAvailableStudentsViewModel.getAvailableStudents().observe(getViewLifecycleOwner(), availableStudents -> {
            if (availableStudents != null) {
                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    Bundle bundle = new Bundle();
                    bundle.putLong("studentId", studentsAdapter.getStudents().get(position).getId());
                    bundle.putLong("semesterId", semesterId);
                    Navigation.findNavController(view).navigate(R.id.action_search_available_students_to_student_profile, bundle);
                };

                studentsAdapter = new StudentsAdapter(getContext(), availableStudents, onItemClickListener);
                recyclerView.setAdapter(studentsAdapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        final SearchView searchView = root.findViewById(R.id.availableStudentsSearch);
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
                studentsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}