package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.ProfessorFormState;

public class ProfessorAddingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private final MutableLiveData<Long> lastProfessorId = new MutableLiveData<>();

    LiveData<ProfessorFormState> getProfessorFormState() {
        return professorFormState;
    }

    public LiveData<Long> getLastProfessorId() {
        return lastProfessorId;
    }

    public void professorAddingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                           String professorPassword, boolean isNameOrSecondNameChanged) {
        professorFormState.setValue(new ProfessorFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void downloadLastProfessorId() {
        this.lastProfessorId.setValue(Repository.getInstance().getLastProfessorId());
    }

    public void addProfessor(String professorName, String professorSecondName, String professorLogin, String professorPassword) {
        Repository.getInstance().addProfessor(professorName, professorSecondName, professorLogin, professorPassword);
    }
}