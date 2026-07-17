package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpDAO {

    private static Connection con = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    private static final String SELECT_CUST_LOGIN = "SELECT * FROM STAFF WHERE STAFFUSERNAME = ? AND STAFFPASSWORD = ?";
    private static final String INSERT_CUST = "INSERT INTO STAFF(STAFFNAME, STAFFEMAIL, STAFFPHONE, STAFFROLE, STAFFUSERNAME, STAFFPASSWORD) VALUES (?, ?, ?, ?, ?, ?)";

    // MD5 helper
    private static String toMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    // Login
    public static bean.Staff login(bean.Staff staff) throws SQLException, NoSuchAlgorithmException {
        try {
            con = util.DBConnection.getConnection();
            ps = con.prepareStatement(SELECT_CUST_LOGIN);
            ps.setString(1, staff.getStaffUsername());
            ps.setString(2, toMD5(staff.getStaffPassword()));
            System.out.println(ps);

            rs = ps.executeQuery();

            if (rs.next()) {
                staff.setStaffName(rs.getString("CUSTNAME"));
                staff.setStaffUsername(rs.getString("CUSTUSERNAME"));
                staff.setLoggedIn(true);
            } else {
                staff.setLoggedIn(false);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    // Sign Up
    public static boolean signUp(bean.Staff staff) throws SQLException, NoSuchAlgorithmException {
        boolean success = false;
        try {
            con = util.DBConnection.getConnection();
            ps = con.prepareStatement(INSERT_CUST);
            ps.setString(1, staff.getStaffName());
            ps.setString(2, staff.getStaffEmail());
            ps.setString(3, staff.getStaffPhone());
            ps.setString(4, staff.getStaffRole().isEmpty()     ? "Staff"  : staff.getStaffRole());
            System.out.println(ps);
            ps.setString(5, staff.getStaffUsername());
            ps.setString(6, toMD5(staff.getStaffPassword()));


            int rows = ps.executeUpdate();
            success = rows > 0;
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}