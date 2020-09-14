package ru.usedesk.chat_gui.internal.chat;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.usedesk.chat_sdk.external.IUsedeskChat;
import ru.usedesk.chat_sdk.external.UsedeskChatSdk;
import ru.usedesk.chat_sdk.external.entity.UsedeskActionListenerRx;
import ru.usedesk.chat_sdk.external.entity.UsedeskChatConfiguration;
import ru.usedesk.chat_sdk.external.entity.UsedeskFeedback;
import ru.usedesk.chat_sdk.external.entity.UsedeskFileInfo;
import ru.usedesk.chat_sdk.external.entity.UsedeskMessage;
import ru.usedesk.chat_sdk.external.entity.UsedeskOfflineForm;
import ru.usedesk.common_sdk.external.entity.exceptions.UsedeskException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Set<Integer>> feedbacksLiveData;
    private MutableLiveData<UsedeskException> exceptionLiveData;
    private MutableLiveData<MessagePanelState> messagePanelStateLiveData;
    private MutableLiveData<List<UsedeskMessage>> messagesLiveData;
    private MutableLiveData<List<UsedeskFileInfo>> fileInfoListLiveData;
    private MutableLiveData<String> messageLiveData;
    private MutableLiveData<String> nameLiveData;
    private MutableLiveData<String> emailLiveData;

    private IUsedeskChat usedeskChat = null;

    public void init(Context context, @Nullable UsedeskChatConfiguration usedeskChatConfiguration) {
        if (usedeskChatConfiguration != null) {
            UsedeskChatSdk.setConfiguration(usedeskChatConfiguration);
        }
        UsedeskActionListenerRx actionListenerRx = new UsedeskActionListenerRx();
        usedeskChat = UsedeskChatSdk.init(context, actionListenerRx);

        feedbacksLiveData = new MutableLiveData<>();
        exceptionLiveData = new MutableLiveData<>();
        messagePanelStateLiveData = new MutableLiveData<>(MessagePanelState.MESSAGE_PANEL);
        messagesLiveData = new MutableLiveData<>();
        fileInfoListLiveData = new MutableLiveData<>();
        messageLiveData = new MutableLiveData<>("");
        nameLiveData = new MutableLiveData<>("");
        emailLiveData = new MutableLiveData<>("");

        clearFileInfoList();

        toLiveData(actionListenerRx.getMessagesObservable(), messagesLiveData);
        toLiveData(actionListenerRx.getOfflineFormExpectedObservable()
                .map(configuration -> {
                    nameLiveData.postValue(configuration.getClientName());
                    emailLiveData.postValue(configuration.getEmail());
                    return MessagePanelState.OFFLINE_FORM_EXPECTED;
                }), messagePanelStateLiveData);
        toLiveData(actionListenerRx.getExceptionObservable(), exceptionLiveData);

        disposables.add(actionListenerRx.getConnectedStateSubject()
                .subscribe(connected -> {
                    if (!connected) {
                        justComplete(this.usedeskChat.connectRx());
                    }
                }));

        feedbacksLiveData.setValue(new HashSet<>());
    }

    public void dispose() {
        disposables.clear();
        UsedeskChatSdk.release();
    }

    public void reinit(Context context, @Nullable UsedeskChatConfiguration usedeskChatConfiguration) {
        dispose();
        init(context, usedeskChatConfiguration);
    }

    void onMessageChanged(@NonNull String message) {
        messageLiveData.setValue(message);
    }

    private void clearFileInfoList() {
        fileInfoListLiveData.setValue(new ArrayList<>());
    }

    private void justComplete(@NonNull Completable completable) {
        addDisposable(completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, Throwable::printStackTrace));
    }

    private <OUT extends IN, IN> void toLiveData(@NonNull Observable<OUT> observable, @NonNull MutableLiveData<IN> liveData) {
        addDisposable(observable.subscribe(liveData::postValue, Throwable::printStackTrace));
    }

    private void addDisposable(@NonNull Disposable disposable) {
        disposables.add(disposable);
    }

    @NonNull
    public LiveData<Set<Integer>> getFeedbacksLiveData() {
        return feedbacksLiveData;
    }

    @NonNull
    public LiveData<UsedeskException> getExceptionLiveData() {
        return exceptionLiveData;
    }

    @NonNull
    LiveData<MessagePanelState> getMessagePanelStateLiveData() {
        return messagePanelStateLiveData;
    }

    @NonNull
    LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    @NonNull
    LiveData<String> getNameLiveData() {
        return nameLiveData;
    }

    @NonNull
    LiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    @NonNull
    public LiveData<List<UsedeskMessage>> getMessagesLiveData() {
        return messagesLiveData;
    }

    @NonNull
    public LiveData<List<UsedeskFileInfo>> getFileInfoListLiveData() {
        return fileInfoListLiveData;
    }

    public void setAttachedFileInfoList(@NonNull List<UsedeskFileInfo> usedeskFileInfoList) {
        fileInfoListLiveData.postValue(usedeskFileInfoList);
    }

    @SuppressWarnings("ConstantConditions")
    void sendFeedback(int messageIndex, @NonNull UsedeskFeedback feedback) {
        Set<Integer> feedbacks = new HashSet<>(feedbacksLiveData.getValue().size() + 1);
        feedbacks.addAll(feedbacksLiveData.getValue());
        feedbacks.add(messageIndex);
        feedbacksLiveData.postValue(feedbacks);

        justComplete(usedeskChat.sendRx(feedback));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dispose();
    }

    @SuppressWarnings("ConstantConditions")
    void detachFile(@NonNull UsedeskFileInfo usedeskFileInfo) {
        List<UsedeskFileInfo> attachedFileInfoList = new ArrayList<>(fileInfoListLiveData.getValue());
        attachedFileInfoList.remove(usedeskFileInfo);
        setAttachedFileInfoList(attachedFileInfoList);
    }

    void onSend(@NonNull String textMessage) {
        justComplete(usedeskChat.sendRx(textMessage));
        justComplete(usedeskChat.sendRx(fileInfoListLiveData.getValue()));

        clearFileInfoList();
    }

    void onSend(@NonNull String name, @NonNull String email, @NonNull String message) {
        justComplete(usedeskChat.sendRx(new UsedeskOfflineForm(name, email, message))
                .doOnComplete(() -> messagePanelStateLiveData.postValue(MessagePanelState.OFFLINE_FORM_SENT)));
    }

    void onNameChanged(@NonNull String name) {
        nameLiveData.setValue(name);
    }

    void onEmailChanged(@NonNull String email) {
        emailLiveData.setValue(email);
    }
}
