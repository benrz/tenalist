package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BazaarsCategory {
    @SerializedName("categories")
    public List<BazaarsCategory.BazaarCategory> bazaarCategory = null;

    public class BazaarCategory{
        private int bazaarCategoryId;
        private String bazaarCategoryName;

        public int getBazaarCategoryId() {
            return bazaarCategoryId;
        }

        public void setBazaarCategoryId(int bazaarCategoryId) {
            this.bazaarCategoryId = bazaarCategoryId;
        }

        public String getBazaarCategoryName() {
            return bazaarCategoryName;
        }

        public void setBazaarCategoryName(String bazaarCategoryName) {
            this.bazaarCategoryName = bazaarCategoryName;
        }
    }
}
