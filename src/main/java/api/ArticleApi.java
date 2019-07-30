package api;

import Util.StringUtil;
import com.google.gson.Gson;
import entity.Article;
import entity.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ArticleApi extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArticleApi.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("id");
        String limit = req.getParameter("limit");
        String off_set = req.getParameter("off_set");
        if (strId == null || strId.length() == 0) {
            if (limit == null || off_set == null) {
                getListArticle(req, resp, "10", "0");
            } else {
                getListArticle(req, resp, limit, off_set);
            }
        } else {
            getdetailArticle(req, resp, strId);
        }
    }

    private void getListArticle(HttpServletRequest req, HttpServletResponse resp, String limit, String off_set) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(ofy().load().type(Article.class).filter("status", 1).offset(Integer.parseInt(off_set)).limit(Integer.parseInt(limit)).list())
                .toJsonString());
    }
    private void getdetailArticle(HttpServletRequest req, HttpServletResponse resp, String strId) throws IOException {
        Article article = ofy().load().type(Article.class).id(Long.parseLong(strId)).now();
        if (article == null || article.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("category is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(article)
                .toJsonString());
        System.out.println("get detail");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            Article article = gson.fromJson(content, Article.class);
            ofy().save().entities(article).now();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_CREATED)
                    .setMessage("Success!")
                    .setData(article).toJsonString());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Can not create video. Error: %s", ex.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not create video!")
                    .toJsonString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    .setMessage("Bad request!")
                    .toJsonString());
            return;
        }
        Article existArticle = ofy().load().type(Article.class).id(Long.parseLong(strId)).now();
        if (existArticle == null || existArticle.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Video is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        existArticle.setStatus(-1);
        existArticle.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        ofy().save().entity(existArticle).now();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Delete Success!")
                .toJsonString());
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    .setMessage("Bad request!")
                    .toJsonString());
            return;
        }
        Article exitsArticle = ofy().load().type(Article.class).id(Long.parseLong(strId)).now();
        if (exitsArticle == null || exitsArticle.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Video is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        String content = StringUtil.convertInputStreamToString(req.getInputStream());
        Article updateArticle = gson.fromJson(content, Article.class);
        exitsArticle.setLink(updateArticle.getLink());
        exitsArticle.setUrl(updateArticle.getUrl());
        exitsArticle.setTitle(updateArticle.getTitle());
        exitsArticle.setDescription(updateArticle.getDescription());
        exitsArticle.setContent(updateArticle.getContent());
        exitsArticle.setSource_id(updateArticle.getSource_id());
        exitsArticle.setCategory_id(updateArticle.getCategory_id());
        exitsArticle.setAuthor(updateArticle.getAuthor());
        exitsArticle.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        ofy().save().entity(updateArticle).now();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Update Success!")
                .setData(updateArticle)
                .toJsonString());
    }


}
