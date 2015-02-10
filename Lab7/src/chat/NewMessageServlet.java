package chat;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // По умолчанию используется кодировка ISO-8859.
        request.setCharacterEncoding("utf8");
        String message = (String) request.getParameter("message");
        response.setCharacterEncoding("utf8");

        if (message != null && !"".equals(message)) {
            // По имени из сессии получить ссылку на объект ChatUser
            ChatUser author = activeUsers.get((String) request.getSession().getAttribute("name"));
            System.out.println((String) request.getParameter("recipient"));
            synchronized (messages) {
                messages.add(new ChatMessage(message, author, Calendar.getInstance().getTimeInMillis()));
            }
        }
        // Перенаправить пользователя на страницу с формой сообщения
        response.sendRedirect("/chat/compose_message.html");
    }
}