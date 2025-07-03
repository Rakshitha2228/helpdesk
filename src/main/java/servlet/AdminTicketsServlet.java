package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.DBConnection;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/adminTickets")
public class AdminTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray tickets = new JSONArray();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT t.id, t.user_id, u.email, t.subject, t.description, t.status, t.created_at " +
                "FROM tickets t JOIN users u ON t.user_id = u.id"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("user_id", rs.getInt("user_id"));
                obj.put("user_email", rs.getString("email"));
                obj.put("subject", rs.getString("subject"));
                obj.put("description", rs.getString("description"));
                obj.put("status", rs.getString("status"));
                obj.put("created_at", rs.getTimestamp("created_at").toString());
                tickets.put(obj);
            }
        } catch (Exception e) {
            JSONObject err = new JSONObject();
            err.put("error", e.getMessage());
            tickets.put(err);
        }

        out.print(tickets.toString());
        out.flush();
    }
}
