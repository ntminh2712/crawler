package dto;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import entity.Article;
import entity.Category;
import entity.Source;

public class ArticleDto {

    @Id
    private long id;
    private String url;
    private String title;
    private String description;
    private String content;
    private String author;
    private String link;
    private String thumnail;
    private Category category;
    private Source source;
    private long created_at;
    private long update_at;
    @Index
    private int status;

    public ArticleDto() {

    }

    public ArticleDto(Article article, Category category, Source source) {
        this.id = article.getId();
        this.url = article.getUrl();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.link = article.getLink();
        this.category = category;
        this.source = source;
        this.thumnail = article.getThumnail();
        this.created_at = article.getCreated_at();
        this.update_at = article.getUpdate_at();
        this.status = article.getStatus();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
