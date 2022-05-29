package id.ac.umn.tenalist;

import com.google.gson.annotations.SerializedName;

import id.ac.umn.tenalist.model.BazaarTransaction;

public class TransactionData {
    @SerializedName("result")
    public BazaarTransaction bazaarTransaction;
    @SerializedName("success")
    public int success;

    public  TransactionData(){}
    public TransactionData(BazaarTransaction bazaarTransaction, int success) {
        this.bazaarTransaction = bazaarTransaction;
        this.success = success;
    }

    public BazaarTransaction getBazaarTransaction() {
        return bazaarTransaction;
    }

    public void setBazaarTransaction(BazaarTransaction bazaarTransaction) {
        this.bazaarTransaction = bazaarTransaction;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
