package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronicdiary.R;

public class SearchFragment extends Fragment {
    //private StudentsAdapter studentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //TODO запуск поиска
        /*studentsAdapter = new StudentsAdapter(getActivity().getApplicationContext(), students);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedStudentsList);
        recyclerView.setAdapter(studentsAdapter);*/

        final SearchView searchView = root.findViewById(R.id.studentsSearch);
        searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    /*private SearchView.OnQueryTextListener getSearchTextUpdateListener() {

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
    }*/
}