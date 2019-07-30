package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;

@Entity
public class Article {

    @Id
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
    @Index
    private int status;

    public Article() {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.created_at = Calendar.getInstance().getTimeInMillis();
        this.update_at = Calendar.getInstance().getTimeInMillis();
        this.status = 1;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getSource_id() {
        return source_id;
    }

    public void setSource_id(long source_id) {
        this.source_id = source_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public boolean isDeactive() {
        return this.status == 0 || this.status == -1;
    }
}
