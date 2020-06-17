package cn.sicnu.ming.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author frank ming
 * @createTime 20200617 16:48
 * @description 过滤
 */
public class Allfilter implements Filter {

    public FilterRegistrationBean<Allfilter> getAllFilter(){
        FilterRegistrationBean<Allfilter> f = new FilterRegistrationBean<>();
        f.setFilter(new Allfilter());
        f.addUrlPatterns("/*");
        return f;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
