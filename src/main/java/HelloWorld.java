import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class HelloWorld extends HttpServlet{
    private String message;

    @Override
    public void init() throws ServletException {
       message = "Hello World via Servlet";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello World via Servlet</h1>");
        out.println("<p>" + new Date().toString() + "</p>");
    }
}
