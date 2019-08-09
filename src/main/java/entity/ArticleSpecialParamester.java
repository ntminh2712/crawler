package entity;

public class ArticleSpecialParamester {
    private String url;
    private String title_selector;
    private String link_selector;
    private String content_selector;
    private String remove_selector;
    private String author_selector;
    private String description_selector;
    private String thumnail_selector;
    private long category_id;

    public ArticleSpecialParamester() {
    }

    public ArticleSpecialParamester(String url, String title_selector, String link_selector, String content_selector, String remove_selector, String author_selector, String description_selector, String thumnail_selector, long category_id) {
        this.url = url;
        this.title_selector = title_selector;
        this.link_selector = link_selector;
        this.content_selector = content_selector;
        this.remove_selector = remove_selector;
        this.author_selector = author_selector;
        this.description_selector = description_selector;
        this.thumnail_selector = thumnail_selector;
        this.category_id = category_id;
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

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }
}
