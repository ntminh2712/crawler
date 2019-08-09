package api;

import Util.StringUtil;
import com.google.gson.Gson;
import entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class CategoryApi extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Source.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strId = req.getParameter("id");
        if (strId == null || strId.length() == 0) {
            getListCategory(req, resp);
        } else {
            getDetailCategory(req, resp, strId);
        }

    }

    private void getListCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(ofy().load().type(Category.class).filter("status", 1).list())
                .toJsonString());
        System.out.println("get list");

    }

    private void getDetailCategory(HttpServletRequest req, HttpServletResponse resp, String strId) throws IOException {
        Category category = ofy().load().type(Category.class).id(Long.parseLong(strId)).now();
        if (category == null || category.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Category is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Success!")
                .setData(category)
                .toJsonString());
        System.out.println("get detail");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            CategoryParamester category = gson.fromJson(content, CategoryParamester.class);
            ofy().save().entities(category).now();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_CREATED)
                    .setMessage("Success!")
                    .setData(category).toJsonString());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Can not create category. Error: %s", ex.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not create category!")
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
        Category existCategory = ofy().load().type(Category.class).id(Long.parseLong(strId)).now();
        if (existCategory == null || existCategory.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Category is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        existCategory.setStatus(-1);
        existCategory.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        ofy().save().entity(existCategory).now();
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
        Category existCategory = ofy().load().type(Category.class).id(Long.parseLong(strId)).now();
        if (existCategory == null || existCategory.isDeactive()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_NOT_FOUND)
                    .setMessage("Category is not found or has been deleted!")
                    .toJsonString());
            return;
        }
        String content = StringUtil.convertInputStreamToString(req.getInputStream());
        CategoryParamester updateVideo = gson.fromJson(content, CategoryParamester.class);
        existCategory.setName(updateVideo.getName());
        existCategory.setDescription(updateVideo.getDescription());
        ofy().save().entity(existCategory).now();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_OK)
                .setMessage("Update Success!")
                .setData(existCategory)
                .toJsonString());
    }
}
