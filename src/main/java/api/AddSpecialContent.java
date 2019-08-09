package api;

import Util.StringUtil;
import com.google.gson.Gson;
import dto.ArticleDto;
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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AddSpecialContent extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArticleApi.class.getName());
    private static Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String token = req.getHeader("token");
            if (token == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                        .setMessage("Token cannot blank!").toJsonString());
                return;
            }
            Credential credential = ofy().load().type(Credential.class).id(token).now();
            if (credential.getTimeRevoke() >= Calendar.getInstance().getTimeInMillis()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                        .setMessage("Token revoke, please login again ").toJsonString());
            }else {
                String content = StringUtil.convertInputStreamToString(req.getInputStream());
                ArticleSpecialParamester articleSpecialParamester = gson.fromJson(content, ArticleSpecialParamester.class);
                Article article = new Article();
                Document document = Jsoup.connect(articleSpecialParamester.getUrl()).ignoreContentType(true).get();
                Elements metaTags = document.getElementsByTag("meta");
                for (Element metaTag : metaTags) {
                    String contentTag = metaTag.attr("content");
                    String name = metaTag.attr("name");
                    String property = metaTag.attr("property");

                    if ("description".equals(name)) {
                        article.setDescription(contentTag);
                    }
                    if ("og:image".equals(property)) {
                        article.setThumnail(contentTag);
                    }
                }
                article.setTitle(document.select(articleSpecialParamester.getTitle_selector()).text());
                article.setAuthor(document.select(articleSpecialParamester.getAuthor_selector()).text());
                article.setDescription(document.select(articleSpecialParamester.getDescription_selector()).text());
                article.setLink(articleSpecialParamester.getUrl());
                article.setContent(document.select(articleSpecialParamester.getContent_selector()).html());
                article.setStatus(-1);
                article.setCategory_id(articleSpecialParamester.getCategory_id());
                ofy().save().entity(article).now();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_OK)
                        .setMessage("Success!")
                        .setData(article)
                        .toJsonString());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Can not create article. Error: %s", ex.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not create article!")
                    .toJsonString());
        }
    }
}
