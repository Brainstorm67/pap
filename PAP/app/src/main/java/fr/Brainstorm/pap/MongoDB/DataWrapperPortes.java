package fr.Brainstorm.pap.MongoDB;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by thoma on 16/02/2017.
 */

public class DataWrapperPortes {
        public List<BigPorte> data;

        static DataWrapperPortes fromJson(String s) {
            DataWrapperPortes dw = new Gson().fromJson(s, DataWrapperPortes.class);
            return dw;
        }

        public static String IDfromJson(String s){
            BigPorte dataWrapper = new Gson().fromJson(s, BigPorte.class);
            return dataWrapper._id.$oid;

        }

        public String toString() {
            return new Gson().toJson(this);
        }

    class BigPorte {
        public Identifier _id;
        public String adresseResume="";
        public String numS="";
        public String numA="";
        public String complement="";
        public String nom_rue="";
        public String nom_ville="";
        public Boolean ouverte=false;
        public Location location;
        public String person="";

        public BigPorte(String adresseResume, String numS, String numA, String complement, String nom_rue, String nom_ville, Boolean ouverte, double latitude, double longitude, String user_id) {
            this.adresseResume = adresseResume;
            this.numS = numS;
            this.numA = numA;
            this.complement = complement;
            this.nom_rue = nom_rue;
            this.nom_ville = nom_ville;
            this.ouverte = ouverte;
            this.location = new Location(latitude,longitude);
            this.person = user_id;
        }
    }

    class Location {
        public String type="";
        public List<Double> coordinates;
        public Location ( double longitude, double latitude) {
            coordinates.add(longitude);
            coordinates.add (latitude);
        }
    }

}
