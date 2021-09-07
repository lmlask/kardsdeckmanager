package akkad.app.kards.deckmanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GridCollectionAdapter extends RecyclerView.Adapter<GridCollectionAdapter.ViewHolder> implements Filterable {

    private ArrayList<Card> mCards;
    private ArrayList<Card> mCardsFull;
    private Context mContext;

    public GridCollectionAdapter(ArrayList<Card> mCards, Context mContext) {
        this.mCards = mCards;
        this.mContext = mContext;
        this.mCardsFull = new ArrayList<>(mCards);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_collectionitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Current card
        Card c = mCards.get(position);

        holder.gridFlavorImage.setImageResource(c.getImage());

        holder.gridParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on " + mCards.get(position));
                Intent intent_card = new Intent(mContext, ShowCard.class);
                intent_card.putExtra("card_image", mCards.get(position).getImage());
                mContext.startActivity(intent_card);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView gridFlavorImage;
        CardView gridParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridFlavorImage = itemView.findViewById(R.id.grid_flavor_image);
            gridParentLayout = itemView.findViewById(R.id.gridParentLayout);
        }
    }

    public void swapDataSet(ArrayList<Card> newData) {
        this.mCards = newData;
        this.mCardsFull = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return cardFilter;
    }

    private Filter cardFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Card> filteredCards = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredCards.addAll(mCardsFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Card card : mCardsFull) {
                    if (card.getName().toLowerCase().contains(filterPattern)) {
                        filteredCards.add(card);
                    }
                    else if (card.getSpecial().toLowerCase().contains(filterPattern)) {
                        filteredCards.add(card);
                    }
                    else if (card.getRarity().toLowerCase().contains(filterPattern)) {
                        filteredCards.add(card);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredCards;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCards.clear();
            mCards.addAll((ArrayList<Card>) results.values);
            notifyDataSetChanged();
        }
    };
}
