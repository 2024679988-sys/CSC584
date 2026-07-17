package bean;

import java.sql.Date;

public class Equipment {
    private String equipmentID;
    private String equipmentName;
    private String category;
    private Date purchaseDate;
    private String equStatus;
    private boolean deletable = true;

    public Equipment() {
    }

    public Equipment(String equipmentID, String equipmentName, String category,
            Date purchaseDate, String equStatus) {
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.equStatus = equStatus;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getEquStatus() {
        return equStatus;
    }

    public void setEquStatus(String equStatus) {
        this.equStatus = equStatus;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
