/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.web.filters;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;

public class SbnFilter implements Filter {
    private static Logger log = Logger.getLogger(SbnFilter.class);

    protected ServletContext ctx = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        ctx = filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        // imposta il linguaggio
        Locale locale = (Locale) request.getSession().getAttribute(
                Globals.LOCALE_KEY);
        if (request.getParameter("language") != null
                && !locale.getLanguage().toLowerCase()
                        .equals(request.getParameter("language").toLowerCase())) {
            locale = new Locale(request.getParameter("language").toLowerCase(),
                    request.getParameter("language").toUpperCase());
            session.setAttribute(Globals.LOCALE_KEY, locale);
            log.info("switch locale");
        }

        response.setHeader("pragma", "no-cache");// http 1.0
        response.addHeader( "cache-control", "must-revalidate" );
        response.addHeader( "cache-control", "no-cache" );
        response.addHeader( "cache-control", "no-store" );
        response.setDateHeader("expires", 0); // proxy
        chain.doFilter(request, response);
    }

    public void destroy() {
        // nothing to destroy yet
    }
}
