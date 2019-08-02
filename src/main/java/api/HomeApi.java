package api;

import dto.ArticleDto;
import entity.Article;
import entity.Category;
import entity.JsonResponse;
import entity.Source;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<ArticleDto> listArticleDto = new ArrayList<>();
        List<Article> list = ofy().load().type(Article.class).filter("status",1).offset(Integer.parseInt(off_set)).limit(Integer.parseInt(limit)).list();
        for (Article article: list){
            Category category = ofy().load().type(Category.class).id(article.getCategory_id()).now();
            Source source = ofy().load().type(Source.class).id(article.getSource_id()).now();
            listArticleDto.add( new ArticleDto(article,category,source));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(listArticleDto)
                .toJsonString());
    }
}
