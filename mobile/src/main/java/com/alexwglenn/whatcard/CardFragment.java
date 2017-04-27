package com.alexwglenn.whatcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.util.DatabaseManager;
import com.alexwglenn.whatcard.viewholders.CardViewHolder;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CardFragment extends Fragment {

    static final String TAG = "CardFragment";

    @Inject
    DatabaseManager mDatabaseManager;

    /**
     * The fragment's ListView/GridView.
     */
    @InjectView(R.id.cardList)
    public RecyclerView mRecyclerView;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
//    private CardAdapter mAdapter;
    private FirebaseIndexRecyclerAdapter<Card, CardViewHolder> mAdapter;

    public static CardFragment newInstance() {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

//        updateCards();

        ((WhatCard) getActivity().getApplication()).getComponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_grid, container, false);

        ButterKnife.inject(this, view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

//        ArrayList<Cards> cards =  mDatabaseManager.getUserCards();
//        mAdapter = new CardAdapter(cards, getActivity());

        mAdapter = new FirebaseIndexRecyclerAdapter<Card, CardViewHolder>(Card.class, R.layout.card_grid_item, CardViewHolder.class, mDatabaseManager.getUserCardsRef(), mDatabaseManager.getAllCardsRef()) {
            @Override
            protected void populateViewHolder(CardViewHolder viewHolder, Card model, int position) {
                viewHolder.setCard(model);
            }
        };

        // Set the adapter
        mRecyclerView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        mRecyclerView.setOnItemClickListener(this);
//        mRecyclerView.setOnItemLongClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible()) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getChildFragmentManager().findFragmentByTag(AddCardFragment.class.getSimpleName());
                    if (prev != null) {


                        ft.remove(prev).commit();
                        ft = getChildFragmentManager().beginTransaction();
                    }
                    ft.addToBackStack(null);

                    AddCardFragment addCardDialog = AddCardFragment.newInstance();
                    addCardDialog.show(getChildFragmentManager(), AddCardFragment.class.getSimpleName());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recreate the adapter
        mAdapter = new FirebaseIndexRecyclerAdapter<Card, CardViewHolder>(Card.class, R.layout.card_grid_item, CardViewHolder.class, mDatabaseManager.getUserCardsRef(), mDatabaseManager.getAllCardsRef()) {
            @Override
            protected void populateViewHolder(CardViewHolder viewHolder, Card model, int position) {
                viewHolder.setCard(model);
            }
        };

        // Set the adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    //    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "Pressed Card " + position, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                   final int position, long id) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Card card = mAdapter.getItem(position);
//                mDatabaseManager.deleteUserCard(card);
//
//                //TODO: Remove on server
//
//                dialog.dismiss();
//            }
//        });
//
//        builder.create().show();
//
//        return true;
//    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
//
//    public void setEmptyText(CharSequence emptyText) {
//        View emptyView = mRecyclerView.
//
//        if (emptyView instanceof TextView) {
//            ((TextView) emptyView).setText(emptyText);
//        }
//    }

//    @Produce
//    public CardsDeletedEvent produceCardDeletedEvent() {
//        return new CardsDeletedEvent();
//    }

//    @Subscribe public void onCardsUpdated(CardsUpdatedEvent event) {
//        updateCards();
//    }
//
//    @Subscribe public void onCardsDeleted(CardsDeletedEvent event) {
//        updateCards();
//    }
//
//    @Subscribe public void onCardsAdded(CardsAddedEvent event) {
//        updateCards();
//    }

//    private void updateCards() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
////        String cardsJson = preferences.getString("Cards", "[{\"name\":\"Chase Freedom\",\"basePercentage\":0.01,\"categoryRates\":[{\"Grocery Stores\":0.05},{\"Movie Theatres\":0.05}]},{\"name\":\"Capital One Quicksilver\",\"basePercentage\":0.015, \"categoryRates\":[]}]");
//        String cardsJson = preferences.getString("Cards", "");
//
//        Gson gson = new Gson();
//
//        Type listType = new TypeToken<ArrayList<Card>>() {}.getType();
//        cards = gson.fromJson(cardsJson, listType);
//        if (cards == null) {
//            cards = new ArrayList<Card>();
//        }
//
//        if (mAdapter == null) {
//            mAdapter = new CardAdapter(cards, getActivity());
//        } else {
//            mAdapter.setCards(cards);
//            mAdapter.notifyDataSetChanged();
//        }
//    }

}
