package api;

import Util.StringUtil;
import com.google.gson.Gson;
import entity.Account;
import entity.ActiveCodeParamester;
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

public class ActiveCodeApi extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArticleApi.class.getName());
    private static Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            ActiveCodeParamester activeCode = gson.fromJson(content, ActiveCodeParamester.class);
            if (StringUtil.isValidEmailAddress(activeCode.getEmail())) {
                Account account = ofy().load().type(Account.class).id(activeCode.getEmail()).now();
                if (account != null) {
                    if (account.getCode_active().getCode() == activeCode.getCode()) {
                        if (account.getCode_active().getTimeRevoke() > Calendar.getInstance().getTimeInMillis()) {
                            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                            resp.getWriter().println(new JsonResponse()
                                    .setStatus(HttpServletResponse.SC_ACCEPTED)
                                    .setMessage("Success!")
                                    .setData(account).toJsonString());
                        }else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().println(new JsonResponse()
                                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                                    .setMessage("Please login again, Your code has been revoke!").toJsonString());
                        }
                    }else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().println(new JsonResponse()
                                .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                                .setMessage("Active code invalid!").toJsonString());
                    }
                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println(new JsonResponse()
                            .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                            .setMessage("Account is not found or has been deleted").toJsonString());
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                        .setMessage("Email active invalid !").toJsonString());
            }
        }catch (Exception ex){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not active!")
                    .toJsonString());
        }
    }
}
