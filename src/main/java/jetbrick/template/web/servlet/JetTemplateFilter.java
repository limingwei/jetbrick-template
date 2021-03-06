/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.web.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;

/**
 * 直接作为 Filter 使用。需要在 web.xml 中作如下配置。
 * <pre><xmp>
 * <filter>
 *   <filter-name>jetbrick-template</filter-name>
 *   <filter-class>jetbrick.template.web.servlet.JetTemplateFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>jetbrick-template</filter-name>
 *   <url-pattern>*.jetx</url-pattern>
 * </filter-mapping>
 * </xmp></pre>
 */
public class JetTemplateFilter implements Filter {
    @Override
    public void init(FilterConfig fc) throws ServletException {
        JetWebEngineLoader.setServletContext(fc.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        JetContext context = new JetWebContext(req, resp);

        String path = req.getPathInfo();
        if ((path == null) || (path.length() == 0)) {
            path = req.getServletPath();
        }
        if (path.endsWith("/")) {
            path = path + "/index.jetx";
        }

        try {
            JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(path);
            template.render(context, resp.getOutputStream());
        } catch (ResourceNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Template not found: " + path);
        }
    }

    @Override
    public void destroy() {
    }
}
