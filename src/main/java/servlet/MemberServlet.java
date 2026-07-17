package servlet;

import java.io.IOException;
import java.util.List;

import bean.Member;
import dao.MemberDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MemberServlet")
public class MemberServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MemberDAO dao = new MemberDAO();

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
            request.getRequestDispatcher("member/addMember.jsp").forward(request, response);
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            String memberID = request.getParameter("memberID");
            Member member = dao.getMember(memberID);
            request.setAttribute("member", member);
            request.getRequestDispatcher("member/editMember.jsp").forward(request, response);
            return;
        }

        if ("delete".equalsIgnoreCase(action)) {
            // Deletion changes data and must be submitted using POST.
            response.sendRedirect(request.getContextPath()
                    + "/MemberServlet?action=list&invalidDeleteRequest=true");
            return;
        }

        List<Member> memberList = dao.getAllMember();
        request.setAttribute("memberList", memberList);
        request.getRequestDispatcher("member/listMember.jsp").forward(request, response);
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

        String staffID = (String) session.getAttribute("staffID");

        Member member = new Member();
        member.setMemberName(request.getParameter("memberName"));
        member.setMemberEmail(request.getParameter("memberEmail"));
        member.setMemberPhone(request.getParameter("memberPhone"));
        member.setMemberType(request.getParameter("memberType"));
        member.setStaffID(staffID);

        if ("insert".equalsIgnoreCase(action)) {
            // A new member has no expiry date until the first payment is made.
            member.setExpiredDate(null);
            dao.insertMember(member);
        } else if ("update".equalsIgnoreCase(action)) {
            String memberID = request.getParameter("memberID");
            Member existingMember = dao.getMember(memberID);

            if (existingMember == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found.");
                return;
            }

            member.setMemberID(memberID);
            // Expiry date can only be changed by a successful payment.
            member.setExpiredDate(existingMember.getExpiredDate());
            dao.updateMember(member);
        }

        response.sendRedirect(request.getContextPath() + "/MemberServlet?action=list");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String memberID = request.getParameter("memberID");

        if (memberID == null || memberID.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/MemberServlet?action=list&deleteFailed=true");
            return;
        }

        memberID = memberID.trim();

        // Prevent deletion when payment records reference this member.
        if (dao.hasIntegrityConstraint(memberID)) {
            response.sendRedirect(request.getContextPath()
                    + "/MemberServlet?action=list&deleteBlocked=true");
            return;
        }

        boolean deleted = dao.deleteMember(memberID);

        if (deleted) {
            response.sendRedirect(request.getContextPath()
                    + "/MemberServlet?action=list&deleteSuccess=true");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/MemberServlet?action=list&deleteFailed=true");
        }
    }
}
