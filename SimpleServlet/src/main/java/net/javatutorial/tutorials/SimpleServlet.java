package net.javatutorial.tutorials;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
        reqest.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String tip = reqest.getParameter("tip");

        UrlCozucu urlCozucu = new UrlCozucu();
        if (tip.equals("1")) {
            String link = reqest.getParameter("url");
            String text = reqest.getParameter("text");
            text = URLDecoder.decode(text, "UTF-8");
            response.getWriter().println(link + " sayfasında " + text + " kelimesi " + urlCozucu.urldekiKelimeleriSay(link, text) + " defa geçiyor");
        } else if (tip.equals("2")) {
            String l = reqest.getParameter("url");
            String t = reqest.getParameter("text");

            List<String> links = Arrays.asList(l.split(","));
            List<String> keywords = Arrays.asList(t.split(","));

            List<URLObjesi> urlObjeleri = urlCozucu.urlSiralama(links, keywords);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
          
            out.println("<ol>");
            out.println("<H2 style=\"background-color:LightGray;\">" +"URL Siralama" + "</H2>");
            out.println();
            int i=0;
            for (URLObjesi urlObjesi : urlObjeleri) {
            	i++;
                out.println("<H2>" + i+ "." +urlObjesi.yazdir() + "</H2>");
            }
            out.println("</ol>");
            out.println("</body>");
            out.println("</html>");
        } else if (tip.equals(3)) {
            String l = reqest.getParameter("url");
            String t = reqest.getParameter("text");

            List<String> links = Arrays.asList(l.split(","));
            List<String> keywords = Arrays.asList(t.split(","));
            List<URLObjesi> urlObjeleri = urlCozucu.siteSiralama(links, keywords);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<ol>");
            out.println("<H2 style=\"background-color:LightGray;\">" +"Site Siralama(derinlik hesaba katilarak)" + "</H2>");
            out.println();
            int i=0;
            for (URLObjesi urlObjesi : urlObjeleri) {
            	i++;
                out.println("<H2>" + i + "." +urlObjesi.yazdir() + "</H2>");
            }
            out.println("</ol>");
            out.println("</html>");
        } else if (tip.equals(4)) {
            String l = reqest.getParameter("url");
            String t = reqest.getParameter("text");

            List<String> links = Arrays.asList(l.split(","));
            List<String> keywords = Arrays.asList(t.split(","));

            List<URLObjesi> urlObjeleri = urlCozucu.semantikAnaliz(links, keywords);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<ol>");
            out.println("<H2 style=\"background-color:LightGray;\">" +"Semantik Analiz" + "</H2>");
            out.println();
            int i=0;
            for (URLObjesi urlObjesi : urlObjeleri) {
            	i++;
                out.println("<li>" + i + "." + urlObjesi.yazdir() + "</li>");
            }
            out.println("</ol>");
            out.println("</html>");
        }
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + this.getServletName() + " has stopped");
    }



}
