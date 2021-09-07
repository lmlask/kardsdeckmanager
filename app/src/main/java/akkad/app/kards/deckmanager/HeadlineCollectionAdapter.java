package akkad.app.kards.deckmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HeadlineCollectionAdapter extends RecyclerView.Adapter<HeadlineCollectionAdapter.ViewHolder> implements Filterable {

    private ArrayList<Card> mCards;
    private ArrayList<Card> mCardsFull;
    private Context mContext;

    public HeadlineCollectionAdapter(ArrayList<Card> mCards, Context mContext) {
        this.mCards = mCards;
        this.mCardsFull = new ArrayList<>(mCards);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Current card
        Card c = mCards.get(position);

        String opCostDraw, attackDraw, defenseDraw;
        opCostDraw = Integer.toString(c.getOpCost());
        attackDraw = Integer.toString(c.getAttack());
        defenseDraw = Integer.toString(c.getDefense());

        // Bind data
        holder.cardName.setText(c.getName());
        holder.flavorImage.setImageResource(c.getImage());
        holder.kreditsImage.setImageResource(c.getKredits());
        holder.attackImage.setImageResource(c.getAttackImage());
        holder.defenseImage.setImageResource(R.drawable.defense);

        if ((c.getTypeName().equals("order")) || c.getTypeName().equals("cm")) {
            holder.attackImage.setVisibility(View.INVISIBLE);
            holder.attack.setVisibility(View.INVISIBLE);
            holder.defenseImage.setVisibility(View.INVISIBLE);
            holder.defense.setVisibility(View.INVISIBLE);
            holder.opCost.setVisibility(View.INVISIBLE);
        } else {
            holder.attackImage.setVisibility(View.VISIBLE);
            holder.attack.setVisibility(View.VISIBLE);
            holder.defenseImage.setVisibility(View.VISIBLE);
            holder.defense.setVisibility(View.VISIBLE);
            holder.opCost.setVisibility(View.VISIBLE);
        }

        if (c.getKreditsNumber() >= 10 && !c.getTypeName().equals("order") && !c.getTypeName().equals("cm")) {
            holder.opCost.setVisibility(View.INVISIBLE);
            holder.opCostSmall.setVisibility(View.VISIBLE);
            holder.opCostSmall.setText(opCostDraw);
        }
        else if ((!c.getTypeName().equals("order")) && (!c.getTypeName().equals("cm"))) {
            holder.opCost.setVisibility(View.VISIBLE);
            holder.opCostSmall.setVisibility(View.INVISIBLE);
            holder.opCost.setText(opCostDraw);
        }
        holder.attack.setText(attackDraw);
        holder.defense.setText(defenseDraw);
        holder.countryStrip.setBackgroundColor(Color.parseColor(c.getCountry()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on " + mCards.get(position));
                //Toast.makeText(mContext, mCards.get(position).getName(), Toast.LENGTH_LONG).show();
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

        TextView cardName;
        ImageView flavorImage;
        ImageView kreditsImage;
        ImageView attackImage;
        ImageView defenseImage;
        TextView defense;
        TextView attack;
        TextView opCost;
        ImageView countryStrip;
        CardView parentLayout;
        TextView opCostSmall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.headline_cardname_collection);
            flavorImage = itemView.findViewById(R.id.headline_flavor_image);
            kreditsImage = itemView.findViewById(R.id.headline_kredits_image);
            attackImage = itemView.findViewById(R.id.headline_attack_image);
            defenseImage = itemView.findViewById(R.id.headline_defense_image);
            attack = itemView.findViewById(R.id.headline_attack_value);
            defense = itemView.findViewById(R.id.headline_defense_value);
            opCost = itemView.findViewById(R.id.headline_opcost_text);
            opCostSmall = itemView.findViewById(R.id.headline_opcost_text_small);
            countryStrip = itemView.findViewById(R.id.headline_country_strip);
            parentLayout = itemView.findViewById(R.id.headline_parent_layout);
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
