package id.ac.umn.tenalist.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.activity.MainActivity;
import id.ac.umn.tenalist.activity.DetailActivity;
import id.ac.umn.tenalist.model.Bazaars;

import static id.ac.umn.tenalist.UserData.formattedDate;

public class BazaarListAdapter extends RecyclerView.Adapter {
    private final LayoutInflater mInflater;
    private List<Bazaars.Bazaar> bazaarList;
    private List<Bazaars.Bazaar> filteredList;
    private String tag;
    Context context = null;

    public BazaarListAdapter(FragmentActivity context, String tag){
        this.mInflater = LayoutInflater.from(context);
        this.tag = tag;
        this.context = context;
        Log.d("TAG DI ADAPTER", tag);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(tag == "SEARCHCATEGORY"){
            Log.d("CREATE VIEW HOLDER", "SEARCHCATEGORY ONCREATE");
            View itemView = mInflater.inflate(R.layout.search_category_item_layout, parent, false);
            return new SearchedCategoryViewHolder(itemView);
        }
        Log.d("CREATE VIEW HOLDER", "HOME ONCREATE");
        View itemView = mInflater.inflate(R.layout.bazaar_item_layout, parent, false);
        return new BazaarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(bazaarList != null && filteredList != null){
            if(tag == "HOME") {
                Log.d("ONBIND VIEW HOLDER", "HOME ONBIND");
                final Bazaars.Bazaar bazaar = bazaarList.get(position);

                BazaarViewHolder bazaarViewHolder = (BazaarViewHolder) holder;

                Glide.with(context)
                        .load(ApiConfig.FINAL_URL + "poster/" + bazaar.getBazaarPoster())
                        //.signature(new ObjectKey(String.valueOf(UserData.picCount)))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("GLIDE ERROR", "ERROR FETCHING POSTER");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //progress_bar_img.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(bazaarViewHolder.ivLogoBazaar);
                bazaarViewHolder.tvNamaBazaar.setText(bazaar.getBazaarName());

                bazaarViewHolder.cvBazaar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent detailIntent = new Intent(context, DetailActivity.class);
                        int bazaarId = bazaar.getBazaarId();
                        detailIntent.putExtra("bazaarId", bazaarId);
                        context.startActivity(detailIntent);
//                        Snackbar.make(v, "Bazaar Name: " + bazaar.getBazaarName() + ", Bazaar Id: " + bazaar.getBazaarId(), Snackbar.LENGTH_LONG).show(); // Bagian JASON
                    }
                });
            }
            else if(tag == "SEARCHCATEGORY"){
                Log.d("ONBIND VIEW HOLDER", "SEARCHCATEGORY ONBIND");
                Log.d("onBindViewHolder pos", String.valueOf(position));

                final Bazaars.Bazaar searchCategory = filteredList.get(position);

                SearchedCategoryViewHolder searchedCategoryViewHolder = (SearchedCategoryViewHolder) holder;

                String formatStartDate = formattedDate.format(searchCategory.getBazaarStartDate());
                String formatEndDate = formattedDate.format(searchCategory.getBazaarEndDate());
                String formatSTime = searchCategory.getBazaarStartTime();
                String formatETime = searchCategory.getBazaarEndTime();

//                searchedCategoryViewHolder.ivLogoBazaar;
                searchedCategoryViewHolder.tvNamaBazaar.setText(searchCategory.getBazaarName());
                searchedCategoryViewHolder.tvAlamatBazaar.setText(searchCategory.getBazaarLocation());
                searchedCategoryViewHolder.tvTanggalBazaar.setText(formatStartDate + " - " + formatEndDate);
                searchedCategoryViewHolder.tvWaktuBazaar.setText(formatSTime + " - " + formatETime);
                searchedCategoryViewHolder.tvPriceBazaar.setText(String.format("D-%d", searchCategory.getBazaarPrice()));
                searchedCategoryViewHolder.tvStatusBazaar.setText(String.valueOf(searchCategory.getBazaarStatusId()));

                Glide.with(context)
                        .load(ApiConfig.FINAL_URL + "poster/" + searchCategory.getBazaarPoster())
                        //.signature(new ObjectKey(String.valueOf(UserData.picCount)))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("GLIDE ERROR", "ERROR FETCHING POSTER");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //progress_bar_img.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(searchedCategoryViewHolder.ivLogoBazaar);

                searchedCategoryViewHolder.cvBazaar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent detailIntent = new Intent(context, DetailActivity.class);
                        int bazaarId = searchCategory.getBazaarId();
                        detailIntent.putExtra("bazaarId", bazaarId);
                        context.startActivity(detailIntent);
//                        Snackbar.make(v, "Bazaar Name: " + searchCategory.getBazaarName() + ", Bazaar Id: " + searchCategory.getBazaarId(), Snackbar.LENGTH_LONG).show(); // Bagian JASON
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(bazaarList != null && tag == "HOME"){
            return bazaarList.size();
        }
        else if(filteredList != null && tag == "SEARCHCATEGORY"){
            return filteredList.size();
        }
        else{
            return 0;
        }
    }

    public void removeBazaar(int posisi){
        bazaarList.remove(posisi);
        notifyDataSetChanged();
    }

    public void addBazaar(Bazaars.Bazaar bazaar){
        bazaarList.add(bazaar);
        notifyDataSetChanged();
    }

    public void updateBazaar(int posisi, Bazaars.Bazaar bazaar){
        bazaarList.set(posisi, bazaar);
        notifyDataSetChanged();
        notifyItemChanged(posisi);
    }

    public void setBazaarList(List<Bazaars.Bazaar> bazaar){
        bazaarList = bazaar;
        filteredList = bazaar;
//        if(bazaarList!=null){
//            for(Bazaars.Bazaar item:bazaarList){
//                Log.d("BAZAAR ID", item.getBazaarId());
//                Log.d("BAZAAR NAME",item.getBazaarName());
//            }
//        }
        notifyDataSetChanged();
    }

    public void searchedBazaarList(String searchedText){
        searchedText = searchedText.toLowerCase(Locale.getDefault());
        filteredList = new ArrayList<>();
        if(filteredList != null){
            Log.d("searchedCategory EMPTY?", String.valueOf(filteredList.isEmpty()));
            Log.d("bazaarList EMPTY?", String.valueOf(bazaarList.isEmpty()));
            for (Bazaars.Bazaar item : bazaarList){
                Log.d("SEMUA ITEM", item.getBazaarName());
                if(item.getBazaarName().toLowerCase(Locale.getDefault()).contains(searchedText)){
                    filteredList.add(item);
                    Log.d("ITEM YG SAMA: ", item.getBazaarName());
                }
            }
        }
        notifyDataSetChanged();


        Log.d("searchedCategory size", String.valueOf(filteredList.size()));
        Log.d("bazaarList size", String.valueOf(bazaarList.size()));
    }

    public void categoryBazaarList(String selectedCategory){
        filteredList = new ArrayList<>();

        Log.d("selectedCategory: ", selectedCategory);
        if(filteredList != null){
            Log.d("IF", "filteredList != null");
            for (Bazaars.Bazaar item : bazaarList){
                Log.d("Bazaar Name: ", item.getBazaarName());
                if(item.getBazaarCategoryName().equals(selectedCategory)){
                    filteredList.add(item);
                    Log.d("Same Bazaar Name: ", item.getBazaarName());
                }
            }
        }

        notifyDataSetChanged();
        Log.d("Category size", String.valueOf(filteredList.size()));
        Log.d("bazaarList size", String.valueOf(bazaarList.size()));
    }

    public Bazaars.Bazaar getBazaarPosition(int posisi){
        return bazaarList.get(posisi);
    }

    public class BazaarViewHolder extends RecyclerView.ViewHolder{
        private final ImageView ivLogoBazaar;
        private final TextView tvNamaBazaar;
        private final CardView cvBazaar;

        public BazaarViewHolder(View itemView) {
            super(itemView);
            ivLogoBazaar = itemView.findViewById(R.id.ivLogoBazaar);
            tvNamaBazaar = itemView.findViewById(R.id.tvNamaBazaar);
            cvBazaar = itemView.findViewById(R.id.cvBazaar);
        }
    }

    public class SearchedCategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogoBazaar;
        private final TextView tvNamaBazaar;
        private final TextView tvAlamatBazaar;
        private final TextView tvTanggalBazaar;
        private final TextView tvWaktuBazaar;
        private final TextView tvPriceBazaar;
        private final TextView tvStatusBazaar;
        private final CardView cvBazaar;

        public SearchedCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivLogoBazaar = itemView.findViewById(R.id.ivLogoBazaar);
            this.tvNamaBazaar = itemView.findViewById(R.id.tvNamaBazaar);
            this.tvAlamatBazaar = itemView.findViewById(R.id.tvAlamatBazaar);
            this.tvTanggalBazaar = itemView.findViewById(R.id.tvTanggalBazaar);
            this.tvWaktuBazaar = itemView.findViewById(R.id.tvWaktuBazaar);
            this.tvPriceBazaar = itemView.findViewById(R.id.tvPriceBazaar);
            this.tvStatusBazaar = itemView.findViewById(R.id.tvStatusBazaar);
            cvBazaar = itemView.findViewById(R.id.cvBazaar);
        }
    }
}
