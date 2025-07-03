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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false); 

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
                out.print("{}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
