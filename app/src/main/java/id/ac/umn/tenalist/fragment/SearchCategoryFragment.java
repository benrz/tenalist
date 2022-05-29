package id.ac.umn.tenalist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.FragmentActionListener;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.adapter.BazaarListAdapter;
import id.ac.umn.tenalist.model.Bazaars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCategoryFragment extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView bazaarRecyclerView;
    private BazaarListAdapter bazaarAdapter;
    private SearchView searchView;
    FragmentActionListener fragmentActionListener;
    private JsonPlaceHolder jsonPlaceHolder;
    private String filterText = null;
    private boolean searchFlag = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_category, container, false);

        bazaarAdapter = new BazaarListAdapter(getActivity(), "SEARCHCATEGORY");
        bazaarRecyclerView = root.findViewById(R.id.rvSearchCategory);
        bazaarRecyclerView.setAdapter(bazaarAdapter);
        bazaarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = root.findViewById(R.id.svBazaar);
        searchView.setOnQueryTextListener(this);

        jsonPlaceHolder = ApiConfig.getApiConfig();

        Bundle bundle = getArguments();
        int mode = bundle.getInt(FragmentActionListener.ACTION_KEY);
        if(bazaarAdapter != null){
            switch (mode){
                case FragmentActionListener.ACTION_VALUE_CATEGORY_SELECTED:{
                    Log.d("FRAGMENT CATEGORY INT", String.valueOf(bundle.getInt(fragmentActionListener.KEY_CATEGORY_BAZAAR)));
                    filterText = bundle.getString(fragmentActionListener.KEY_CATEGORY_BAZAAR);
                    searchFlag = false;
                };
                break;
                case FragmentActionListener.ACTION_VALUE_SEARCH_SELECTED:{
                    Log.d("FRAGMENT SEARCH STRING", bundle.getString(fragmentActionListener.KEY_SEARCHED_BAZAAR));
                    filterText = bundle.getString(fragmentActionListener.KEY_SEARCHED_BAZAAR);
                    searchFlag = true;
                };
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + mode);
            }
        }

        fetchData();
        return root;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    public void fetchData(){
        Call<Bazaars> bazaarCall = jsonPlaceHolder.getBazaar();
        bazaarCall.enqueue(new Callback<Bazaars>() {
            @Override
            public void onResponse(Call<Bazaars> call, Response<Bazaars> response) {
                if(!response.isSuccessful()){
                    return;
                }

                Bazaars bazaars = response.body();
                List<Bazaars.Bazaar> bazaarList = bazaars.bazaar;
                bazaarAdapter.setBazaarList(bazaarList); // Set data yang telah diGET kedalam adapter
//                bazaarAdapter.notifyDataSetChanged();

                if(searchFlag)
                    bazaarAdapter.searchedBazaarList(filterText);
                else
                    bazaarAdapter.categoryBazaarList(filterText);
            }

            @Override
            public void onFailure(Call<Bazaars> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getActivity(), "SEARCHED: "+query, Toast.LENGTH_LONG);
        if(bazaarAdapter != null) {
            bazaarAdapter.searchedBazaarList(query);
            Log.d("FRAGMENT SEARCH", query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}