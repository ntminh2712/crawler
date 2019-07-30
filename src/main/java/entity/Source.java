package entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;

@Entity
public class Source {
    @Id
    private long id;
    private String url;
    private String title_selector;
    private String link_selector;
    private String content_selector;
    private String remove_selector;
    private String author_selector;
    private String description_selector;
    private long category_id;
    private long created_at;
    private long update_at;
    @Index
    private int status;

    public Source() {
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

    public String getTitle_selector() {
        return title_selector;
    }

    public void setTitle_selector(String title_selector) {
        this.title_selector = title_selector;
    }

    public String getContent_selector() {
        return content_selector;
    }

    public void setContent_selector(String content_selector) {
        this.content_selector = content_selector;
    }

    public String getRemove_selector() {
        return remove_selector;
    }

    public void setRemove_selector(String remove_selector) {
        this.remove_selector = remove_selector;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
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

    public long getId() {
        return id;
    }

    public String getAuthor_selector() {
        return author_selector;
    }

    public void setAuthor_selector(String author_selector) {
        this.author_selector = author_selector;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription_selector() {
        return description_selector;
    }

    public void setDescription_selector(String description_selector) {
        this.description_selector = description_selector;
    }

    public String getLink_selector() {
        return link_selector;
    }

    public void setLink_selector(String link_selector) {
        this.link_selector = link_selector;
    }

    public boolean isDeactive() {
        return this.status == 0 || this.status == - 1;
    }
}
