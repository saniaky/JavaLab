package chat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UsersListServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf8");
        PrintWriter pw = response.getWriter();

        pw.println("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><meta http-equiv='refresh' content='10'></head>");
        pw.println("<body>");

        for (String user : activeUsers.keySet()) {
            String nickname = activeUsers.get(user).getName();
            pw.println("<div><form action='/chat/users.do' method='post'>\n" +
                    "<input type='radio' name='recipient' value='" + nickname + "' onChange='submit();'>" + nickname +
                    "</form></div>");
        }

        pw.println("</body></html>");
    }

}
