package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private StudentsAdapter studentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //TODO запуск поиска
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("1ИУ9-11", "1Александр", "1Другаков"));
        students.add(new Student("2ИУ9-21", "2Александр", "2Другаков"));
        students.add(new Student("3ИУ9-31", "3Александр", "3Другаков"));
        students.add(new Student("4ИУ9-41", "4Александр", "4Другаков"));
        students.add(new Student("5ИУ9-51", "5Александр", "5Другаков"));
        studentsAdapter = new StudentsAdapter(getActivity().getApplicationContext(), students);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedStudentsList);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(studentsAdapter);

        final SearchView searchView = root.findViewById(R.id.studentsSearch);
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