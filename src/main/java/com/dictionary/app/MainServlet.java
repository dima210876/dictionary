package com.dictionary.app;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/MyServlet")
public class MainServlet extends HttpServlet
{
    private static final String URL = "jdbc:mysql://localhost:3306/dictionary" +
            "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "myroot";
    private static final String LEXEME_REGEX = "[A-Za-z]++";
    private String languageWas;
    private String languageNeed;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html;charset=UTF-8");
        String word = "" + req.getParameter("txt");
        checkOnLanguage(word);
        req.setAttribute("translation", getTranslation(word));
        req.setAttribute("word", word);
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/");
        requestDispatcher.forward(req, resp);
    }

    private void checkOnLanguage(String word)
    {
        Pattern p = Pattern.compile(LEXEME_REGEX);
        Matcher m = p.matcher(word);
        languageWas = "eng";
        languageNeed = "ru";
        boolean hasNext = m.find();
        if (!hasNext)
        {
            String st = languageWas;
            languageWas = languageNeed;
            languageNeed = st;
        }
    }

    private ResultSet getQueryResult()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from words");
        } catch (SQLException | ClassNotFoundException |  InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String getTranslation(String word)
    {
        ResultSet resultSet = getQueryResult();
        if (resultSet == null)
        {
            return "(слово не найдено в словаре)";
        }
        try
        {
            if (resultSet.next())
            {
                do {
                    if (resultSet.getString(languageWas).equals(word))
                    {
                        return " -- " + resultSet.getString(languageNeed);
                    }
                } while (resultSet.next());
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "(слово не найдено в словаре)";
    }
}
