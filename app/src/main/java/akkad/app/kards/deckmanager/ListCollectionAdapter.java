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

public class ListCollectionAdapter extends RecyclerView.Adapter<ListCollectionAdapter.ViewHolder> implements Filterable {

    private ArrayList<Card> mCards;
    private ArrayList<Card> mCardsFull;
    private Context mContext;

    public ListCollectionAdapter(ArrayList<Card> mCards, Context mContext) {
        this.mCards = mCards;
        this.mContext = mContext;
        this.mCardsFull = new ArrayList<>(mCards);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //Current card
        Card c = mCards.get(position);

        // Doesn't print opcost if card is an order
        String opCostDraw, attackDraw, defenseDraw;
        if(c.getType() == R.drawable.order || c.getType() == R.drawable.cm){
            opCostDraw = " ";
            attackDraw = "-";
            defenseDraw = "-";
        }
        else {
            opCostDraw = Integer.toString(c.getOpCost());
            attackDraw = Integer.toString(c.getAttack());
            defenseDraw = Integer.toString(c.getDefense());
        }

        // Bind data
        if (c.getSpecial().trim().equals("")) {
            holder.cardName2.setText(c.getName());
            holder.cardName.setVisibility(View.INVISIBLE);
            holder.cardName2.setVisibility(View.VISIBLE);
        }
        else {
            holder.cardName.setText(c.getName());
            holder.cardName.setVisibility(View.VISIBLE);
            holder.cardName2.setVisibility(View.INVISIBLE);
        }
        holder.flavorImage.setImageResource(c.getImage());
        holder.kreditsImage.setImageResource(c.getKredits());
        holder.specialText.setText(c.getSpecial());
        holder.typeImage.setImageResource(c.getType());
        holder.attackImage.setImageResource(c.getAttackImage());
        holder.defenseImage.setImageResource(R.drawable.defense);
        holder.attack.setText(attackDraw);
        holder.defense.setText(defenseDraw);

        if (c.getKreditsNumber() >= 10) {
            holder.opCost.setVisibility(View.INVISIBLE);
            holder.opCostSmall.setVisibility(View.VISIBLE);
            holder.opCostSmall.setText(opCostDraw);
        }
        else {
            holder.opCost.setVisibility(View.VISIBLE);
            holder.opCostSmall.setVisibility(View.INVISIBLE);
            holder.opCost.setText(opCostDraw);
        }
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
        if (c.getPassive()) {
            holder.passiveImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.passiveImage.setVisibility(View.INVISIBLE);
        }
        if ((c.getType() == R.drawable.order) || c.getType() == R.drawable.cm) {
            holder.attackImage.setVisibility(View.INVISIBLE);
            holder.attack.setVisibility(View.INVISIBLE);
            holder.defenseImage.setVisibility(View.INVISIBLE);
            holder.defense.setVisibility(View.INVISIBLE);
        }
        else {
            holder.attackImage.setVisibility(View.VISIBLE);
            holder.attack.setVisibility(View.VISIBLE);
            holder.defenseImage.setVisibility(View.VISIBLE);
            holder.defense.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardName;
        TextView cardName2;
        TextView specialText;
        ImageView flavorImage;
        ImageView kreditsImage;
        ImageView typeImage;
        ImageView attackImage;
        ImageView defenseImage;
        TextView defense;
        TextView attack;
        TextView opCost;
        ImageView countryStrip;
        CardView parentLayout;
        ImageView passiveImage;
        TextView opCostSmall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardname_collection);
            cardName2 = itemView.findViewById(R.id.cardname_collection_2);
            specialText = itemView.findViewById(R.id.special_text);
            flavorImage = itemView.findViewById(R.id.flavor_image);
            kreditsImage = itemView.findViewById(R.id.kredits_image);
            typeImage = itemView.findViewById(R.id.type_image);
            attackImage = itemView.findViewById(R.id.attack_image);
            defenseImage = itemView.findViewById(R.id.defense_image);
            attack = itemView.findViewById(R.id.attack_value);
            defense = itemView.findViewById(R.id.defense_value);
            opCost = itemView.findViewById(R.id.opcost_text);
            opCostSmall = itemView.findViewById(R.id.opcost_text_small);
            countryStrip = itemView.findViewById(R.id.country_strip);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            passiveImage = itemView.findViewById(R.id.passive_image);
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
