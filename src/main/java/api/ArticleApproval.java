package api;

import Util.StringUtil;
import com.google.api.client.util.IOUtils;
import com.google.gson.Gson;
import dto.ArticleDto;
import entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ArticleApproval extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArticleApi.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String limit = req.getParameter("limit");
        String off_set = req.getParameter("off_set");
        if (limit == null || off_set == null) {
            getListArticle(req, resp, "10", "0");
        } else {
            getListArticle(req, resp, limit, off_set);
        }
    }

    private void getListArticle(HttpServletRequest req, HttpServletResponse resp, String limit, String off_set) throws IOException {
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
            List<ArticleDto> listArticleDto = new ArrayList<>();
            List<Article> list = ofy().load().type(Article.class).filter("status", -1).offset(Integer.parseInt(off_set)).limit(Integer.parseInt(limit)).list();
            for (Article article : list) {
                Category category = ofy().load().type(Category.class).id(article.getCategory_id()).now();
                Source source = ofy().load().type(Source.class).id(article.getSource_id()).now();
                listArticleDto.add(new ArticleDto(article, category, source));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_OK)
                    .setMessage("Success!")
                    .setData(listArticleDto)
                    .toJsonString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        if (token == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    .setMessage("Token cannot blank!").toJsonString());
        }
        Credential credential = ofy().load().type(Credential.class).id(token).now();
        if (credential.getTimeRevoke() >= Calendar.getInstance().getTimeInMillis()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    .setMessage("Token revoke, please login again ").toJsonString());
        }else {
            List<Account> accountExits = ofy().load().type(Account.class).filter("token", token).list();
            if (accountExits == null || accountExits.size() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                        .setMessage("Authenticate fail!").toJsonString());
            } else {
                String content = StringUtil.convertInputStreamToString(req.getInputStream());
                List<Long> listIdError = new ArrayList<>();
                try {
                    Approval approval = gson.fromJson(content, Approval.class);
                    if (approval != null) {
                        for (Long id : approval.getArticleId()) {
                            Article articleExist = ofy().load().type(Article.class).id(id).now();
                            if (articleExist != null) {
                                articleExist.setStatus(1);
                                ofy().save().entities(articleExist).now();
                            } else {
                                listIdError.add(id);
                            }

                        }
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println(new JsonResponse()
                            .setStatus(HttpServletResponse.SC_OK)
                            .setMessage("Approval!")
                            .toJsonString());

                } catch (Exception ex) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().println(new JsonResponse()
                            .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                            .setMessage("Can not approval")
                            .toJsonString());
                }
            }
        }
    }

}
