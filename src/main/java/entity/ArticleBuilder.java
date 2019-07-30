package entity;

public final class ArticleBuilder {
    private long id;
    private String url;
    private String title;
    private String description;
    private String content;
    private String author;
    private String link;
    private long category_id;
    private long source_id;
    private long created_at;
    private long update_at;
    private int status;

    private ArticleBuilder() {
    }

    public static ArticleBuilder anArticle() {
        return new ArticleBuilder();
    }

    public ArticleBuilder withId(long id) {
        this.id = id;
        return this;
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

    public ArticleBuilder withLink(String link) {
        this.link = link;
        return this;
    }

    public ArticleBuilder withCategory_id(long category_id) {
        this.category_id = category_id;
        return this;
    }

    public ArticleBuilder withSource_id(long source_id) {
        this.source_id = source_id;
        return this;
    }

    public ArticleBuilder withCreated_at(long created_at) {
        this.created_at = created_at;
        return this;
    }

    public ArticleBuilder withUpdate_at(long update_at) {
        this.update_at = update_at;
        return this;
    }

    public ArticleBuilder withStatus(int status) {
        this.status = status;
        return this;
    }

    public Article build() {
        Article article = new Article();
        article.setId(id);
        article.setUrl(url);
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setAuthor(author);
        article.setLink(link);
        article.setCategory_id(category_id);
        article.setSource_id(source_id);
        article.setCreated_at(created_at);
        article.setUpdate_at(update_at);
        article.setStatus(status);
        return article;
    }
}
