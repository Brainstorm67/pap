package fr.jlm2017.pap;

/**
 * Created by thoma on 15/02/2017.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fr.jlm2017.pap.MongoDB.DataObject;
import fr.jlm2017.pap.MongoDB.DeleteAsyncTask;
import fr.jlm2017.pap.MongoDB.GetAllAsyncTask;

public class supprTab extends Fragment{

    ArrayList<Militant> listItems = new ArrayList<>();
    ArrayList<String> listItemsString = new ArrayList<>();
    private Militant user;
    ArrayAdapter<String> adapter;
    public int nbusers;
    ListView layout;
    TextView nbView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = ((AdminActivity) getActivity()).user;
        nbusers=0;
        View rootView = inflater.inflate(R.layout.tab_admin_2, container, false);
        layout = (ListView) rootView.findViewById(R.id.militantsList);
        nbView = (TextView) rootView.findViewById(R.id.nbUsers);
        adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_selectable_list_item,listItemsString);
        layout.setAdapter(adapter);
        refreshList();

        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, final int pos, long id) {
                // pop up de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final String name = listItemsString.get(pos);
                builder.setMessage("Voulez-vous vraiment supprimer :\n" + name + " ?" )
                        .setTitle(R.string.dialog_title);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        String message =("Vous avez supprimé :"+name);
                        DeleteAsyncTask tsk = new DeleteAsyncTask();
                        try {
                            if(tsk.execute(listItems.get(pos)).get()){
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                deleteMilitant(pos);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog box = builder.create();
                box.show();
            }
        });

        Button refreshButton = (Button) rootView.findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshList();
                showLongToast("Liste à jour");
            }
        });

        return rootView;
    }

    // fonction pour rafraichir la liste (appelée à la création et si on appuie sur "Refresh")
    public void refreshList() {
        listItems.clear();
        listItemsString.clear();
        nbusers = 0;
        adapter.notifyDataSetInvalidated();
        GetAllAsyncTask tsk = new GetAllAsyncTask();
        Pair<ArrayList<DataObject>, Boolean> result = null;
        try {
            result = tsk.execute("militants").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());

        for(DataObject x: result.first){

            Militant m = (Militant) x;
            addMilitant(m);
        }


    }

    // fonctions de modification des listes affichées et du chiffre

    private void addMilitant(Militant m) {
        nbusers++;
        nbView.setText(String.valueOf(nbusers));
        if(m.id_.equals(user.id_)) return;  // on ne peut pas se voir soit meme dans la liste
        listItems.add(m);
        listItemsString.add(m.pseudo + " | " + m.email);
        adapter.notifyDataSetChanged();
    }

    private void deleteMilitant(int pos) {
        listItems.remove(pos);
        listItemsString.remove(pos);
        nbusers--;
        nbView.setText(String.valueOf(nbusers));
        adapter.notifyDataSetChanged();
    }


    //Toasts Miam

    public void showLongToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    // reception du Broadcaster si on vient d'ajouter un militant dans l'autre Fragment

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if ("DATA_ACTION".equals(intent.getAction()) == true)
            {
                Militant mili  = intent.getParcelableExtra("DATA_EXTRA");
                System.out.println("ID caché : "+mili.id_);
                addMilitant(mili);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("DATA_ACTION"));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }
}
