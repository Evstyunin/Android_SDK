package ru.usedesk.sdk.domain.interactor.knowledgebase;


import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import ru.usedesk.sdk.domain.boundaries.IKnowledgeBaseRepository;
import ru.usedesk.sdk.domain.boundaries.IUserInfoRepository;
import ru.usedesk.sdk.domain.entity.exceptions.ApiException;
import ru.usedesk.sdk.domain.entity.exceptions.DataNotFoundException;
import ru.usedesk.sdk.domain.entity.knowledgebase.ArticleBody;
import ru.usedesk.sdk.domain.entity.knowledgebase.ArticleInfo;
import ru.usedesk.sdk.domain.entity.knowledgebase.Section;

public class KnowledgeBaseInteractor implements IKnowledgeBaseInteractor {

    private IUserInfoRepository userInfoRepository;
    private IKnowledgeBaseRepository knowledgeRepository;
    private Scheduler workScheduler;
    private Scheduler mainThreadScheduler;

    @Inject
    KnowledgeBaseInteractor(IUserInfoRepository userInfoRepository,
                            IKnowledgeBaseRepository knowledgeRepository,
                            @Named("work") Scheduler workScheduler,
                            @Named("main") Scheduler mainThreadScheduler) {
        this.userInfoRepository = userInfoRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.workScheduler = workScheduler;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    @NonNull
    public Single<List<Section>> getSectionsSingle() {
        return Single.create(
                (SingleOnSubscribe<List<Section>>) emitter -> emitter.onSuccess(getSections()))
                .subscribeOn(workScheduler)
                .observeOn(mainThreadScheduler);
    }

    @Override
    @NonNull
    public Single<ArticleBody> getArticleSingle(@NonNull ArticleInfo articleInfo) {
        return Single.create(
                (SingleOnSubscribe<ArticleBody>) emitter -> emitter.onSuccess(getArticle(articleInfo)))
                .subscribeOn(workScheduler)
                .observeOn(mainThreadScheduler);
    }

    @Override
    @NonNull
    public Single<List<ArticleBody>> getArticlesSingle(@NonNull String searchQuery) {
        return Single.create(
                (SingleOnSubscribe<List<ArticleBody>>) emitter -> emitter.onSuccess(getArticles(searchQuery)))
                .subscribeOn(workScheduler)
                .observeOn(mainThreadScheduler);
    }

    @NonNull
    private List<Section> getSections() throws DataNotFoundException, ApiException {
        String id = userInfoRepository.getConfiguration().getCompanyId();
        String token = userInfoRepository.getToken();

        id = "4";
        token = "11eb3f39dec94ecf0fe4a80349903e6ad5ce6d75";

        return knowledgeRepository.getSections(id, token);
    }

    @NonNull
    private ArticleBody getArticle(@NonNull ArticleInfo articleInfo) throws DataNotFoundException,
            ApiException {
        String id = userInfoRepository.getConfiguration().getCompanyId();
        String token = userInfoRepository.getToken();

        id = "4";
        token = "11eb3f39dec94ecf0fe4a80349903e6ad5ce6d75";

        return knowledgeRepository.getArticle(id, token, articleInfo);
    }

    @NonNull
    private List<ArticleBody> getArticles(@NonNull String searchQuery) throws DataNotFoundException,
            ApiException {
        String id = userInfoRepository.getConfiguration().getCompanyId();
        String token = userInfoRepository.getToken();

        id = "4";
        token = "11eb3f39dec94ecf0fe4a80349903e6ad5ce6d75";

        return knowledgeRepository.getArticles(id, token, searchQuery);
    }
}
