package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/analytics")
public class AnalyticsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String url = "jdbc:mysql://localhost:3306/helpdesk";
        String username = "root";
        String password = "admin";

        Map<String, Integer> stats = new LinkedHashMap<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT status, COUNT(*) as count FROM tickets GROUP BY status");

            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> json = new HashMap<>();
        json.put("labels", stats.keySet());
        json.put("counts", stats.values());

        out.print(new Gson().toJson(json));
    }
}
