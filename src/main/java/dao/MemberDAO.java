package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import bean.Member;
import util.DBConnection;

public class MemberDAO {

    public void insertMember(Member member) {
        String sql = "INSERT INTO MEMBER "
                + "(MEMBERNAME, MEMBEREMAIL, EXPIREDDATE, MEMBERPHONE, STAFFID, MEMBERTYPE) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getMemberName());
            ps.setString(2, member.getMemberEmail());

            if (member.getExpiredDate() != null) {
                ps.setDate(3, new java.sql.Date(member.getExpiredDate().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, member.getMemberPhone());
            ps.setString(5, member.getStaffID());
            ps.setString(6, member.getMemberType());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Member> getAllMember() {
        List<Member> list = new ArrayList<>();

        String sql = "SELECT M.*, "
                + "CASE WHEN EXISTS ("
                + "SELECT 1 FROM PAYMENT P WHERE P.MEMBERID = M.MEMBERID"
                + ") THEN 1 ELSE 0 END AS DELETIONBLOCKED "
                + "FROM MEMBER M ORDER BY M.MEMBERID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Member m = new Member();

                m.setMemberID(rs.getString("MEMBERID"));
                m.setMemberName(rs.getString("MEMBERNAME"));
                m.setMemberEmail(rs.getString("MEMBEREMAIL"));
                m.setExpiredDate(rs.getDate("EXPIREDDATE"));
                m.setMemberPhone(rs.getString("MEMBERPHONE"));
                m.setStaffID(rs.getString("STAFFID"));
                m.setMemberType(rs.getString("MEMBERTYPE"));
                m.setDeletionBlocked(rs.getInt("DELETIONBLOCKED") == 1);

                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Member getMember(String id) {
        Member m = null;

        String sql = "SELECT * FROM MEMBER WHERE MEMBERID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new Member();

                    m.setMemberID(rs.getString("MEMBERID"));
                    m.setMemberName(rs.getString("MEMBERNAME"));
                    m.setMemberEmail(rs.getString("MEMBEREMAIL"));
                    m.setExpiredDate(rs.getDate("EXPIREDDATE"));
                    m.setMemberPhone(rs.getString("MEMBERPHONE"));
                    m.setStaffID(rs.getString("STAFFID"));
                    m.setMemberType(rs.getString("MEMBERTYPE"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;
    }

    public void updateMember(Member m) {
        String sql = "UPDATE MEMBER SET MEMBERNAME=?, MEMBEREMAIL=?, EXPIREDDATE=?, "
                + "MEMBERPHONE=?, STAFFID=?, MEMBERTYPE=? WHERE MEMBERID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getMemberName());
            ps.setString(2, m.getMemberEmail());

            if (m.getExpiredDate() != null) {
                ps.setDate(3, new java.sql.Date(m.getExpiredDate().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, m.getMemberPhone());
            ps.setString(5, m.getStaffID());
            ps.setString(6, m.getMemberType());
            ps.setString(7, m.getMemberID());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasIntegrityConstraint(String memberID) {
        String sql = "SELECT CASE WHEN EXISTS ("
                + "SELECT 1 FROM PAYMENT WHERE MEMBERID = ?"
                + ") THEN 1 ELSE 0 END AS DELETIONBLOCKED FROM DUAL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, memberID);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("DELETIONBLOCKED") == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fail safely when the dependency check cannot be completed.
            return true;
        }
    }

    public boolean deleteMember(String id) {
        String sql = "DELETE FROM MEMBER WHERE MEMBERID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
