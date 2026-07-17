package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.DiscountsAndPromotions;
import util.DBConnection;

public class DashboardDAO {

    public int getTotalMembers() {
        return getIntValue("SELECT COUNT(*) FROM MEMBER");
    }

    public int getActiveMembers() {
        String sql = "SELECT COUNT(*) FROM MEMBER "
                + "WHERE EXPIREDDATE IS NOT NULL "
                + "AND TRUNC(EXPIREDDATE) >= TRUNC(SYSDATE)";
        return getIntValue(sql);
    }

    public int getTotalPayments() {
        return getIntValue("SELECT COUNT(*) FROM PAYMENT");
    }

    public double getTotalRevenue() {
        return getDoubleValue(
                "SELECT NVL(SUM(DISCOUNTEDAMOUNT), 0) FROM PAYMENT");
    }

    public int getTotalEquipment() {
        return getIntValue("SELECT COUNT(*) FROM EQUIPMENT");
    }

    public int getPendingMaintenance() {
        String sql = "SELECT COUNT(*) FROM EQUIPMENTMAINTENANCE "
                + "WHERE UPPER(TRIM(MAINTENANCESTATUS)) "
                + "IN ('PENDING', 'IN PROGRESS')";
        return getIntValue(sql);
    }

    private int getIntValue(String sql) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private double getDoubleValue(String sql) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getDouble(1) : 0.0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Returns the active promotion that will end first.
     * This is more useful on a dashboard than simply returning the
     * promotion with the largest ID, which may be expired or upcoming.
     */
    public DiscountsAndPromotions getCurrentPromotion() {
        String sql = "SELECT PROMOID, PROMONAME, DISCOUNTPERCENT, "
                + "STARTDATE, ENDDATE, PROMODESC "
                + "FROM DISCOUNTSANDPROMOTIONS "
                + "WHERE TRUNC(SYSDATE) BETWEEN TRUNC(STARTDATE) "
                + "AND TRUNC(ENDDATE) "
                + "ORDER BY ENDDATE ASC, PROMOID DESC "
                + "FETCH FIRST 1 ROWS ONLY";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) {
                return null;
            }

            DiscountsAndPromotions promo = new DiscountsAndPromotions();
            promo.setPromoID(rs.getString("PROMOID"));
            promo.setPromoName(rs.getString("PROMONAME"));
            promo.setDiscountPercent(rs.getDouble("DISCOUNTPERCENT"));
            promo.setStartDate(rs.getDate("STARTDATE"));
            promo.setEndDate(rs.getDate("ENDDATE"));
            promo.setPromoDesc(rs.getString("PROMODESC"));
            return promo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Each row contains:
     * [0] activity title
     * [1] activity details
     * [2] activity date (yyyy-MM-dd)
     * [3] status
     */
    public List<String[]> getRecentActivities() {
        List<String[]> activities = new ArrayList<>();

        String sql = "SELECT ACTIVITY, DETAILS, ACTIVITYDATE, STATUS "
                + "FROM ( "
                + "    SELECT 'Payment received' AS ACTIVITY, "
                + "           NVL(M.MEMBERNAME, P.MEMBERID) "
                + "           || ' paid RM ' "
                + "           || TO_CHAR(P.DISCOUNTEDAMOUNT, 'FM9999990.00') "
                + "           AS DETAILS, "
                + "           P.PAYMENTDATE AS ACTIVITYDATE, "
                + "           'COMPLETED' AS STATUS "
                + "    FROM PAYMENT P "
                + "    LEFT JOIN MEMBER M ON M.MEMBERID = P.MEMBERID "
                + "    UNION ALL "
                + "    SELECT 'Equipment maintenance' AS ACTIVITY, "
                + "           NVL(E.EQUIPMENTNAME, EM.EQUIPMENTID) "
                + "           || ' - ' || EM.MAINTENANCEDESC AS DETAILS, "
                + "           EM.MAINTENANCEDATE AS ACTIVITYDATE, "
                + "           EM.MAINTENANCESTATUS AS STATUS "
                + "    FROM EQUIPMENTMAINTENANCE EM "
                + "    LEFT JOIN EQUIPMENT E "
                + "           ON E.EQUIPMENTID = EM.EQUIPMENTID "
                + ") "
                + "ORDER BY ACTIVITYDATE DESC "
                + "FETCH FIRST 6 ROWS ONLY";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] activity = new String[4];
                activity[0] = rs.getString("ACTIVITY");
                activity[1] = rs.getString("DETAILS");
                activity[2] = String.valueOf(rs.getDate("ACTIVITYDATE"));
                activity[3] = rs.getString("STATUS");
                activities.add(activity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return activities;
    }

    /**
     * Each row contains:
     * [0] member name
     * [1] member type
     * [2] expiry date (yyyy-MM-dd)
     * [3] expiry label
     */
    public List<String[]> getUpcomingExpiries() {
        List<String[]> expiries = new ArrayList<>();

        String sql = "SELECT MEMBERNAME, MEMBERTYPE, EXPIREDDATE, "
                + "       CASE "
                + "           WHEN TRUNC(EXPIREDDATE) = TRUNC(SYSDATE) "
                + "               THEN 'EXPIRES TODAY' "
                + "           ELSE 'EXPIRING SOON' "
                + "       END AS EXPIRYSTATUS "
                + "FROM MEMBER "
                + "WHERE EXPIREDDATE IS NOT NULL "
                + "  AND TRUNC(EXPIREDDATE) BETWEEN TRUNC(SYSDATE) "
                + "      AND TRUNC(SYSDATE) + 45 "
                + "ORDER BY EXPIREDDATE ASC "
                + "FETCH FIRST 5 ROWS ONLY";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] expiry = new String[4];
                expiry[0] = rs.getString("MEMBERNAME");
                expiry[1] = rs.getString("MEMBERTYPE");
                expiry[2] = String.valueOf(rs.getDate("EXPIREDDATE"));
                expiry[3] = rs.getString("EXPIRYSTATUS");
                expiries.add(expiry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return expiries;
    }
}
