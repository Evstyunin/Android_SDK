package ru.usedesk.chat_gui.internal.chat;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ChatViewModelFactory(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel();
    }
}
