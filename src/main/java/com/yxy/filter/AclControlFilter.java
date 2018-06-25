package com.yxy.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.yxy.common.ApplicationContextHelper;
import com.yxy.common.JsonData;
import com.yxy.common.RequestHolder;
import com.yxy.model.SysUser;
import com.yxy.service.SysCoreService;
import com.yxy.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AclControlFilter
 *
 * @author 余昕宇
 * @date 2018-06-22 16:27
 **/
@Slf4j
public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    private final static String NO_AUTH_URL = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        exclusionUrlSet.add(NO_AUTH_URL);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        if (exclusionUrlSet.contains(servletPath)) {

            filterChain.doFilter(servletRequest, servletResponse);

        }

        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {

            log.info("someone visit {}, but no login, parameter{}", servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;

        }

        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {

            log.info("{} visit {}, but no login, parameter{}", JsonMapper.obj2String(sysUser), servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;

        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String servletUrl = request.getServletPath();
        if (servletUrl.endsWith(".json")) {

            JsonData jsonData = JsonData.fail("无访问权限，如需要访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.obj2String(jsonData));

        } else {

            clientRedirect(NO_AUTH_URL, response);

        }

    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException {

        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");

    }

    @Override
    public void destroy() {

    }

}
