package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import bean.DiscountsAndPromotions;
import bean.Member;
import bean.Payment;
import bean.Staff;
import util.DBConnection;

public class PaymentDAO {

    private PaymentDAO() {
    }

    /**
     * Creates a payment and extends the member expiry date in one transaction.
     * If either operation fails, neither change is saved.
     */
    public static boolean addPaymentAndUpdateExpiry(Payment payment) {
        String insertSql = "INSERT INTO PAYMENT "
                + "(AMOUNT, DISCOUNTEDAMOUNT, PAYMENTMETHOD, MEMBERID, STAFFID, PROMOID, DURATION) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String expirySql = "UPDATE MEMBER SET EXPIREDDATE = ADD_MONTHS("
                + "CASE WHEN EXPIREDDATE IS NULL OR EXPIREDDATE < TRUNC(SYSDATE) "
                + "THEN TRUNC(SYSDATE) ELSE EXPIREDDATE END, ?) WHERE MEMBERID = ?";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement paymentStatement = con.prepareStatement(insertSql);
                 PreparedStatement expiryStatement = con.prepareStatement(expirySql)) {

                paymentStatement.setDouble(1, payment.getAmount());
                paymentStatement.setDouble(2, payment.getDiscountedAmount());
                paymentStatement.setString(3, payment.getPaymentMethod());
                paymentStatement.setString(4, payment.getMemberID());
                paymentStatement.setString(5, payment.getStaffID());
                setNullablePromoID(paymentStatement, 6, payment.getPromoID());
                paymentStatement.setInt(7, payment.getDuration());

                int paymentRows = paymentStatement.executeUpdate();

                expiryStatement.setInt(1, payment.getDuration());
                expiryStatement.setString(2, payment.getMemberID());
                int memberRows = expiryStatement.executeUpdate();

                if (paymentRows == 1 && memberRows == 1) {
                    con.commit();
                    return true;
                }

                con.rollback();
                return false;
            }
        } catch (Exception e) {
            rollbackQuietly(con);
            e.printStackTrace();
            return false;
        } finally {
            restoreAutoCommit(con);
            closeQuietly(con);
        }
    }

    public static ArrayList<Payment> getAllPayments() {
        ArrayList<Payment> paymentList = new ArrayList<>();
        String sql = "SELECT * FROM PAYMENT ORDER BY PAYMENTID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paymentList.add(mapPayment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentList;
    }

    public static Payment getPaymentByID(String paymentID) {
        String sql = "SELECT * FROM PAYMENT WHERE PAYMENTID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Editing a payment does not extend the expiry date again.
     * Member and duration are preserved by the controller.
     */
    public static boolean updatePayment(Payment payment) {
        String sql = "UPDATE PAYMENT SET AMOUNT=?, DISCOUNTEDAMOUNT=?, PAYMENTMETHOD=?, "
                + "MEMBERID=?, STAFFID=?, PROMOID=?, DURATION=? WHERE PAYMENTID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, payment.getAmount());
            ps.setDouble(2, payment.getDiscountedAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getMemberID());
            ps.setString(5, payment.getStaffID());
            setNullablePromoID(ps, 6, payment.getPromoID());
            ps.setInt(7, payment.getDuration());
            ps.setString(8, payment.getPaymentID());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deletePayment(String paymentID) {
        String sql = "DELETE FROM PAYMENT WHERE PAYMENTID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Member> getAllMembers() {
        ArrayList<Member> memberList = new ArrayList<>();
        String sql = "SELECT MEMBERID, MEMBERNAME, MEMBERTYPE, EXPIREDDATE "
                + "FROM MEMBER ORDER BY MEMBERNAME";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Member member = new Member();
                member.setMemberID(rs.getString("MEMBERID"));
                member.setMemberName(rs.getString("MEMBERNAME"));
                member.setMemberType(rs.getString("MEMBERTYPE"));
                member.setExpiredDate(rs.getDate("EXPIREDDATE"));
                memberList.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberList;
    }

    public static ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> staffList = new ArrayList<>();
        String sql = "SELECT STAFFID, STAFFNAME FROM STAFF ORDER BY STAFFNAME";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffID(rs.getString("STAFFID"));
                staff.setStaffName(rs.getString("STAFFNAME"));
                staffList.add(staff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return staffList;
    }

    public static ArrayList<DiscountsAndPromotions> getAllPromotions() {
        ArrayList<DiscountsAndPromotions> promoList = new ArrayList<>();
        String sql = "SELECT * FROM DISCOUNTSANDPROMOTIONS ORDER BY PROMOID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                promoList.add(mapPromotion(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return promoList;
    }

    public static ArrayList<DiscountsAndPromotions> getAvailablePromotions() {
        ArrayList<DiscountsAndPromotions> promoList = new ArrayList<>();
        String sql = "SELECT * FROM DISCOUNTSANDPROMOTIONS "
                + "WHERE TRUNC(SYSDATE) BETWEEN TRUNC(STARTDATE) AND TRUNC(ENDDATE) "
                + "ORDER BY ENDDATE, PROMOID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                promoList.add(mapPromotion(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return promoList;
    }

    public static double getMemberPrice(String memberID) {
        String sql = "SELECT MEMBERTYPE FROM MEMBER WHERE MEMBERID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, memberID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getPriceByMemberType(rs.getString("MEMBERTYPE"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static double getPriceByMemberType(String memberType) {
        if (memberType == null) {
            return -1;
        }

        switch (memberType.trim().toLowerCase()) {
            case "premium":
                return 50.00;
            case "regular":
                return 35.00;
            case "student":
                return 20.00;
            default:
                return -1;
        }
    }

    public static Double getAvailableDiscountPercent(String promoID) {
        if (promoID == null || promoID.isBlank()) {
            return 0.0;
        }

        String sql = "SELECT DISCOUNTPERCENT FROM DISCOUNTSANDPROMOTIONS "
                + "WHERE PROMOID=? "
                + "AND TRUNC(SYSDATE) BETWEEN TRUNC(STARTDATE) AND TRUNC(ENDDATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promoID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("DISCOUNTPERCENT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Payment mapPayment(ResultSet rs) throws Exception {
        Payment payment = new Payment();
        payment.setPaymentID(rs.getString("PAYMENTID"));
        payment.setPaymentDate(rs.getDate("PAYMENTDATE"));
        payment.setAmount(rs.getDouble("AMOUNT"));
        payment.setDiscountedAmount(rs.getDouble("DISCOUNTEDAMOUNT"));
        payment.setPaymentMethod(rs.getString("PAYMENTMETHOD"));
        payment.setMemberID(rs.getString("MEMBERID"));
        payment.setStaffID(rs.getString("STAFFID"));
        payment.setPromoID(rs.getString("PROMOID"));
        payment.setDuration(rs.getInt("DURATION"));
        return payment;
    }

    private static DiscountsAndPromotions mapPromotion(ResultSet rs) throws Exception {
        DiscountsAndPromotions promo = new DiscountsAndPromotions();
        promo.setPromoID(rs.getString("PROMOID"));
        promo.setPromoName(rs.getString("PROMONAME"));
        promo.setDiscountPercent(rs.getDouble("DISCOUNTPERCENT"));
        promo.setStartDate(rs.getDate("STARTDATE"));
        promo.setEndDate(rs.getDate("ENDDATE"));
        promo.setPromoDesc(rs.getString("PROMODESC"));
        return promo;
    }

    private static void setNullablePromoID(PreparedStatement ps, int parameterIndex, String promoID)
            throws Exception {
        if (promoID == null || promoID.isBlank()) {
            ps.setNull(parameterIndex, Types.VARCHAR);
        } else {
            ps.setString(parameterIndex, promoID);
        }
    }

    private static void rollbackQuietly(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    private static void restoreAutoCommit(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    private static void closeQuietly(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
