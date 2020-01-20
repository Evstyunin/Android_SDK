package ru.usedesk.knowledgebase_gui.screens.pages.articlebody;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.usedesk.common_gui.external.IUsedeskViewCustomizer;
import ru.usedesk.knowledgebase_gui.R;
import ru.usedesk.knowledgebase_sdk.external.entity.ArticleBody;

public class ArticlesBodyAdapter extends RecyclerView.Adapter<ArticlesBodyAdapter.ArticleViewHolder> {

    private final List<ArticleBody> articleInfoList;
    private final IOnArticleBodyClickListener onArticleClickListener;
    private final IUsedeskViewCustomizer usedeskViewCustomizer;

    ArticlesBodyAdapter(@NonNull List<ArticleBody> articleInfoList,
                        @NonNull IOnArticleBodyClickListener onArticleClickListener,
                        @NonNull IUsedeskViewCustomizer usedeskViewCustomizer) {
        this.articleInfoList = articleInfoList;
        this.onArticleClickListener = onArticleClickListener;
        this.usedeskViewCustomizer = usedeskViewCustomizer;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = usedeskViewCustomizer.createView(viewGroup, R.layout.usedesk_item_article_info, R.style.Usedesk_Theme_KnowledgeBase);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i) {
        articleViewHolder.bind(articleInfoList.get(i));
    }

    @Override
    public int getItemCount() {
        return articleInfoList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewCount;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewCount = itemView.findViewById(R.id.tv_count);
        }

        void bind(@NonNull final ArticleBody articleBody) {
            textViewTitle.setText(articleBody.getTitle());
            textViewCount.setText(Integer.toString(articleBody.getViews()));

            itemView.setOnClickListener(v -> onArticleClickListener.onArticleBodyClick(articleBody.getId()));
        }
    }
}
