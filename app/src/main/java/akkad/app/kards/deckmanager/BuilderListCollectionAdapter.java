package akkad.app.kards.deckmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import static android.view.animation.AnimationUtils.loadAnimation;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static android.content.ContentValues.TAG;

public class BuilderListCollectionAdapter extends RecyclerView.Adapter<BuilderListCollectionAdapter.ViewHolder>{

    private LinkedHashMap<Card, Integer> mCards;
    BuilderCollectionFragment fragment;
    private Context mContext;
    Animation slideOut;
    Animation slideIn;

    public BuilderListCollectionAdapter(LinkedHashMap<Card, Integer> mCards, Context mContext, BuilderCollectionFragment fragment) {
        this.mCards = mCards;
        this.mContext = mContext;
        this.fragment = fragment;

        slideOut = loadAnimation(mContext, R.anim.slide_out);
        slideIn = loadAnimation(mContext, R.anim.slide_in);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewer_list_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Card c = (new ArrayList<>(mCards.keySet())).get(position);
        int cardNum = (new ArrayList<>(mCards.values())).get(position);
        
        String opCostDraw, attackDraw, defenseDraw;
        opCostDraw = Integer.toString(c.getOpCost());
        attackDraw = Integer.toString(c.getAttack());
        defenseDraw = Integer.toString(c.getDefense());

        // Bind data
        if (c.getSpecial().trim().equals("")) {
            holder.cardName2.setText(c.getName());
            holder.cardName.setVisibility(View.INVISIBLE);
            holder.cardName2.setVisibility(View.VISIBLE);
        }
        else {
            holder.cardName.setText(c.getName());
            holder.cardName2.setVisibility(View.INVISIBLE);
            holder.cardName.setVisibility(View.VISIBLE);
        }

        holder.flavorImage.setImageResource(c.getImage());
        holder.kreditsImage.setImageResource(c.getKredits());
        holder.specialText.setText(c.getSpecial());
        holder.typeImage.setImageResource(c.getType());
        holder.attackImage.setImageResource(c.getAttackImage());
        holder.defenseImage.setImageResource(R.drawable.defense);
        holder.attack.setText(attackDraw);
        holder.defense.setText(defenseDraw);

        if ((c.getTypeName().equals("order")) || c.getTypeName().equals("countermeasure")) {
            holder.attackImage.setVisibility(View.INVISIBLE);
            holder.attack.setVisibility(View.INVISIBLE);
            holder.defenseImage.setVisibility(View.INVISIBLE);
            holder.defense.setVisibility(View.INVISIBLE);
            holder.opCost.setVisibility(View.INVISIBLE);
            holder.opCostSmall.setVisibility(View.INVISIBLE);
        } else {
            holder.attackImage.setVisibility(View.VISIBLE);
            holder.attack.setVisibility(View.VISIBLE);
            holder.defenseImage.setVisibility(View.VISIBLE);
            holder.defense.setVisibility(View.VISIBLE);
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
        }
        
        holder.countryStrip.setBackgroundColor(Color.parseColor(c.getCountry()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: SHORT clicked on " + c.getName());

                if (((DeckBuilderActivity)mContext).addCardToDeck(c.getName())) {
                    mCards.put(c, (mCards.get(c)+1));
                    int cardNum = (new ArrayList<>(mCards.values())).get(position);
                    holder.cardCount.setText(Integer.toString(cardNum));
                }
            }
        });
        if (c.getPassive()) {
            holder.passiveImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.passiveImage.setVisibility(View.INVISIBLE);
        }

        holder.cardCount.setText(Integer.toString(cardNum));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: Long-clicked on " + c.getName());
                Intent intent_card = new Intent(mContext, ShowCard.class);
                intent_card.putExtra("card_image", c.getImage());
                mContext.startActivity(intent_card);
                return true;
            }
        });
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
        TextView cardCount;
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
            countryStrip = itemView.findViewById(R.id.country_strip);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            passiveImage = itemView.findViewById(R.id.passive_image);
            cardCount = itemView.findViewById(R.id.card_count);
            opCostSmall = itemView.findViewById(R.id.opcost_text_small);
        }
    }

    public void swapDataSet(LinkedHashMap<Card, Integer> newData) {
        this.mCards = newData;
        notifyDataSetChanged();
    }
}
