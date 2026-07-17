package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

import bean.DiscountsAndPromotions;
import bean.Member;
import bean.Payment;
import bean.Staff;
import dao.PaymentDAO;

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

        if (action.equalsIgnoreCase("add")) {
            request.setAttribute("memberList", PaymentDAO.getAllMembers());
            request.setAttribute("promoList", PaymentDAO.getAvailablePromotions());
            request.getRequestDispatcher("payment/addPayment.jsp").forward(request, response);
            return;
        }

        if (action.equalsIgnoreCase("edit")) {
            String paymentID = request.getParameter("paymentID");
            Payment payment = PaymentDAO.getPaymentByID(paymentID);

            if (payment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found.");
                return;
            }

            request.setAttribute("payment", payment);
            request.setAttribute("memberList", PaymentDAO.getAllMembers());
            request.setAttribute("promoList", PaymentDAO.getAvailablePromotions());
            request.getRequestDispatcher("payment/editPayment.jsp").forward(request, response);
            return;
        }

        if (action.equalsIgnoreCase("delete")) {
            PaymentDAO.deletePayment(request.getParameter("paymentID"));
            response.sendRedirect(request.getContextPath() + "/PaymentController?action=list");
            return;
        }

        ArrayList<Payment> paymentList = PaymentDAO.getAllPayments();
        ArrayList<Member> memberList = PaymentDAO.getAllMembers();
        ArrayList<Staff> staffList = PaymentDAO.getAllStaff();
        ArrayList<DiscountsAndPromotions> promoList = PaymentDAO.getAllPromotions();

        request.setAttribute("paymentList", paymentList);
        request.setAttribute("memberList", memberList);
        request.setAttribute("staffList", staffList);
        request.setAttribute("promoList", promoList);
        request.getRequestDispatcher("payment/listPayment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = trimToNull(request.getParameter("action"));
        String paymentMethod = trimToNull(request.getParameter("paymentMethod"));
        String promoID = trimToNull(request.getParameter("promoID"));
        String sessionStaffID = (String) session.getAttribute("staffID");

        if (paymentMethod == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment method is required.");
            return;
        }

        String memberID;
        String staffID = sessionStaffID;
        int duration;
        String paymentID = null;

        if ("update".equalsIgnoreCase(action)) {
            paymentID = trimToNull(request.getParameter("paymentID"));
            if (paymentID == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment ID is required.");
                return;
            }

            Payment existingPayment = PaymentDAO.getPaymentByID(paymentID);
            if (existingPayment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found.");
                return;
            }

            // Prevent editing a payment from extending or moving membership time again.
            memberID = existingPayment.getMemberID();
            duration = existingPayment.getDuration();
            staffID = existingPayment.getStaffID();
        } else {
            memberID = trimToNull(request.getParameter("memberID"));
            duration = parseDuration(request.getParameter("duration"));

            if (memberID == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Member is required.");
                return;
            }

            if (duration < 1 || duration > 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Duration must be 1, 2, or 3 months.");
                return;
            }
        }

        double monthlyPrice = PaymentDAO.getMemberPrice(memberID);
        if (monthlyPrice < 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "The selected member has an invalid member type.");
            return;
        }

        double amount = roundMoney(monthlyPrice * duration);
        double discountPercent = 0.0;

        if (promoID != null) {
            Double availableDiscount = PaymentDAO.getAvailableDiscountPercent(promoID);
            if (availableDiscount == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The selected promotion is unavailable, expired, or has not started.");
                return;
            }
            discountPercent = availableDiscount;
        }

        double discountedAmount = roundMoney(amount - (amount * discountPercent / 100.0));

        Payment payment = new Payment();
        payment.setPaymentID(paymentID);
        payment.setAmount(amount);
        payment.setDiscountedAmount(discountedAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setMemberID(memberID);
        payment.setStaffID(staffID);
        payment.setPromoID(promoID);
        payment.setDuration(duration);

        boolean saved;
        if ("update".equalsIgnoreCase(action)) {
            saved = PaymentDAO.updatePayment(payment);
        } else {
            saved = PaymentDAO.addPaymentAndUpdateExpiry(payment);
        }

        if (!saved) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Payment could not be saved. No membership expiry change was committed.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/PaymentController?action=list");
    }

    private static int parseDuration(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static double roundMoney(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
