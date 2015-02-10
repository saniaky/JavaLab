package servlet;

import entity.Ad;
import entity.AdList;
import entity.User;
import helper.AdListHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 4/27/13
 * Time: 6:17 PM
 */
@WebServlet(
        name = "MakeBidSevlet",
        urlPatterns = "/makeBid",
        initParams = {
                @WebInitParam(name = "percent", value = "2")
        }
)
public class MakeBidSevlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        OutputStream out = response.getOutputStream();
        String percent = getServletConfig().getInitParameter("percent");
        User user = (User) request.getSession().getAttribute("authUser");
        String errorMessage = null;
        int adId = Integer.valueOf(request.getParameter("adId"));

        try {
            String bidComment = request.getParameter("bidComment");
            Double bidValue = Double.valueOf(request.getParameter("bidValue"));
            Double bidPercent = Double.valueOf(percent) / 100.0 + 1;
            AdList adList = (AdList) getServletContext().getAttribute("ads");

            if (adList == null) {
                out.write(new String("Something bad happened... your id: " + adId + ", bid value: " + bidValue).getBytes());
            } else {
                for (Ad ad : adList.getAds()) {
                    if (ad.getId() == adId) {
                        Double lastBid =
                                new BigDecimal(ad.getLastBid().getBidValue() * bidPercent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (bidValue < lastBid) {
                            errorMessage = "Ставка меньше положенной";
                        } else {
                            bidValue = new BigDecimal(bidValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            ad.setBid(bidValue, bidComment, user);
                        }
                        break;
                    }
                }
                //System.out.println(bidComment + ",  - " + request.getCharacterEncoding());
            }
            AdListHelper.saveAdList(adList);
        } catch (NumberFormatException e) {
            errorMessage = "Неверное значение ставки";
            out.write(errorMessage.getBytes("UTF-8"));
        }
        response.sendRedirect("viewAd.jsp?id=" + adId);
        request.getSession().setAttribute("errorMessage", errorMessage);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
