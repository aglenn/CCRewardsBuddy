package com.alexwglenn.whatcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CardsAddedEvent;
import com.alexwglenn.whatcard.model.CardsDeletedEvent;
import com.alexwglenn.whatcard.model.CardsResponse;
import com.alexwglenn.whatcard.model.CardsUpdatedEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static final String TAG = "CardFragment";

    @Inject
    ThisCardService thisCardService;

    /**
     * The fragment's ListView/GridView.
     */
    @InjectView(R.id.cardList)
    public AbsListView mListView;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CardAdapter mAdapter;

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

        ((WhatCard)getActivity().getApplication()).getComponent().inject(this);

        thisCardService.getUserCards("1", "09ef2344-9667-4811-9b28-fc05089d48e6")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<Card[]>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Getting cards complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "An error getting cards " + e.getLocalizedMessage());
                        Log.d(TAG, "An error getting cards " + e.toString());
                    }

                    @Override
                    public void onNext(Response<Card[]> response) {
                        Log.d(TAG, "Got a response: " + response.code() + " " + response.body());
                        List<Card> cards = Arrays.asList(response.body());
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.copyToRealm(cards);
                        realm.commitTransaction();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.inject(this, view);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Card> cards = realm.where(Card.class).findAll();
        mAdapter = new CardAdapter(cards, getActivity());
        // Set the adapter
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible()) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getChildFragmentManager().findFragmentByTag(AddCardFragment.class.getSimpleName());
                    if(prev != null) {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Pressed Card " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Card card = mAdapter.getItem(position);
                card.deleteFromRealm();
                realm.commitTransaction();

                //TODO: Remove on server

                dialog.dismiss();
            }
        });

        builder.create().show();

        return true;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

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
