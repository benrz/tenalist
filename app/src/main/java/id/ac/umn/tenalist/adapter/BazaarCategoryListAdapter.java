package id.ac.umn.tenalist.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import id.ac.umn.tenalist.Interface.FragmentActionListener;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.model.BazaarsCategory;

public class BazaarCategoryListAdapter extends RecyclerView.Adapter<BazaarCategoryListAdapter.BazaarCategoryViewHolder> {
    private final LayoutInflater mInflater;
    private List<BazaarsCategory.BazaarCategory> bazaarCategoryList;
    FragmentActionListener fragmentActionListener;

    public BazaarCategoryListAdapter(FragmentActivity context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BazaarCategoryListAdapter.BazaarCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.category_item_layout, parent, false);
        return new BazaarCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BazaarCategoryListAdapter.BazaarCategoryViewHolder holder, int position) {
        if(bazaarCategoryList != null){
            final BazaarsCategory.BazaarCategory bazaarCategory = bazaarCategoryList.get(position);
    //            holder.ivLogoCategory.setImageResource(bazaarCategory.getBazaarPoster());
            holder.tvNamaCategory.setText(bazaarCategory.getBazaarCategoryName());

            holder.cvCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_CATEGORY_SELECTED);
                    bundle.putString(FragmentActionListener.KEY_CATEGORY_BAZAAR, bazaarCategory.getBazaarCategoryName());
                    fragmentActionListener.onActionPerformed(bundle);
//                    Snackbar.make(v, "BazaarCategory Name: "+bazaarCategory.getBazaarCategoryName()+", BazaarCategory Id: "+bazaarCategory.getBazaarCategoryId(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public int getItemCount() {
        if (bazaarCategoryList != null) {
            return bazaarCategoryList.size();
        } else {
            return 0;
        }
    }

    void removeBazaarCategory(int posisi) {
        bazaarCategoryList.remove(posisi);
        notifyDataSetChanged();
    }

    void addBazaar(BazaarsCategory.BazaarCategory bazaarCategory){
        bazaarCategoryList.add(bazaarCategory);
        notifyDataSetChanged();
    }

    void updateBazaar(int posisi, BazaarsCategory.BazaarCategory bazaarCategory){
        bazaarCategoryList.set(posisi, bazaarCategory);
        notifyDataSetChanged();
        notifyItemChanged(posisi);
    }

    public BazaarsCategory.BazaarCategory getBazaarCategoryPosition(int posisi){
        return bazaarCategoryList.get(posisi);
    }

    public void setBazaarCategoryList(List<BazaarsCategory.BazaarCategory> bazaarCategory){
        bazaarCategoryList = bazaarCategory;
        notifyDataSetChanged();
    }

    public class BazaarCategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogoCategory;
        private final TextView tvNamaCategory;
        private final CardView cvCategory;

        public BazaarCategoryViewHolder(View itemView) {
            super(itemView);
            ivLogoCategory = itemView.findViewById(R.id.ivLogoCategory);
            tvNamaCategory = itemView.findViewById(R.id.tvNamaCategory);
            cvCategory = itemView.findViewById(R.id.cvCategory);
        }
    }
}
