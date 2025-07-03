package servlet;

import dao.DBConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/myTickets")
public class MyTicketsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            // get userId from request parameter (for now)
            String userIdParam = request.getParameter("userId");
            int userId = Integer.parseInt(userIdParam);

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, subject, description, status, created_at, updated_at FROM tickets WHERE user_id = ?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            JSONArray arr = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("subject", rs.getString("subject"));
                obj.put("description", rs.getString("description"));
                obj.put("status", rs.getString("status"));
                obj.put("created_at", rs.getString("created_at"));
                obj.put("updated_at", rs.getString("updated_at"));
                arr.put(obj);
            }
            out.print(arr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
