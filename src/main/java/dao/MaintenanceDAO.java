package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bean.Equipment;
import bean.EquipmentMaintenance;
import util.DBConnection;

public class MaintenanceDAO {

    public List<EquipmentMaintenance> getAllMaintenance() {
        List<EquipmentMaintenance> list = new ArrayList<>();

        String sql = "SELECT * FROM EQUIPMENTMAINTENANCE ORDER BY MAINTENANCEID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EquipmentMaintenance m = new EquipmentMaintenance();

                m.setMaintenanceID(rs.getString("MAINTENANCEID"));
                m.setStaffID(rs.getString("STAFFID"));
                m.setEquipmentID(rs.getString("EQUIPMENTID"));
                m.setMaintenanceDate(rs.getDate("MAINTENANCEDATE"));
                m.setMaintenanceDesc(rs.getString("MAINTENANCEDESC"));
                m.setMaintenanceStatus(rs.getString("MAINTENANCESTATUS"));

                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public EquipmentMaintenance getMaintenanceById(String maintenanceID) {
        EquipmentMaintenance m = null;

        String sql = "SELECT * FROM EQUIPMENTMAINTENANCE WHERE MAINTENANCEID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maintenanceID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new EquipmentMaintenance();

                    m.setMaintenanceID(rs.getString("MAINTENANCEID"));
                    m.setStaffID(rs.getString("STAFFID"));
                    m.setEquipmentID(rs.getString("EQUIPMENTID"));
                    m.setMaintenanceDate(rs.getDate("MAINTENANCEDATE"));
                    m.setMaintenanceDesc(rs.getString("MAINTENANCEDESC"));
                    m.setMaintenanceStatus(rs.getString("MAINTENANCESTATUS"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;
    }

    public void addMaintenance(EquipmentMaintenance m) {
        String sql = "INSERT INTO EQUIPMENTMAINTENANCE "
                + "(STAFFID, EQUIPMENTID, MAINTENANCEDATE, MAINTENANCEDESC, MAINTENANCESTATUS) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getStaffID());
            ps.setString(2, m.getEquipmentID());

            if (m.getMaintenanceDate() != null) {
                ps.setDate(3, new java.sql.Date(m.getMaintenanceDate().getTime()));
            } else {
                ps.setDate(3, null);
            }

            ps.setString(4, m.getMaintenanceDesc());
            ps.setString(5, m.getMaintenanceStatus());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMaintenance(EquipmentMaintenance m) {
        String sql = "UPDATE EQUIPMENTMAINTENANCE SET "
                + "STAFFID=?, EQUIPMENTID=?, MAINTENANCEDATE=?, "
                + "MAINTENANCEDESC=?, MAINTENANCESTATUS=? "
                + "WHERE MAINTENANCEID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getStaffID());
            ps.setString(2, m.getEquipmentID());

            if (m.getMaintenanceDate() != null) {
                ps.setDate(3, new java.sql.Date(m.getMaintenanceDate().getTime()));
            } else {
                ps.setDate(3, null);
            }

            ps.setString(4, m.getMaintenanceDesc());
            ps.setString(5, m.getMaintenanceStatus());
            ps.setString(6, m.getMaintenanceID());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMaintenance(String maintenanceID) {
        String sql = "DELETE FROM EQUIPMENTMAINTENANCE WHERE MAINTENANCEID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maintenanceID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> list = new ArrayList<>();

        String sql = "SELECT EQUIPMENTID, EQUIPMENTNAME FROM EQUIPMENT ORDER BY EQUIPMENTID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Equipment e = new Equipment();

                e.setEquipmentID(rs.getString("EQUIPMENTID"));
                e.setEquipmentName(rs.getString("EQUIPMENTNAME"));

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}