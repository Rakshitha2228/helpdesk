package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/getTechnicians")
public class GetTechniciansServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String url = "jdbc:mysql://localhost:3306/helpdesk";
        String username = "root";
        String password = "admin";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM users WHERE role='technician'");

            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                if (json.length() > 1) json.append(",");
                json.append("{\"id\":").append(rs.getInt("id"))
                    .append(",\"name\":\"").append(rs.getString("name")).append("\"}");
            }
            json.append("]");
            out.print(json.toString());
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            out.print("[]");
        }
    }
}
