package taskqueue.demo;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import entity.Article;
import entity.CrawlerSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GetQueue extends HttpServlet {
    private static Queue q = QueueFactory.getQueue("add-queue");
    static {
        ObjectifyService.register(Article.class);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TaskHandle> tasks = q.leaseTasks(10, TimeUnit.SECONDS, 1);
        if (tasks.size() > 0) {
            TaskHandle taskHandle = tasks.get(0);
            String articleObjectJson = new String(taskHandle.getPayload());
            Article article = new Gson().fromJson(articleObjectJson, Article.class);
            Document document = Jsoup.connect(article.getUrl()).ignoreContentType(true).get();

            CrawlerSource crawlerSource = AddQueue.crawlerSource; // lấy source theo id của article từ database;
            String title = document.select(crawlerSource.getTitleSelector()).text();
            String description = document.select(crawlerSource.getDescriptionSelector()).text();
            String content = document.select(crawlerSource.getContentSelector()).text();
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            System.out.print("get queue");
            ofy().save().entity(article).now();
        }

    }
}
