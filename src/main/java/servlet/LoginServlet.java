package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.DBConnection;
import org.json.JSONObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE email = ? AND password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // ✅ Save userId in session
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("id"));

                json.put("status", "success");
                json.put("message", "Login successful");
                json.put("userId", rs.getInt("id"));
                json.put("name", rs.getString("name"));
            } else {
                json.put("status", "error");
                json.put("message", "Invalid email or password");
            }
        } catch (Exception e) {
            json.put("status", "error");
            json.put("message", "Exception: " + e.getMessage());
        }

        out.print(json.toString());
        out.flush();
    }
}
