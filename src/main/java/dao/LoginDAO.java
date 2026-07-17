package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    private static Connection con = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    private static final String SELECT_USER_LOGIN =
            "SELECT * FROM STAFF WHERE staffUsername = ? AND staffPassword = ?";

    public static bean.Staff login(bean.Staff user)
            throws SQLException, NoSuchAlgorithmException {

        // Convert password to MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(user.getStaffPassword().getBytes());

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        try {

            con = util.DBConnection.getConnection();

            ps = con.prepareStatement(SELECT_USER_LOGIN);
            ps.setString(1, user.getStaffUsername());
            ps.setString(2, sb.toString());

            System.out.println(ps);

            rs = ps.executeQuery();

            if (rs.next()) {

                user.setStaffID(rs.getString("staffID"));
                user.setStaffName(rs.getString("staffName"));
                user.setStaffRole(rs.getString("staffRole"));
                user.setStaffEmail(rs.getString("staffEmail"));
                user.setStaffPhone(rs.getString("staffPhone"));
                user.setHireDate(rs.getDate("hireDate"));
                user.setStaffUsername(rs.getString("staffUsername"));
                user.setStaffPassword(rs.getString("staffPassword"));

                user.setLoggedIn(true);

            } else {

                user.setLoggedIn(false);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            if (rs != null)
                rs.close();

            if (ps != null)
                ps.close();

            if (con != null)
                con.close();
        }

        return user;
    }
}