package taskqueue.demo;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import entity.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AddQueue extends HttpServlet {

    private static Queue q = QueueFactory.getQueue("add-queue");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("addQueue");
        List<Source> listSource = ofy().load().type(Source.class).filter("status", 1).list();
        for (Source source : listSource) {
            Document document = Jsoup.connect(source.getUrl()).ignoreContentType(true).get();
            Elements elementsLink = document.select(source.getLink_selector());
            for (int i = 0; i < elementsLink.size() - 1;i ++ ){
                String url = elementsLink.get(i).attr("href").trim();
                Article article = ArticleBuilder.anArticle().withCategory_id(source.getCategory_id()).withUrl(url).withSource_id(source.getId()).build();
                q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(new Gson().toJson(article)));
            }
        }
    }
}
