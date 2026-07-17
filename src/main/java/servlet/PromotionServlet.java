package servlet;

import java.io.IOException;
import java.sql.Date;

import bean.DiscountsAndPromotions;
import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PromotionServlet")
public class PromotionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final PromotionDAO dao = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        if ("add".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("promotion/addPromotion.jsp").forward(request, response);
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            String promoID = request.getParameter("promoID");

            request.setAttribute("promo", dao.getPromotionByID(promoID));
            request.getRequestDispatcher("promotion/editPromotion.jsp").forward(request, response);
            return;
        }

        if ("delete".equalsIgnoreCase(action)) {
            // Deletion changes data and must be submitted using POST.
            response.sendRedirect(request.getContextPath()
                    + "/PromotionServlet?action=list&invalidDeleteRequest=true");
            return;
        }

        request.setAttribute("promoList", dao.getAllPromotions());
        request.getRequestDispatcher("promotion/listPromotion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            handleDelete(request, response);
            return;
        }

        DiscountsAndPromotions promo = new DiscountsAndPromotions();

        promo.setPromoName(request.getParameter("promoName"));
        promo.setDiscountPercent(Double.parseDouble(request.getParameter("discountPercent")));
        promo.setStartDate(Date.valueOf(request.getParameter("startDate")));
        promo.setEndDate(Date.valueOf(request.getParameter("endDate")));
        promo.setPromoDesc(request.getParameter("promoDesc"));

        if ("update".equalsIgnoreCase(action)) {
            promo.setPromoID(request.getParameter("promoID"));
            dao.updatePromotion(promo);
        } else {
            dao.addPromotion(promo);
        }

        response.sendRedirect(request.getContextPath() + "/PromotionServlet?action=list");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String promoID = request.getParameter("promoID");

        if (promoID == null || promoID.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/PromotionServlet?action=list&deleteFailed=true");
            return;
        }

        promoID = promoID.trim();

        // Prevent deletion when payment records reference this promotion.
        if (dao.hasIntegrityConstraint(promoID)) {
            response.sendRedirect(request.getContextPath()
                    + "/PromotionServlet?action=list&deleteBlocked=true");
            return;
        }

        boolean deleted = dao.deletePromotion(promoID);

        if (deleted) {
            response.sendRedirect(request.getContextPath()
                    + "/PromotionServlet?action=list&deleteSuccess=true");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/PromotionServlet?action=list&deleteFailed=true");
        }
    }
}
