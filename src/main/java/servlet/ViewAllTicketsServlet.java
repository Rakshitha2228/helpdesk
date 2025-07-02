package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/viewAllTickets")
public class ViewAllTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String url = "jdbc:mysql://localhost:3306/helpdesk";
        String username = "root";
        String password = "admin";

        List<Map<String, Object>> tickets = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT t.id, t.subject, t.status, u.name AS userName FROM tickets t JOIN users u ON t.user_id=u.id");

            while (rs.next()) {
                Map<String, Object> ticket = new HashMap<>();
                ticket.put("id", rs.getInt("id"));
                ticket.put("subject", rs.getString("subject"));
                ticket.put("status", rs.getString("status"));
                ticket.put("userName", rs.getString("userName"));
                tickets.add(ticket);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print(new Gson().toJson(tickets));
    }
}
