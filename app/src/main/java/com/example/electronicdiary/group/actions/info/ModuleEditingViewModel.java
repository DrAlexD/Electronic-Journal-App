package com.example.electronicdiary.group.actions.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class ModuleEditingViewModel extends ViewModel {
    private final MutableLiveData<ModuleFormState> moduleFormState = new MutableLiveData<>();
    private final MutableLiveData<Module> module = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<ModuleFormState> getModuleFormState() {
        return moduleFormState;
    }

    public LiveData<Module> getModule() {
        return module;
    }

    public void moduleEditingDataChanged(String minPoints, String maxPoints) {
        moduleFormState.setValue(new ModuleFormState(minPoints, maxPoints));
    }

    public void downloadModuleById(long moduleId) {
        Repository.getInstance().getModuleById(moduleId, module);
    }

    public void editModule(long moduleId, int moduleNumber, SubjectInfo subjectInfo, int minPoints, int maxPoints) {
        Repository.getInstance().editModule(moduleId, new Module(moduleNumber, subjectInfo, minPoints, maxPoints), answer);
    }
}