package akkad.app.kards.deckmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class DeckManagerAdapter extends RecyclerView.Adapter<DeckManagerAdapter.ViewHolder>{

    private static final int EDIT_DECK_CODE = 1;
    private static final int VIEW_DECK_CODE = 3;
    private static final int EDIT_FROM_MANAGER_CODE = 4;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EXPORT_CODE_ONLY = "exportCodeOnly";

    private ArrayList<Deck> mDecks;
    private Context mContext;
    private DeckUtils utils;

    public DeckManagerAdapter(ArrayList<Deck> mDecks, Context mContext) {
        this.mDecks = mDecks;
        this.mContext = mContext;
        utils = new DeckUtils(mContext);
    }

    @NonNull
    @Override
    public DeckManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_deckitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Current deck
        Deck d = mDecks.get(position);
        holder.backImage.setImageResource(d.getBackImage());
        holder.deckName.setText(d.getDeckName());
        holder.allyImage.setImageResource(d.getAllyImage());
        if (!d.isValid()) {
            holder.validTitle.setVisibility(View.VISIBLE);
            holder.validBg.setVisibility(View.VISIBLE);
        } else {
            holder.validTitle.setVisibility(View.INVISIBLE);
            holder.validBg.setVisibility(View.INVISIBLE);
        }
        holder.deckParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on " + mDecks.get(position).getDeckName());
                Intent deckViewer = new Intent(mContext, DeckViewerActivity.class);
                deckViewer.putExtra("deckName", mDecks.get(position).getDeckName());
                ((DeckManagerActivity)mContext).startActivityForResult(deckViewer, VIEW_DECK_CODE);
            }
        });
        holder.deckParentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backImage;
        TextView deckName;
        CardView deckParentLayout;
        ImageView allyImage;
        ImageView validBg;
        TextView validTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backImage = itemView.findViewById(R.id.deck_back_image);
            deckName = itemView.findViewById(R.id.deck_name);
            allyImage = itemView.findViewById(R.id.ally_image);
            deckParentLayout = itemView.findViewById(R.id.deck_parent_layout);
            validBg = itemView.findViewById(R.id.invalid_bg);
            validTitle = itemView.findViewById(R.id.invalid_title);
        }
    }

    public void swapDataSet(ArrayList<Deck> newData) {
        this.mDecks = newData;
        notifyDataSetChanged();
    }

    private void showPopupMenu(View v, int position) {
        PopupMenu pMenu = new PopupMenu(v.getContext(), v);
        pMenu.inflate(R.menu.manager_context_menu);
        pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_view_context:
                        Intent deckViewer = new Intent(mContext, DeckViewerActivity.class);
                        deckViewer.putExtra("deckName", mDecks.get(position).getDeckName());
                        ((DeckManagerActivity)mContext).startActivityForResult(deckViewer, VIEW_DECK_CODE);
                        break;

                    case R.id.action_edit_context:
                        Intent editDeckIntent = new Intent(mContext, DeckBuilderActivity.class);
                        editDeckIntent.putExtra("deckName", mDecks.get(position).getDeckName());
                        editDeckIntent.putExtra("requestCode", EDIT_FROM_MANAGER_CODE);
                        ((DeckManagerActivity)mContext).startActivityForResult(editDeckIntent, EDIT_FROM_MANAGER_CODE);
                        break;

                    case  R.id.action_delete_context:
                        showDeleteDialog(position);
                        break;

                    case R.id.action_description_context:
                        Deck deck = mDecks.get(position);
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        boolean codeOnly = sharedPreferences.getBoolean(EXPORT_CODE_ONLY, false);
                        Log.d(TAG, "onMenuItemClick: codeOnly is " + codeOnly);

                        if (codeOnly) {
                            Toast.makeText(mContext, R.string.import_code_copied, Toast.LENGTH_SHORT).show();
                            String code = utils.getImportCode(deck.getDeckName());
                            ClipboardManager clipboard2 = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip2 = ClipData.newPlainText(null, code);
                            clipboard2.setPrimaryClip(clip2);
                        } else {
                            Toast.makeText(mContext, R.string.description_toast, Toast.LENGTH_SHORT).show();
                            String description = utils.getDeckDescription(deck.getDeckName(), deck.getCountry(), deck.getAlly(), deck.getCardsInDeck());
                            ClipboardManager clipboard2 = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip2 = ClipData.newPlainText(null, description);
                            clipboard2.setPrimaryClip(clip2);
                        }
                }
                return true;
            }
        });
        pMenu.show();
    }

    private void showDeleteDialog(int position) {
        final AlertDialog.Builder d = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                .setTitle(mContext.getString(R.string.delete_title))
                .setMessage(mContext.getString(R.string.delete_confirmation) + " " + mDecks.get(position).getDeckName() + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(mContext.getString(R.string.cancel), null)
                .setPositiveButton(mContext.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeckUtils utils = new DeckUtils(mContext);
                        utils.deleteDeckByName(mDecks.get(position).getDeckName());
                        mDecks.remove(position);
                        ((DeckManagerActivity)mContext).setDecks(mDecks);
                        notifyDataSetChanged();
                    }});
                d.show();
    }
}
