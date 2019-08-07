package taskqueue.demo;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import entity.Article;
import entity.CrawlerSource;
import entity.Source;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GetQueue extends HttpServlet {
    private static Queue q = QueueFactory.getQueue("add-queue");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TaskHandle> tasks = q.leaseTasks(10, TimeUnit.SECONDS, 1);
        if (tasks.size() > 0) {
            TaskHandle taskHandle = tasks.get(0);
            String articleObjectJson = new String(taskHandle.getPayload());
            Article article = new Gson().fromJson(articleObjectJson, Article.class);
            Document document = Jsoup.connect(article.getUrl()).ignoreContentType(true).get();
            Elements metaTags = document.getElementsByTag("meta");
            for (Element metaTag : metaTags) {
                String contentTag = metaTag.attr("content");
                String name = metaTag.attr("name");
                String property = metaTag.attr("property");

                if("description".equals(name)) {
                    article.setDescription(contentTag);
                }
                if("og:image".equals(property)) {
                    article.setThumnail(contentTag);
                }
            }
            Source source = ofy().load().type(Source.class).id(article.getSource_id()).now();
            String title = document.select(source.getTitle_selector()).text();
            String author = document.select(source.getAuthor_selector()).text();
            String description = document.select(source.getDescription_selector()).text();
            String link = article.getUrl();
            String content = document.select(source.getContent_selector()).html();
            article.setId(Calendar.getInstance().getTimeInMillis());
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            article.setAuthor(author);
            article.setStatus(-1);
            article.setLink(link);
            article.setCategory_id(source.getCategory_id());
            article.setUpdate_at(Calendar.getInstance().getTimeInMillis());
            article.setCreated_at(Calendar.getInstance().getTimeInMillis());
            if(ofy().load().type(Article.class).filter("link", article.getLink()).list() == null || ofy().load().type(Article.class).filter("link", article.getLink()).list().size() == 0){
                ofy().save().entity(article).now();
            }

        }

    }
}
