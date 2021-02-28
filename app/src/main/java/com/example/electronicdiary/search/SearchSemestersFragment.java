package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class SearchSemestersFragment extends Fragment {
    private SemestersAdapter semestersAdapter;
    private ArrayList<Semester> semesters;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_semesters, container, false);

        downloadData();

        int actionCode = getArguments().getInt("actionCode");

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 2) {
                //TODO удаление семестра из базы

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", 2);
                Navigation.findNavController(view).navigate(R.id.action_search_semesters_to_admin_actions, bundle);
            } else if (actionCode == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("semesterYear", String.valueOf(semesters.get(position).getYear()));
                Navigation.findNavController(view).navigate(R.id.action_search_semesters_to_dialog_semester_editing, bundle);
            }
        };
        semestersAdapter = new SemestersAdapter(getContext(), semesters, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedSemestersList);
        recyclerView.setAdapter(semestersAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.semestersSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск всех семестров
        semesters = new ArrayList<>();
        semesters.add(new Semester(2018, true));
        semesters.add(new Semester(2018, false));
        semesters.add(new Semester(2019, true));
        semesters.add(new Semester(2019, false));
        semesters.add(new Semester(2020, true));
        semesters.add(new Semester(2020, false));
        semesters.add(new Semester(2021, true));
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                semestersAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}