//package id.ac.umn.tenalist.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//import java.util.Map;
//
//import id.ac.umn.tenalist.R;
//import id.ac.umn.tenalist.activity.DetailActivity;
//import id.ac.umn.tenalist.model.BazaarDetail;
//import id.ac.umn.tenalist.model.SharedPreference;
//import id.ac.umn.tenalist.model.User;
//
//public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
//
//    private Context mContext;
//    private List<BazaarDetail.bazaarDetail> bazaars;
//    SharedPreference sharedPreference;
//
//    public FavoriteAdapter(Context mContext, List<BazaarDetail.bazaarDetail> bazaars) {
//        this.bazaars = bazaars;
//        this.mContext = mContext;
//        sharedPreference = new SharedPreference();
//        for (int i=0; i<bazaars.size();i++){
//            Log.d("DEBUG", bazaars.get(i).getBazaarName());
//        }
//    }
//
//    @NonNull
//    @Override
//    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false);
//        return new FavoriteAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        final BazaarDetail.bazaarDetail bazaar = bazaars.get(position);
////        Log.d("MASUK", bazaar.getBazaarName());
//        holder.tv_bazaar_name.setText(String.valueOf(bazaar.getBazaarName()));
//        holder.tv_bazaar_price.setText(String.valueOf(bazaar.getBazaarPrice()));
//        holder.tv_bazaar_desc.setText(String.valueOf(bazaar.getBazaarDescription()));
//        if (checkFavoriteItem(bazaar)) {
//            holder.btn_favorite.setImageResource(R.drawable.heart_red);
//            holder.btn_favorite.setTag("red");
//        } else {
//            holder.btn_favorite.setImageResource(R.drawable.heart_grey);
//            holder.btn_favorite.setTag("grey");
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent detailIntent = new Intent(mContext, DetailActivity.class);
//                int bazaarId = bazaar.getBazaarId();
//                detailIntent.putExtra("bazaarId", bazaarId);
//                mContext.startActivity(detailIntent);
//            }
//        });
//
//        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Log.d("MASUK2", bazaar.getBazaarName());
//                sharedPreference.removeFavorite(mContext, bazaar);
//                bazaars.remove(bazaar);
//                notifyDataSetChanged();
//
//                holder.btn_favorite.setImageResource(R.drawable.heart_grey);
//                holder.btn_favorite.setTag("grey");
//                Toast.makeText(mContext,
//                        "Remove from Favorites",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return bazaars.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView tv_bazaar_name;
//        public TextView tv_bazaar_price;
//        public TextView tv_bazaar_desc;
//        public ImageView btn_favorite;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tv_bazaar_name = itemView.findViewById(R.id.tv_bazaar_name);
//            tv_bazaar_price = itemView.findViewById(R.id.tv_bazaar_price);
//            tv_bazaar_desc = itemView.findViewById(R.id.tv_bazaar_desc);
//            btn_favorite = itemView.findViewById(R.id.btn_favorite);
//        }
//    }
//
//    public boolean checkFavoriteItem(BazaarDetail.bazaarDetail bazaarDetail) {
//        boolean check = false;
//        sharedPreference = new SharedPreference();
//        List<BazaarDetail.bazaarDetail> favorites = sharedPreference.getFavorites(mContext);
//        if (favorites != null) {
//            for (BazaarDetail.bazaarDetail bazaar : favorites) {
//                if (bazaar.getBazaarName().equals(bazaarDetail.getBazaarName())) {
//                    check = true;
//                    break;
//                }
//            }
//        }
//        return check;
//    }
//}
