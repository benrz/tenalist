package id.ac.umn.tenalist.Interface;

import android.os.Bundle;

public interface FragmentActionListener {
    String ACTION_KEY = "action_key";
    int ACTION_VALUE_CATEGORY_SELECTED = 1;
    int ACTION_VALUE_SEARCH_SELECTED = 2;
    int ACTION_VALUE_DETAIL_SELECTED = 3;
    String KEY_CATEGORY_BAZAAR = "KEY_CATEGORY_BAZAAR";
    String KEY_SEARCHED_BAZAAR = "KEY_SEARCHED_BAZAAR";
    String KEY_SELECTED_BAZAAR = "KEY_SELECTED_BAZAAR";

    void onActionPerformed(Bundle bundle);
}
