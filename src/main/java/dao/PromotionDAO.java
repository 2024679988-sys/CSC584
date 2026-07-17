package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.DiscountsAndPromotions;
import util.DBConnection;

public class PromotionDAO {

    public void addPromotion(DiscountsAndPromotions promo) {
        String sql = "INSERT INTO DiscountsAndPromotions "
                + "(promoName, discountPercent, startDate, endDate, promoDesc) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promo.getPromoName());
            ps.setDouble(2, promo.getDiscountPercent());
            ps.setDate(3, new java.sql.Date(promo.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(promo.getEndDate().getTime()));
            ps.setString(5, promo.getPromoDesc());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DiscountsAndPromotions> getAllPromotions() {
        List<DiscountsAndPromotions> list = new ArrayList<>();

        String sql = "SELECT D.*, "
                + "CASE WHEN EXISTS ("
                + "SELECT 1 FROM PAYMENT P WHERE P.PROMOID = D.PROMOID"
                + ") THEN 1 ELSE 0 END AS DELETIONBLOCKED "
                + "FROM DISCOUNTSANDPROMOTIONS D ORDER BY D.PROMOID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DiscountsAndPromotions promo = new DiscountsAndPromotions();

                promo.setPromoID(rs.getString("PROMOID"));
                promo.setPromoName(rs.getString("PROMONAME"));
                promo.setDiscountPercent(rs.getDouble("DISCOUNTPERCENT"));
                promo.setStartDate(rs.getDate("STARTDATE"));
                promo.setEndDate(rs.getDate("ENDDATE"));
                promo.setPromoDesc(rs.getString("PROMODESC"));
                promo.setDeletionBlocked(rs.getInt("DELETIONBLOCKED") == 1);

                list.add(promo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DiscountsAndPromotions getPromotionByID(String promoID) {
        DiscountsAndPromotions promo = null;

        String sql = "SELECT * FROM DiscountsAndPromotions WHERE promoID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promoID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    promo = new DiscountsAndPromotions();

                    promo.setPromoID(rs.getString("promoID"));
                    promo.setPromoName(rs.getString("promoName"));
                    promo.setDiscountPercent(rs.getDouble("discountPercent"));
                    promo.setStartDate(rs.getDate("startDate"));
                    promo.setEndDate(rs.getDate("endDate"));
                    promo.setPromoDesc(rs.getString("promoDesc"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return promo;
    }

    public void updatePromotion(DiscountsAndPromotions promo) {
        String sql = "UPDATE DiscountsAndPromotions SET "
                + "promoName=?, discountPercent=?, startDate=?, endDate=?, promoDesc=? "
                + "WHERE promoID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promo.getPromoName());
            ps.setDouble(2, promo.getDiscountPercent());
            ps.setDate(3, new java.sql.Date(promo.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(promo.getEndDate().getTime()));
            ps.setString(5, promo.getPromoDesc());
            ps.setString(6, promo.getPromoID());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasIntegrityConstraint(String promoID) {
        String sql = "SELECT CASE WHEN EXISTS ("
                + "SELECT 1 FROM PAYMENT WHERE PROMOID = ?"
                + ") THEN 1 ELSE 0 END AS DELETIONBLOCKED FROM DUAL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promoID);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("DELETIONBLOCKED") == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fail safely when the dependency check cannot be completed.
            return true;
        }
    }

    public boolean deletePromotion(String promoID) {
        String sql = "DELETE FROM DiscountsAndPromotions WHERE promoID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, promoID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
