package entity;

public final class ArticleBuilder {
    private String url;
    private String title;
    private String description;
    private String content;
    private String author;
    private long sourceId;
    private long createdAtMLS;
    private long updatedAtMLS;
    private long deletedAtMLS;
    private int status;

    private ArticleBuilder() {
    }

    public static ArticleBuilder anArticle() {
        return new ArticleBuilder();
    }

    public ArticleBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public ArticleBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ArticleBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ArticleBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public ArticleBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public ArticleBuilder withSourceId(long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public ArticleBuilder withCreatedAtMLS(long createdAtMLS) {
        this.createdAtMLS = createdAtMLS;
        return this;
    }

    public ArticleBuilder withUpdatedAtMLS(long updatedAtMLS) {
        this.updatedAtMLS = updatedAtMLS;
        return this;
    }

    public ArticleBuilder withDeletedAtMLS(long deletedAtMLS) {
        this.deletedAtMLS = deletedAtMLS;
        return this;
    }

    public ArticleBuilder withStatus(int status) {
        this.status = status;
        return this;
    }

    public Article build() {
        Article article = new Article();
        article.setUrl(url);
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setAuthor(author);
        article.setSourceId(sourceId);
        article.setCreatedAtMLS(createdAtMLS);
        article.setUpdatedAtMLS(updatedAtMLS);
        article.setDeletedAtMLS(deletedAtMLS);
        article.setStatus(status);
        return article;
    }
}
