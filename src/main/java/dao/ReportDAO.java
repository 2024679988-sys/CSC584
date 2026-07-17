package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class ReportDAO {

    public int getPaymentCount(Date startDate, Date endDateExclusive) {
        return getCount("SELECT COUNT(*) FROM PAYMENT WHERE PAYMENTDATE >= ? AND PAYMENTDATE < ?",
                startDate, endDateExclusive);
    }

    public double getTotalRevenue(Date startDate, Date endDateExclusive) {
        String sql = "SELECT COALESCE(SUM(DISCOUNTEDAMOUNT), 0) "
                + "FROM PAYMENT WHERE PAYMENTDATE >= ? AND PAYMENTDATE < ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setDateRange(ps, startDate, endDateExclusive);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public int getExpiringMemberCount(Date startDate, Date endDateExclusive) {
        return getCount("SELECT COUNT(*) FROM MEMBER WHERE EXPIREDDATE >= ? AND EXPIREDDATE < ?",
                startDate, endDateExclusive);
    }

    public int getMaintenanceCount(Date startDate, Date endDateExclusive) {
        return getCount("SELECT COUNT(*) FROM EQUIPMENTMAINTENANCE "
                + "WHERE MAINTENANCEDATE >= ? AND MAINTENANCEDATE < ?",
                startDate, endDateExclusive);
    }

    public List<String[]> getPaymentDetails(Date startDate, Date endDateExclusive) {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT P.PAYMENTID, P.PAYMENTDATE, M.MEMBERNAME, "
                + "P.AMOUNT, P.DISCOUNTEDAMOUNT, P.PAYMENTMETHOD, S.STAFFNAME "
                + "FROM PAYMENT P "
                + "LEFT JOIN MEMBER M ON P.MEMBERID = M.MEMBERID "
                + "LEFT JOIN STAFF S ON P.STAFFID = S.STAFFID "
                + "WHERE P.PAYMENTDATE >= ? AND P.PAYMENTDATE < ? "
                + "ORDER BY P.PAYMENTDATE DESC, P.PAYMENTID DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setDateRange(ps, startDate, endDateExclusive);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[] {
                            value(rs.getString("PAYMENTID")),
                            value(String.valueOf(rs.getDate("PAYMENTDATE"))),
                            value(rs.getString("MEMBERNAME")),
                            String.format("%.2f", rs.getDouble("AMOUNT")),
                            String.format("%.2f", rs.getDouble("DISCOUNTEDAMOUNT")),
                            value(rs.getString("PAYMENTMETHOD")),
                            value(rs.getString("STAFFNAME"))
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<String[]> getExpiringMemberTypeSummary(Date startDate, Date endDateExclusive) {
        return getGroupedSummary(
                "SELECT MEMBERTYPE, COUNT(*) AS TOTAL FROM MEMBER "
                + "WHERE EXPIREDDATE >= ? AND EXPIREDDATE < ? "
                + "GROUP BY MEMBERTYPE ORDER BY TOTAL DESC, MEMBERTYPE",
                "MEMBERTYPE", startDate, endDateExclusive);
    }

    public List<String[]> getMaintenanceStatusSummary(Date startDate, Date endDateExclusive) {
        return getGroupedSummary(
                "SELECT MAINTENANCESTATUS, COUNT(*) AS TOTAL FROM EQUIPMENTMAINTENANCE "
                + "WHERE MAINTENANCEDATE >= ? AND MAINTENANCEDATE < ? "
                + "GROUP BY MAINTENANCESTATUS ORDER BY TOTAL DESC, MAINTENANCESTATUS",
                "MAINTENANCESTATUS", startDate, endDateExclusive);
    }

    private List<String[]> getGroupedSummary(String sql, String groupColumn,
            Date startDate, Date endDateExclusive) {
        List<String[]> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setDateRange(ps, startDate, endDateExclusive);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[] {
                            value(rs.getString(groupColumn)),
                            String.valueOf(rs.getInt("TOTAL"))
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    private int getCount(String sql, Date startDate, Date endDateExclusive) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setDateRange(ps, startDate, endDateExclusive);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setDateRange(PreparedStatement ps, Date startDate, Date endDateExclusive)
            throws Exception {
        ps.setDate(1, startDate);
        ps.setDate(2, endDateExclusive);
    }

    private String value(String text) {
        return text == null || text.isBlank() ? "-" : text;
    }
}
