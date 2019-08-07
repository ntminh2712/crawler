package entity;

import java.util.List;

public class Approval {

    List<Long> articleId;
    List<Long> listIdError;

    public Approval() {
    }

    public Approval(List<Long> articleId, List<Long> listIdError) {
        this.articleId = articleId;
        this.listIdError = listIdError;
    }

    public void setArticleId(List<Long> articleId) {
        this.articleId = articleId;
    }

    public List<Long> getArticleId() {
        return articleId;
    }

    public List<Long> getListIdError() {
        return listIdError;
    }

    public void setListIdError(List<Long> listIdError) {
        this.listIdError = listIdError;
    }
}
