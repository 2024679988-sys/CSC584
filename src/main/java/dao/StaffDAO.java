package dao;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Staff;
import util.DBConnection;

public class StaffDAO {

    private static String md5(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void addStaff(Staff staff) {
        String sql = "INSERT INTO STAFF "
                + "(STAFFNAME, STAFFEMAIL, STAFFPHONE, HIREDATE, STAFFROLE, STAFFUSERNAME, STAFFPASSWORD) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getStaffName());
            ps.setString(2, staff.getStaffEmail());
            ps.setString(3, staff.getStaffPhone());

            if (staff.getHireDate() != null) {
                ps.setDate(4, new java.sql.Date(staff.getHireDate().getTime()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, staff.getStaffRole());
            ps.setString(6, staff.getStaffUsername());
            ps.setString(7, md5(staff.getStaffPassword()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();

        String sql = "SELECT S.*, "
                + "CASE WHEN "
                + "EXISTS (SELECT 1 FROM MEMBER M WHERE M.STAFFID = S.STAFFID) OR "
                + "EXISTS (SELECT 1 FROM PAYMENT P WHERE P.STAFFID = S.STAFFID) OR "
                + "EXISTS (SELECT 1 FROM EQUIPMENTMAINTENANCE E WHERE E.STAFFID = S.STAFFID) "
                + "THEN 1 ELSE 0 END AS DELETIONBLOCKED "
                + "FROM STAFF S ORDER BY S.STAFFID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff();

                staff.setStaffID(rs.getString("STAFFID"));
                staff.setStaffName(rs.getString("STAFFNAME"));
                staff.setStaffEmail(rs.getString("STAFFEMAIL"));
                staff.setStaffPhone(rs.getString("STAFFPHONE"));
                staff.setHireDate(rs.getDate("HIREDATE"));
                staff.setStaffRole(rs.getString("STAFFROLE"));
                staff.setStaffUsername(rs.getString("STAFFUSERNAME"));
                staff.setStaffPassword(rs.getString("STAFFPASSWORD"));
                staff.setDeletionBlocked(rs.getInt("DELETIONBLOCKED") == 1);

                staffList.add(staff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return staffList;
    }

    public static Staff getStaffById(String staffID) {
        Staff staff = null;

        String sql = "SELECT * FROM STAFF WHERE STAFFID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staffID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    staff = new Staff();

                    staff.setStaffID(rs.getString("STAFFID"));
                    staff.setStaffName(rs.getString("STAFFNAME"));
                    staff.setStaffEmail(rs.getString("STAFFEMAIL"));
                    staff.setStaffPhone(rs.getString("STAFFPHONE"));
                    staff.setHireDate(rs.getDate("HIREDATE"));
                    staff.setStaffRole(rs.getString("STAFFROLE"));
                    staff.setStaffUsername(rs.getString("STAFFUSERNAME"));
                    staff.setStaffPassword(rs.getString("STAFFPASSWORD"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return staff;
    }

    public static void updateStaff(Staff staff) {
        String sql = "UPDATE STAFF SET "
                + "STAFFNAME=?, STAFFEMAIL=?, STAFFPHONE=?, HIREDATE=?, "
                + "STAFFROLE=?, STAFFUSERNAME=?, STAFFPASSWORD=? "
                + "WHERE STAFFID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getStaffName());
            ps.setString(2, staff.getStaffEmail());
            ps.setString(3, staff.getStaffPhone());

            if (staff.getHireDate() != null) {
                ps.setDate(4, new java.sql.Date(staff.getHireDate().getTime()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, staff.getStaffRole());
            ps.setString(6, staff.getStaffUsername());

            if (staff.getStaffPassword() != null && !staff.getStaffPassword().trim().isEmpty()) {
                ps.setString(7, md5(staff.getStaffPassword()));
            } else {
                Staff oldStaff = getStaffById(staff.getStaffID());
                ps.setString(7, oldStaff.getStaffPassword());
            }

            ps.setString(8, staff.getStaffID());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasIntegrityConstraint(String staffID) {
        String sql = "SELECT CASE WHEN "
                + "EXISTS (SELECT 1 FROM MEMBER WHERE STAFFID = ?) OR "
                + "EXISTS (SELECT 1 FROM PAYMENT WHERE STAFFID = ?) OR "
                + "EXISTS (SELECT 1 FROM EQUIPMENTMAINTENANCE WHERE STAFFID = ?) "
                + "THEN 1 ELSE 0 END AS DELETIONBLOCKED FROM DUAL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staffID);
            ps.setString(2, staffID);
            ps.setString(3, staffID);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("DELETIONBLOCKED") == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fail safely: do not allow deletion when the dependency check cannot be completed.
            return true;
        }
    }

    public static boolean deleteStaff(String staffID) {
        String sql = "DELETE FROM STAFF WHERE STAFFID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staffID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}