package api;

import Util.StringUtil;
import com.google.gson.Gson;
import entity.Account;
import entity.ActiveCode;
import entity.Article;
import entity.JsonResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class AccountApi extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArticleApi.class.getName());
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = StringUtil.convertInputStreamToString(req.getInputStream());
            Account account = gson.fromJson(content, Account.class);
            if (StringUtil.isValidEmailAddress(account.getEmail())) {
                if (account.getPhone() == null || account.getFull_name() == null) {
                    login(account.getEmail(), req, resp);
                } else {
                    register(account, req, resp);
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().println(new JsonResponse()
                        .setStatus(HttpServletResponse.SC_CREATED)
                        .setMessage("Email invalid !"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Can not create account. Error: %s", ex.getMessage()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .setMessage("Can not login or register !")
                    .toJsonString());
        }
    }
    private long generateActivecode() {
        Random rnd = new Random();
        long n = 100000 + rnd.nextInt(900000);
        return n;
    }
    private void login(String email,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account account = ofy().load().type(Account.class).id(email).now();
        ActiveCode activeCode = new ActiveCode();
        activeCode.setTimeRevoke(Calendar.getInstance().getTimeInMillis() + 900000);
        activeCode.setCode(generateActivecode());
        account.setCode_active(activeCode);
        ofy().save().entities(account).now();
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_ACCEPTED)
                .setMessage("Please check your email to login!")
                .setData(account).toJsonString());
        sendMail("minhntd00562@fpt.edu.vn", account.getEmail(),account.getFull_name(), "Account Login", activeCode.getCode());
    }
    private void register(Account account,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account accountExists = ofy().load().type(Account.class).id(account.getEmail()).now();
        if (accountExists != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(new JsonResponse()
                    .setStatus(HttpServletResponse.SC_CREATED)
                    .setMessage("This email already exists"));
            return;
        }
        account.setStatus(0);
        ActiveCode activeCode = new ActiveCode();
        activeCode.setTimeRevoke(Calendar.getInstance().getTimeInMillis() + 900000);
        activeCode.setCode(generateActivecode());
        account.setCode_active(activeCode);
        ofy().save().entities(account).now();
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println(new JsonResponse()
                .setStatus(HttpServletResponse.SC_CREATED)
                .setMessage("Register successfully, Please check your email to active account!")
                .setData(account).toJsonString());
        sendMail("minhntd00562@fpt.edu.vn", account.getEmail(),account.getFull_name(), "Active account", activeCode.getCode());
    }

    public void sendMail(String sendEmailFrom, String sendMailTo,String name, String messageSubject, long messageText) {
        Properties prop = new Properties();
        Session session = Session.getDefaultInstance(prop, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sendEmailFrom));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendMailTo, "Hello, Mr/Mrs" + name));
            msg.setSubject(messageSubject);
            msg.setText("This is your active code, " + messageText);
            Transport.send(msg);
            LOGGER.warning("sentMail success.");
        } catch (AddressException e) {
            LOGGER.warning("AddressException");
            e.printStackTrace();
        } catch (MessagingException e) {
            LOGGER.warning("MessagingException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LOGGER.warning("UnsupportedEncodingException.");
            e.printStackTrace();
        }
    }
}
