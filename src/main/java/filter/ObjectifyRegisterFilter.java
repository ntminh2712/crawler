package filter;

import com.googlecode.objectify.ObjectifyService;
import entity.Account;
import entity.Article;
import entity.Category;
import entity.Source;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ObjectifyRegisterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ObjectifyService.register(Article.class);
        ObjectifyService.register(Category.class);
        ObjectifyService.register(Source.class);
        ObjectifyService.register(Account.class);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
