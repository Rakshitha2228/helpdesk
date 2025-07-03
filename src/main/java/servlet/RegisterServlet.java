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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (name, email, password) VALUES (?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int result = ps.executeUpdate();

            json.put("status", result > 0 ? "success" : "error");
            json.put("message", result > 0 ? "Registration successful" : "Registration failed");
        } catch (Exception e) {
            json.put("status", "error");
            json.put("message", "Exception: " + e.getMessage());
        }

        out.print(json.toString());
        out.flush();
    }
}
