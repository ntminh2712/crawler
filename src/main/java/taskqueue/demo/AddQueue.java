package taskqueue.demo;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import entity.Article;
import entity.ArticleBuilder;
import entity.CrawlerSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class AddQueue extends HttpServlet {

    private static Queue q = QueueFactory.getQueue("add-queue");
    private static String indexPageLink = "https://vnexpress.net/giai-tri";
    private static String linkSelector = "article.list_news h4.title_news a:first-child";
    private static String titleSelector = "h1.title_news_detail";
    private static String descriptionSelector = ".sidebar_1 p.description";
    private static String contentSelector = "article.content_detail";
    private static String authorSelector = "article.content_detail";
    public static CrawlerSource crawlerSource;

    static {
        // tạo 1 crawler source làm mẫu.
        if (crawlerSource == null) {
            crawlerSource = new CrawlerSource();
            crawlerSource.setId(Calendar.getInstance().getTimeInMillis());
            crawlerSource.setUrl(indexPageLink);
            crawlerSource.setLinkSelector(linkSelector);
            crawlerSource.setTitleSelector(titleSelector);
            crawlerSource.setDescriptionSelector(descriptionSelector);
            crawlerSource.setContentSelector(contentSelector);
            crawlerSource.setAuthorSelector(authorSelector);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("addQueue");
        Document document = Jsoup.connect(crawlerSource.getUrl()).ignoreContentType(true).get();
        Elements elements = document.select(crawlerSource.getLinkSelector());
        for (Element el :
                elements) {
            String link = el.attr("href").trim();
            Article article = ArticleBuilder.anArticle().withUrl(link).withSourceId(crawlerSource.getId()).build();
            q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(new Gson().toJson(article)));
        }
    }
}
