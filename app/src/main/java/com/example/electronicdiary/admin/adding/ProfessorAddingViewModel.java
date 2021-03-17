package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class ProfessorAddingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorAddingFormState> professorAddingFormState = new MutableLiveData<>();
    private final MutableLiveData<Integer> lastProfessorId = new MutableLiveData<>();

    LiveData<ProfessorAddingFormState> getProfessorAddingFormState() {
        return professorAddingFormState;
    }

    public LiveData<Integer> getLastProfessorId() {
        return lastProfessorId;
    }

    public void professorAddingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                           String professorPassword, boolean isNameOrSecondNameChanged) {
        professorAddingFormState.setValue(new ProfessorAddingFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void downloadLastProfessorId() {
        this.lastProfessorId.setValue(Repository.getInstance().getLastProfessorId());
    }

    public void addProfessor(String professorName, String professorSecondName, String professorLogin, String professorPassword) {
        Repository.getInstance().addProfessor(professorName, professorSecondName, professorLogin, professorPassword);
    }
}