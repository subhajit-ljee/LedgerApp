package com.sourav.ledgerproject.profile.addclient.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Provider;

public class ClientViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;
    public ClientViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators){
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Provider<ViewModel> creator = creators.get(modelClass);
        if(creator==null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                if(modelClass.isAssignableFrom(entry.getKey())){
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if(creator==null){
            throw new IllegalArgumentException("class not found "+modelClass);
        }

        try{
            return (T) creator.get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
