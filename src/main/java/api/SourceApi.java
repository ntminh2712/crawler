package api;

import Util.StringUtil;
import com.google.gson.Gson;
import dto.ArticleDto;
import dto.SourceDto;
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
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SourceApi extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Source.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            // get list
            getListSource(req, resp);
        } else {
            // get detail
            getDetailSource(req, resp, strId);
        }
    }

    private void getListSource(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<SourceDto> listSourceDto = new ArrayList<>();
        List<Source> list = ofy().load().type(Source.class).filter("status", 1).list();
        for (Source source: list){
            Category category = ofy().load().type(Category.class).id(source.getCategory_id()).now();
            listSourceDto.add( new SourceDto(source,category));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(listSourceDto)
                .toJsonString());

    }

    private void getDetailSource(HttpServletRequest req, HttpServletResponse resp, String strId) throws IOException {
        Source source = (Source) ofy().load().type(Source.class).id( Long.parseLong(strId)).now();
        if (source == null || source.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Source is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        Category category = ofy().load().type(Category.class).id(source.getCategory_id()).now();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(new SourceDto(source,category))
                .toJsonString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            Source source = gson.fromJson(content, Source.class);
            Category category = ofy().load().type(Category.class).id(source.getCategory_id()).now();
            if (category == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_NOT_FOUND)
                        .setMessage("category is not found or has been deleted!")
                        .toJsonString());
                return;
            }
            ofy().save().entities(source).now();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_CREATED)
                    .setMessage("Success!")
                    .setData(source).toJsonString());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Can not create source. Error: %s", ex.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not create source!")
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
        Source existSource = ofy().load().type(Source.class).id(Long.parseLong(strId)).now();
        if (existSource == null || existSource.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Source is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        existSource.setStatus(-1);
        existSource.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        ofy().save().entity(existSource).now();
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
        Source existSource = ofy().load().type(Source.class).id(Long.parseLong(strId)).now();
        if (existSource == null || existSource.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Source is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        String content = StringUtil.convertInputStreamToString(req.getInputStream());
        Source updateSource = gson.fromJson(content, Source.class);
        existSource.setUrl(updateSource.getUrl());
        existSource.setTitle_selector(updateSource.getTitle_selector());
        existSource.setLink_selector(updateSource.getLink_selector());
        existSource.setContent_selector(updateSource.getContent_selector());
        existSource.setDescription_selector(updateSource.getDescription_selector());
        existSource.setRemove_selector(updateSource.getRemove_selector());
        existSource.setAuthor_selector(updateSource.getAuthor_selector());
        existSource.setCategory_id(updateSource.getCategory_id());
        ofy().save().entity(existSource).now();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Update Success!")
                .setData(existSource)
                .toJsonString());
    }
}
