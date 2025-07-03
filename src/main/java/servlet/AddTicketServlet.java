package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBConnection;
import org.json.JSONObject;

@WebServlet("/addTicket")
public class AddTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String subject = request.getParameter("subject");
        String description = request.getParameter("description");
        int userId = Integer.parseInt(request.getParameter("userId"));

        JSONObject json = new JSONObject();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tickets (user_id, subject, description, status, created_at) VALUES (?, ?, ?, 'Open', NOW())"
            );
            ps.setInt(1, userId);
            ps.setString(2, subject);
            ps.setString(3, description);

            int rows = ps.executeUpdate();
            json.put("status", rows > 0 ? "success" : "error");
        } catch (Exception e) {
            json.put("status", "error");
            json.put("message", e.getMessage());
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
