package dao;

import bean.Equipment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    public List<Equipment> getAllEquipment() {
        List<Equipment> list = new ArrayList<>();
        String query = "SELECT E.*, "
                + "CASE WHEN (SELECT COUNT(*) FROM EQUIPMENTMAINTENANCE M "
                + "WHERE M.EQUIPMENTID = E.EQUIPMENTID) = 0 THEN 1 ELSE 0 END AS CAN_DELETE "
                + "FROM EQUIPMENT E ORDER BY E.EQUIPMENTID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Equipment equipment = mapEquipment(rs);
                equipment.setDeletable(rs.getInt("CAN_DELETE") == 1);
                list.add(equipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Equipment getEquipmentById(String id) {
        Equipment equipment = null;
        String query = "SELECT E.*, "
                + "CASE WHEN (SELECT COUNT(*) FROM EQUIPMENTMAINTENANCE M "
                + "WHERE M.EQUIPMENTID = E.EQUIPMENTID) = 0 THEN 1 ELSE 0 END AS CAN_DELETE "
                + "FROM EQUIPMENT E WHERE E.EQUIPMENTID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    equipment = mapEquipment(rs);
                    equipment.setDeletable(rs.getInt("CAN_DELETE") == 1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return equipment;
    }

    public boolean addEquipment(Equipment equipment) {
        String query = "INSERT INTO EQUIPMENT "
                + "(EQUIPMENTNAME, CATEGORY, PURCHASEDATE, EQUSTATUS) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, equipment.getEquipmentName());
            pstmt.setString(2, equipment.getCategory());
            pstmt.setDate(3, equipment.getPurchaseDate());
            pstmt.setString(4, equipment.getEquStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateEquipment(Equipment equipment) {
        String query = "UPDATE EQUIPMENT SET EQUIPMENTNAME=?, CATEGORY=?, "
                + "PURCHASEDATE=?, EQUSTATUS=? WHERE EQUIPMENTID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, equipment.getEquipmentName());
            pstmt.setString(2, equipment.getCategory());
            pstmt.setDate(3, equipment.getPurchaseDate());
            pstmt.setString(4, equipment.getEquStatus());
            pstmt.setString(5, equipment.getEquipmentID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean canDeleteEquipment(String id) {
        String query = "SELECT COUNT(*) FROM EQUIPMENTMAINTENANCE WHERE EQUIPMENTID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteEquipment(String id) {
        if (!canDeleteEquipment(id)) {
            return false;
        }

        String query = "DELETE FROM EQUIPMENT WHERE EQUIPMENTID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            // A final database guard protects against a reference created after the check.
            ex.printStackTrace();
            return false;
        }
    }

    public int getEquipmentCount() {
        int count = 0;
        String query = "SELECT COUNT(*) AS TOTAL FROM EQUIPMENT";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                count = rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private Equipment mapEquipment(ResultSet rs) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentID(rs.getString("EQUIPMENTID"));
        equipment.setEquipmentName(rs.getString("EQUIPMENTNAME"));
        equipment.setCategory(rs.getString("CATEGORY"));
        equipment.setPurchaseDate(rs.getDate("PURCHASEDATE"));
        equipment.setEquStatus(rs.getString("EQUSTATUS"));
        return equipment;
    }
}
