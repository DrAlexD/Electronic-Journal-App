package com.example.electronic_journal.group.actions.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.SubjectInfo;

public class ModuleEditingViewModel extends ViewModel {
    private final MutableLiveData<ModuleFormState> moduleFormState = new MutableLiveData<>();
    private final MutableLiveData<Module> module = new MutableLiveData<>();
    private MutableLiveData<Integer> answer = new MutableLiveData<>();

    public LiveData<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Integer> answer) {
        this.answer = answer;
    }

    LiveData<ModuleFormState> getModuleFormState() {
        return moduleFormState;
    }

    public LiveData<Module> getModule() {
        return module;
    }

    public void moduleEditingDataChanged(String minPoints) {
        moduleFormState.setValue(new ModuleFormState(minPoints));
    }

    public void downloadModuleById(long moduleId) {
        Repository.getInstance().getModuleById(moduleId, module);
    }

    public void editModule(long moduleId, int moduleNumber, SubjectInfo subjectInfo, int minPoints, int maxPoints) {
        Repository.getInstance().editModule(moduleId, new Module(moduleNumber, subjectInfo, minPoints, maxPoints), answer);
    }
}