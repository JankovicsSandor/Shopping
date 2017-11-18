package sanyi.shop.com.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {
    private List<ShopItem> Items;
    private final ItemAdapterOnClickHandler mClickHandler;

    public ItemAdapter(ItemAdapterOnClickHandler clickHandler) {
        mClickHandler=clickHandler;
    }

    // Called when creating new viewholders
    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View view=inflater.inflate(R.layout.shop_list_item,parent,false);
        return new ItemAdapterViewHolder(view);
    }

    //Called when creating the views
    @Override
    public void onBindViewHolder(ItemAdapterViewHolder holder, int position) {
        //TODO implement proper BindView with all data
        String nameOfItem=Items.get(position).getName();
        String detailOfItem=Items.get(position).getDetail();
        String picturePath = Items.get(position).getPicture();

        int price=Items.get(position).getPrice();

        holder.mItemTextView.setText(nameOfItem);
        holder.mDetailTextView.setText(detailOfItem);
        holder.mPriceTextView.setText(String.valueOf(price));
        // Setting up the picasso library to download the picture
        Context context=holder.mProfPicture.getContext();
        if(context!=null){
            Picasso.with(context).load(picturePath).into(holder.mProfPicture);
        }
    }

    @Override
    public int getItemCount() {
        if(null==Items) return 0;
        return Items.size();
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mItemTextView;
        private TextView mDetailTextView;
        private TextView mPriceTextView;
        private ImageView mProfPicture;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            //TODO change the item for a proper outline
            mItemTextView=(TextView)itemView.findViewById(R.id.tv_weather_data);
            mDetailTextView=(TextView)itemView.findViewById(R.id.detailTextViewInShop);
            mPriceTextView=(TextView)itemView.findViewById(R.id.priceShop);
            mProfPicture=(ImageView)itemView.findViewById(R.id.itemProfPicture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO add proper onclick event handler;
           mClickHandler.onClick(view);
        }
    }
    public void setItemData(List<ShopItem> itemData){
        Items=itemData;
        notifyDataSetChanged();
    }

}
