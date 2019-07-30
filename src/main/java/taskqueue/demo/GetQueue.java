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
            Source source = ofy().load().type(Source.class).id(article.getSource_id()).now();
            String title = document.select(source.getTitle_selector()).text();
            String author = document.select(source.getAuthor_selector()).text();
            String description = document.select(source.getDescription_selector()).text();
            String link = document.select(source.getLink_selector()).text();
            String content = document.select(source.getContent_selector()).html();
            article.setId(Calendar.getInstance().getTimeInMillis());
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            article.setAuthor(author);
            article.setLink(link);
            article.setStatus(1);
            article.setCategory_id(source.getCategory_id());
            article.setUpdate_at(Calendar.getInstance().getTimeInMillis());
            article.setCreated_at(Calendar.getInstance().getTimeInMillis());
            ofy().save().entity(article).now();
        }

    }
}
