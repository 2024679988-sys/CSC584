package servlet;

import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

import dao.ReportDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String period = request.getParameter("period");
        if (!"weekly".equalsIgnoreCase(period) && !"monthly".equalsIgnoreCase(period)) {
            period = "monthly";
        }
        period = period.toLowerCase();

        LocalDate anchorDate = parseDate(request.getParameter("anchorDate"));
        LocalDate startDate;
        LocalDate endDate;

        if ("weekly".equals(period)) {
            startDate = anchorDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endDate = startDate.plusDays(6);
        } else {
            startDate = anchorDate.withDayOfMonth(1);
            endDate = anchorDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        Date sqlStart = Date.valueOf(startDate);
        Date sqlEndExclusive = Date.valueOf(endDate.plusDays(1));

        request.setAttribute("period", period);
        request.setAttribute("anchorDate", anchorDate.toString());
        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());
        request.setAttribute("generatedDate", LocalDate.now().toString());
        request.setAttribute("paymentCount", reportDAO.getPaymentCount(sqlStart, sqlEndExclusive));
        request.setAttribute("totalRevenue", reportDAO.getTotalRevenue(sqlStart, sqlEndExclusive));
        request.setAttribute("expiringMemberCount", reportDAO.getExpiringMemberCount(sqlStart, sqlEndExclusive));
        request.setAttribute("maintenanceCount", reportDAO.getMaintenanceCount(sqlStart, sqlEndExclusive));
        request.setAttribute("paymentDetails", reportDAO.getPaymentDetails(sqlStart, sqlEndExclusive));
        request.setAttribute("expiringMemberTypeSummary", reportDAO.getExpiringMemberTypeSummary(sqlStart, sqlEndExclusive));
        request.setAttribute("maintenanceStatusSummary",
                reportDAO.getMaintenanceStatusSummary(sqlStart, sqlEndExclusive));

        request.getRequestDispatcher("report/report.jsp").forward(request, response);
    }

    private LocalDate parseDate(String input) {
        if (input == null || input.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            return LocalDate.now();
        }
    }
}
