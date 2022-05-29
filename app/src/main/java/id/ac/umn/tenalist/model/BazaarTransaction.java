package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

public class BazaarTransaction {
    @SerializedName("bazaarTransactionId")
    private int bazaarTransactionId;

    @SerializedName("bazaarId")
    private int bazaarId;

    @SerializedName("bazaarBoothId")
    private int bazaarBoothId;

    @SerializedName("tenantId")
    private int tenantId;

    @SerializedName("organizerId")
    private int organizerId;

    @SerializedName("totalPaid")
    private int totalPaid;

    @SerializedName("paymentStatus")
    private int paymentStatus;

    @SerializedName("accountNum")
    private String accountNum;

    @SerializedName("accountName")
    private String accountName;

    public BazaarTransaction(){}
    public BazaarTransaction(int bazaarTransactionId, int bazaarId, int bazaarBoothId, int tenantId, int organizerId,
                             int totalPaid, int paymentStatus, String accountNum, String accountName) {
        this.bazaarTransactionId = bazaarTransactionId;
        this.bazaarId = bazaarId;
        this.bazaarBoothId = bazaarBoothId;
        this.tenantId = tenantId;
        this.organizerId = organizerId;
        this.totalPaid = totalPaid;
        this.paymentStatus = paymentStatus;
        this.accountNum = accountNum;
        this.accountName = accountName;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBazaarTransactionId() {
        return bazaarTransactionId;
    }
    public void setBazaarTransactionId(int bazaarTransactionId) {
        this.bazaarTransactionId = bazaarTransactionId;
    }
    public int getBazaarId() {
        return bazaarId;
    }
    public void setBazaarId(int bazaarId) {
        this.bazaarId = bazaarId;
    }
    public int getBazaarBoothId() {
        return bazaarBoothId;
    }
    public void setBazaarBoothId(int bazaarBoothId) {
        this.bazaarBoothId = bazaarBoothId;
    }
    public int getTenantId() {
        return tenantId;
    }
    public void setTenantId(int tenantId) {
        tenantId = tenantId;
    }
    public int getOrganizerId() {
        return organizerId;
    }
    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }
    public int getTotalPaid() {
        return totalPaid;
    }
    public void setTotalPaid(int totalPaid) {
        this.totalPaid = totalPaid;
    }
    public int getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
