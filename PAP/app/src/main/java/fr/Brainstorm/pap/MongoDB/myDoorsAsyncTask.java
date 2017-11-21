package fr.Brainstorm.pap.MongoDB;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by thoma on 15/03/2017.
 * Project : Porte à Porte pour Brainstorm
 */

public abstract class myDoorsAsyncTask extends AsyncTask<String, Void, Pair<ArrayList<Porte>, Boolean>> implements InterfaceReceivedData<Pair<ArrayList<Porte>, Boolean>> {

    public OkHttpClient client;

    @Override
    protected final Pair<ArrayList<Porte>, Boolean> doInBackground(String... arg0) {

        String args = arg0[0];
        QueryBuilder qb = new QueryBuilder();
        client = new OkHttpClient();
        Pair<String, Boolean> response = Pair.create("",false);
        try {
            String URL = qb.buildGetMyPorteURL("portes", args );
            System.out.println("inputed URL : "+ URL);
            response = getFromDB(URL);
            System.out.println("and response ("+ response.second+ ") : "+ response.first);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        Pair<ArrayList<Porte>, Boolean> result = Pair.create(new ArrayList<Porte>(),response.second);
        if(response.second) {
            System.out.println("portes récupérées : "+ response.first);
            String updatedJson = "{\"data\" : " + response.first + "}";

            DataWrapperPortes dataWrapper = DataWrapperPortes.fromJson(updatedJson);
            for (DataWrapperPortes.BigPorte big : dataWrapper.data) {
                result.first.add(new Porte(big));
            }

        }

        return result;

    }

    private Pair<String, Boolean> getFromDB(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Pair.create(response.body().string(),response.isSuccessful());
        }
        catch (Exception e) {
            return Pair.create("",false);
        }
    }

    @Override
    protected void onPostExecute(Pair<ArrayList<Porte>, Boolean> pair) {
        onResponseReceived(pair);
    }
}

