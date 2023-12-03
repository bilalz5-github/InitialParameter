import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

@WebServlet(
        urlPatterns = "/loginServlet",
        initParams = {
                @WebInitParam(name = "driverClass", value = "org.mariadb.jdbc.Driver"),
                @WebInitParam(name = "url", value = "jdbc:mariadb://mariadb.vamk.fi/e2101856_servlet"),
                @WebInitParam(name = "dbUser", value = "e2101856"),
                @WebInitParam(name = "dbPassword", value = "x4m9uCCbptJ")
        }
)
public class loginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        System.out.println(email);      
        System.out.println(password);      

try (
            Connection conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101856_servlet", getInitParameter("dbUser"),getInitParameter("dbPassword") );
            PreparedStatement ps = conn.prepareStatement("SELECT email,password FROM e2101856_servlet;" + "");
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            RequestDispatcher reqDis;

            if (rs.next()) {
                reqDis = req.getRequestDispatcher("homeServlet");
                req.setAttribute("message", "Welcome home: " + email);
                reqDis.forward(req, res);
            } else {
                reqDis = req.getRequestDispatcher("login.html");
                reqDis.include(req, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
