package bean;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberID;
    private String memberName;
    private String memberEmail;
    private Date expiredDate;
    private String memberPhone;
    private String staffID;
    private String memberType;
    private boolean deletionBlocked;

    public Member() {
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean isDeletionBlocked() {
        return deletionBlocked;
    }

    public void setDeletionBlocked(boolean deletionBlocked) {
        this.deletionBlocked = deletionBlocked;
    }
}
