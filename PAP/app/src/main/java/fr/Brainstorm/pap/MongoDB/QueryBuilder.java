package fr.Brainstorm.pap.MongoDB;

import android.annotation.SuppressLint;
import android.util.Pair;

import java.util.ArrayList;

import fr.Brainstorm.pap.utils.Encoder;

/**
 * Created by thoma on 14/02/2017.
 */

public class QueryBuilder {

    /**
     * Specify your database name here
     *
     * @return String databaseName
     */
    private String getDatabaseName() {
        return "papbrain";
    }

    /**
     * Specify your MongoLab API here
     *
     * @return
     */
    private String getApiKey() {
        return Encoder.decodeString("CuvUeEnRQpYHlX88M2WTl7oYKuaS77qguAw4wS8Cqg2qt_2ip8sXzg==");
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     *
     * @return
     */
    private String getBaseUrl() {
        return "https://api.mlab.com/api/1/databases/" + getDatabaseName() + "/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     *
     * @return
     */
    private String docApiKeyUrl() {
        return "?apiKey=" + getApiKey();
    }

    /**
     * Builds a complete URL using the methods specified above
     *
     * @return
     */
    String buildObjectsSaveURL(DataObject objects) //objects = "militants" ou "portes"
    {
        String solution;
        if (objects.getClass() == Militant.class) {
            solution = "militants";
        } else {
            solution = "portes";
        }
        return buildObjectsSaveURL(solution);
    }

    private String buildObjectsSaveURL(String objects) //objects = "militants" ou "portes"
    {
        return getBaseUrl() + objects + docApiKeyUrl();
    }

    String builObjectGetAllURL(String objects) {
        return buildObjectsSaveURL(objects);
    }

    String builObjectGetFilteredURL(String objects, ArrayList<Pair<String, String>> ids) { // on donne une collection et des couples id, valeur pour filtrer
        String id;
        if (objects.equals("militants")) {
            id = "militant";
        } else {
            id = "porte";
        }
        String res = getBaseUrl() + objects + "?q={$and:[";
        for (int i = 0; i < ids.size(); i++) {
            res = res + "{\"" + id + "." + ids.get(i).first + "\":\"" + ids.get(i).second + "\"}";
            if (i < ids.size() - 1) res = res + ",";
        }
        res = res + "]}&apiKey=" + getApiKey();
        return res;
    }


    String buildObjectsUpdateURL(DataObject objects) //URL d'update
    {
        String solution;
        if (objects.getClass() == Militant.class) {
            solution = "militants";
        } else {
            solution = "portes";
        }
        return getBaseUrl() + solution + "/" + objects.id_ + docApiKeyUrl();
    }

    /**
     * Formats the contact details for MongoHQ Posting
     *
     * @param contact: Details of the person
     * @return
     */

    String createObject(DataObject contact) {
        if (contact.getClass() == Militant.class) {
            return createMilitant((Militant) contact);
        } else {
            return createPorte((Porte) contact);
        }
    }

    private String createMilitant(Militant contact) {
        return String
                .format("{\"militant\"  : {\"pseudo\": \"%s\", "
                                + " \"email\": \"%s\", "
                                + "\"password\": \"%s\",\"admin\": \"%b\" }}",
                        contact.pseudo, contact.email, contact.password, contact.admin);
    }

    @SuppressLint("DefaultLocale")
    private String createPorte(Porte contact) {
        String res = String
                .format("{\"adresseResume\": \"%s\", \"complement\": \"%s\",\"nom_rue\": \"%s\",\"nom_ville\": \"%s\",\"numA\": \"%s\",\"numS\": \"%s\", " +
                                "\"ouverte\": \"%b\", " +
                                "\"location\" : { " +
                                "\"type\" : \"Point\"," +
                                "\"coordinates\": [%f,%f]}, \"person\": \"%s\"}",
                        contact.adresseResume, contact.complement, contact.nom_rue, contact.nom_ville, contact.numA, contact.numS, contact.ouverte, contact.longitude, contact.latitude, contact.user_id);

        return res;
    }


    //pour l'update on rajoute "set" devant :

    String setObject(DataObject contact) {
        if (contact.getClass() == Militant.class) {
            return setMilitant((Militant) contact);
        } else {
            return setPorte((Porte) contact);
        }
    }

    private String setMilitant(Militant contact) {
        return String
                .format("{\"$set\" : {\"militant\" : {\"pseudo\": \"%s\", "
                                + " \"email\": \"%s\", "
                                + "\"password\": \"%s\",\"admin\": \"%b\" }}}",
                        contact.pseudo, contact.email, contact.password, contact.admin);
    }

    @SuppressLint("DefaultLocale")
    private String setPorte(Porte contact) {
        return String
                .format("{\"adresseResume\": \"%s\", \"complement\": \"%s\",\"nom_rue\": \"%s\",\"nom_ville\": \"%s\",\"numA\": \"%s\",\"numS\": \"%s\", " +
                                "\"ouverte\": \"%b\", " +
                                "\"location\" : { " +
                                "\"type\" : \"Point\"," +
                                "\"coordinates\": [%f,%f]}, \"person\": \"%s\"}",
                        contact.adresseResume, contact.complement, contact.nom_rue, contact.nom_ville, contact.numA, contact.numS, contact.ouverte, contact.longitude, contact.latitude, contact.user_id);
    }


    public String buildProcheURL(Pair<Double, Double> first) {
        return getBaseUrl() + "portes?q={location: { $near: { $geometry: { type: \"Point\",  coordinates: ["+first.second+" , "+ first.first+"] },$maxDistance: 5000 }}}" + "&apiKey=" + getApiKey();
    }

    public String buildGetMyPorteURL(String objects, String user_id) { // on donne une collection et des couples id, valeur pour filtrer
        String res = getBaseUrl() + objects + "?q={";
        res = res + "\"person\":\"" + user_id + "\"";
        res = res + "}&apiKey=" + getApiKey();
        return res;
    }
}