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
import com.example.electronicdiary.search.StudentsAdapter;

public class SearchAllStudentsFragment extends Fragment {
    private StudentsAdapter studentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_students, container, false);

        SearchAllStudentsViewModel searchAllStudentsViewModel = new ViewModelProvider(this).get(SearchAllStudentsViewModel.class);
        searchAllStudentsViewModel.downloadAllStudents();

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllStudentsList);
        searchAllStudentsViewModel.getAllStudents().observe(getViewLifecycleOwner(), allStudents -> {
            if (allStudents == null) {
                return;
            }

            View.OnClickListener onItemClickListener = view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                if (actionCode == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("student", allStudents.get(position).getFullName());
                    Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_dialog_student_editing, bundle);
                } else if (actionCode == 2) {
                    searchAllStudentsViewModel.deleteStudent(allStudents.get(position).getId());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", actionCode);
                    Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_admin_actions, bundle);
                } else if (actionCode == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionCode", actionCode);
                    bundle.putString("student", allStudents.get(position).getFullName());
                    bundle.putString("group", allStudents.get(position).getGroup());

                    Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_search_all_groups, bundle);
                }
            };

            studentsAdapter = new StudentsAdapter(getContext(), allStudents, onItemClickListener);
            recyclerView.setAdapter(studentsAdapter);
            recyclerView.setHasFixedSize(false);
        });

        final SearchView searchView = root.findViewById(R.id.allStudentsSearch);
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