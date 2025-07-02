package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.DBConnection;
import org.json.JSONObject;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false); // false means don’t create new session if none

        try (PrintWriter out = response.getWriter()) {
            if (session != null && session.getAttribute("userId") != null) {
                int userId = (int) session.getAttribute("userId");

                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, email FROM users WHERE id = ?"
                );
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", rs.getString("name"));
                    obj.put("email", rs.getString("email"));
                    out.print(obj.toString());
                } else {
                    out.print("{}");
                }
            } else {
                out.print("{}"); // no session or user not logged in
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
