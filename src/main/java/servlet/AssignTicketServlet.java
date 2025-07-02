package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/assignTicket")
public class AssignTicketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int ticketId = Integer.parseInt(req.getParameter("ticketId"));
        int assigneeId = Integer.parseInt(req.getParameter("assigneeId"));

        String url = "jdbc:mysql://localhost:3306/helpdesk";
        String username = "root";
        String password = "admin";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);

            // If your tickets table doesn't yet have assigned_to column, add it first:
            // ALTER TABLE tickets ADD COLUMN assigned_to INT;

            PreparedStatement ps = conn.prepareStatement("UPDATE tickets SET assigned_to=? WHERE id=?");
            ps.setInt(1, assigneeId);
            ps.setInt(2, ticketId);
            ps.executeUpdate();

            conn.close();

            // Show success message
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h3 style='color:green;'>✅ Ticket successfully assigned!</h3>");
            out.println("<a href='alltickets.html'>Back to All Tickets</a>");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Database error: " + e.getMessage());
        }
    }
}
