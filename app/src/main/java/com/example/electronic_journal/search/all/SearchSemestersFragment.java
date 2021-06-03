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
import com.example.electronic_journal.search.SemestersAdapter;

public class SearchSemestersFragment extends Fragment {
    private SemestersAdapter semestersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_semesters, container, false);

        SearchSemestersViewModel searchSemestersViewModel = new ViewModelProvider(this).get(SearchSemestersViewModel.class);
        searchSemestersViewModel.downloadSemesters();

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedSemestersList);
        searchSemestersViewModel.getSemesters().observe(getViewLifecycleOwner(), semesters -> {
            if (semesters != null) {
                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 2) {
                        searchSemestersViewModel.deleteSemester(semestersAdapter.getSemesters().get(position).getId());

                        searchSemestersViewModel.getAnswer().observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешное удаление семестра", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", 2);
                                Navigation.findNavController(view).navigate(R.id.action_search_semesters_to_admin_actions, bundle);
                            }
                        });
                    }
                };

                semestersAdapter = new SemestersAdapter(getContext(), semesters, onItemClickListener);
                recyclerView.setAdapter(semestersAdapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        final SearchView searchView = root.findViewById(R.id.semestersSearch);
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
                semestersAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}