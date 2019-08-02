package dto;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import entity.Category;
import entity.Source;

public class SourceDto {

    @Id
    private long id;
    private String url;
    private String title_selector;
    private String link_selector;
    private String content_selector;
    private String remove_selector;
    private String author_selector;
    private String description_selector;
    private String thumnail_selector;
    private Category category;
    private long created_at;
    private long update_at;
    @Index
    private int status;

    public SourceDto() {
    }

    public SourceDto(Source source, Category category) {
        this.id = source.getId();
        this.url = source.getUrl();
        this.title_selector = source.getTitle_selector();
        this.link_selector = source.getLink_selector();
        this.content_selector = source.getContent_selector();
        this.remove_selector = source.getRemove_selector();
        this.author_selector = source.getAuthor_selector();
        this.description_selector = source.getDescription_selector();
        this.thumnail_selector = source.getThumnail_selector();
        this.category = category;
        this.created_at = source.getCreated_at();
        this.update_at = source.getUpdate_at();
        this.status = source.getStatus();

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

    public String getTitle_selector() {
        return title_selector;
    }

    public void setTitle_selector(String title_selector) {
        this.title_selector = title_selector;
    }

    public String getLink_selector() {
        return link_selector;
    }

    public void setLink_selector(String link_selector) {
        this.link_selector = link_selector;
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

    public String getAuthor_selector() {
        return author_selector;
    }

    public void setAuthor_selector(String author_selector) {
        this.author_selector = author_selector;
    }

    public String getDescription_selector() {
        return description_selector;
    }

    public void setDescription_selector(String description_selector) {
        this.description_selector = description_selector;
    }

    public String getThumnail_selector() {
        return thumnail_selector;
    }

    public void setThumnail_selector(String thumnail_selector) {
        this.thumnail_selector = thumnail_selector;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
