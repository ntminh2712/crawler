package api;

import entity.Article;
import entity.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class HomeApi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getArticleHome(req, resp);
    }

    private void getArticleHome(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String limit = req.getParameter("limit");
        String off_set = req.getParameter("off_set");
        if (limit == null || off_set == null) {
            getListHome(req, resp, "10", "0");
        } else {
            getListHome(req, resp, limit, off_set);
        }
    }

    private void getListHome(HttpServletRequest req, HttpServletResponse resp, String limit, String off_set) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(ofy().load().type(Article.class).filter("status", 1).offset(Integer.parseInt(off_set)).limit(Integer.parseInt(limit)).list())
                .toJsonString());
    }
}
