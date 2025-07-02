package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/getAssignedTickets")
public class ViewAssignedTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String url = "jdbc:mysql://localhost:3306/helpdesk";
        String username = "root";
        String password = "admin";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                    "SELECT t.id, t.subject, t.status, u.name AS assigned_user " +
                    "FROM tickets t " +
                    "LEFT JOIN users u ON t.assigned_to = u.id " +
                    "WHERE t.assigned_to IS NOT NULL")) {

                List<String> ticketsJson = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String subject = rs.getString("subject");
                    String status = rs.getString("status");
                    String assignedUser = rs.getString("assigned_user");
                    ticketsJson.add(String.format(
                        "{\"id\":%d,\"subject\":\"%s\",\"status\":\"%s\",\"assignedUser\":\"%s\"}",
                        id, subject, status, assignedUser
                    ));
                }
                out.print("[" + String.join(",", ticketsJson) + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("[]");
        }
    }
}
