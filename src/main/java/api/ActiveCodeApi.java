package api;

import Util.StringUtil;
import entity.Account;
import entity.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ActiveCodeApi extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String activecode = req.getParameter("code");
        try {
            if (StringUtil.isValidEmailAddress(email)) {
                Account account = ofy().load().type(Account.class).id(email).now();
                if (account != null) {
                    if (account.getCode_active().getCode() == Integer.parseInt(activecode)) {
                        if (account.getCode_active().getTimeRevoke() > Calendar.getInstance().getTimeInMillis()) {
                            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                            resp.getWriter().println(new JsonResponse()
                                    .setStatus(HttpServletResponse.SC_ACCEPTED)
                                    .setMessage("Success!")
                                    .setData(account));
                        }else {
                            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                            resp.getWriter().println(new JsonResponse()
                                    .setStatus(HttpServletResponse.SC_ACCEPTED)
                                    .setMessage("Please login again, Your code has been revoke!"));
                        }
                    }else {
                        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                        resp.getWriter().println(new JsonResponse()
                                .setStatus(HttpServletResponse.SC_ACCEPTED)
                                .setMessage("Active code invalid!"));
                    }
                }else {
                    resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                    resp.getWriter().println(new JsonResponse()
                            .setStatus(HttpServletResponse.SC_ACCEPTED)
                            .setMessage("Account is not found or has been deleted"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_CREATED)
                        .setMessage("Email active invalid !"));
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
