package bean;

import java.io.Serializable;
import java.util.Date;

public class DiscountsAndPromotions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String promoID;
    private String promoName;
    private double discountPercent;
    private Date startDate;
    private Date endDate;
    private String promoDesc;
    private boolean deletionBlocked;

    public DiscountsAndPromotions() {
    }

    public String getPromoID() {
        return promoID;
    }

    public void setPromoID(String promoID) {
        this.promoID = promoID;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPromoDesc() {
        return promoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        this.promoDesc = promoDesc;
    }

    public boolean isDeletionBlocked() {
        return deletionBlocked;
    }

    public void setDeletionBlocked(boolean deletionBlocked) {
        this.deletionBlocked = deletionBlocked;
    }
}
