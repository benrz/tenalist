package id.ac.umn.tenalist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.FragmentActionListener;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.adapter.BazaarCategoryListAdapter;
import id.ac.umn.tenalist.adapter.BazaarListAdapter;
import id.ac.umn.tenalist.model.Bazaars;
import id.ac.umn.tenalist.model.BazaarsCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentActionListener fragmentActionListener;
    private JsonPlaceHolder jsonPlaceHolder;
    private RecyclerView categoryRecyclerView;
    private RecyclerView popularRecyclerView;
    private RecyclerView bazaarRecyclerView;
    private BazaarListAdapter bazaarAdapter;
    private BazaarListAdapter bazaarPopularAdapter;
    private BazaarCategoryListAdapter categoryAdapter;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        bazaarAdapter = new BazaarListAdapter(getActivity(), "HOME");
        bazaarPopularAdapter = new BazaarListAdapter(getActivity(), "HOME");
        categoryAdapter = new BazaarCategoryListAdapter(getActivity());

        bazaarRecyclerView = root.findViewById(R.id.rvBazaar);
        popularRecyclerView = root.findViewById(R.id.rvPopular);
        categoryRecyclerView = root.findViewById(R.id.rvCategory);

        bazaarRecyclerView.setAdapter(bazaarAdapter);
        popularRecyclerView.setAdapter(bazaarPopularAdapter);
        categoryRecyclerView.setAdapter(categoryAdapter);

        bazaarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager horizontalLayoutManagerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView.setLayoutManager(horizontalLayoutManagerPopular);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(horizontalLayoutManager);

        searchView = root.findViewById(R.id.svBazaar);
        searchView.setOnQueryTextListener(this);

        jsonPlaceHolder = ApiConfig.getApiConfig();
        fetchData();
        return root;
    }

    public void fetchData() {
        // GET ALL BAZAAR
        Call<Bazaars> bazaarCall = jsonPlaceHolder.getBazaar();
        bazaarCall.enqueue(new Callback<Bazaars>() {
            @Override
            public void onResponse(Call<Bazaars> call, Response<Bazaars> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Bazaars bazaars = response.body();
                List<Bazaars.Bazaar> bazaarList = bazaars.bazaar;
                bazaarAdapter.setBazaarList(bazaarList); // Set data yang telah diGET kedalam adapter
            }

            @Override
            public void onFailure(Call<Bazaars> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

        // GET ALL BAZAAR
        Call<Bazaars> bazaarPopularCall = jsonPlaceHolder.getPopularBazaar();
        bazaarPopularCall.enqueue(new Callback<Bazaars>() {
            @Override
            public void onResponse(Call<Bazaars> call, Response<Bazaars> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Bazaars bazaarsPopular = response.body();
                List<Bazaars.Bazaar> bazaarListPopular = bazaarsPopular.bazaar;
                bazaarPopularAdapter.setBazaarList(bazaarListPopular); // Set data yang telah diGET kedalam adapter
            }

            @Override
            public void onFailure(Call<Bazaars> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

        // GET ALL CATEGORY
        Call<BazaarsCategory> bazaarCategoryCall = jsonPlaceHolder.getBazaarCategory();
        bazaarCategoryCall.enqueue(new Callback<BazaarsCategory>() {
            @Override
            public void onResponse(Call<BazaarsCategory> call, Response<BazaarsCategory> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                BazaarsCategory bazaarsCategory = response.body();
                List<BazaarsCategory.BazaarCategory> bazaarCategoryList = bazaarsCategory.bazaarCategory;
                categoryAdapter.setBazaarCategoryList(bazaarCategoryList); // Set data yang telah diGET kedalam adapter
                categoryAdapter.setFragmentActionListener((FragmentActionListener) getActivity());
            }

            @Override
            public void onFailure(Call<BazaarsCategory> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (fragmentActionListener != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_SEARCH_SELECTED);
            bundle.putString(FragmentActionListener.KEY_SEARCHED_BAZAAR, query);
            fragmentActionListener.onActionPerformed(bundle);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
