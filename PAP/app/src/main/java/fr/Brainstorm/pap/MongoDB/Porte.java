package fr.jlm2017.pap.MongoDB;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thoma on 15/02/2017.
 */

public class Porte extends DataObject implements Parcelable {

    public String adresseResume="";
    public String numS="";
    public String numA="";
    public String complement="";
    public String nom_rue="";
    public String nom_ville="";
    public Boolean ouverte=false;
    public double latitude=0;
    public double longitude=0;
    public String user_id="";

    public Porte(String adresseResume, String numS, String numA, String complement, String nom_rue, String nom_ville, Boolean ouverte, double latitude, double longitude, String user_id) {
        this.adresseResume = adresseResume;
        this.numS = numS;
        this.numA = numA;
        this.complement = complement;
        this.nom_rue = nom_rue;
        this.nom_ville = nom_ville;
        this.ouverte = ouverte;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
    }

    public Porte(DataWrapperPortes.BigDataPorte big) {
        this.id_=big._id.$oid;
        this.adresseResume = big.porte.adresseResume;
        this.numS = big.porte.numS;
        this.numA = big.porte.numA;
        this.complement = big.porte.complement;
        this.nom_rue = big.porte.nom_rue;
        this.nom_ville = big.porte.nom_ville;
        this.ouverte = big.porte.ouverte;
        this.latitude = big.porte.latitude;
        this.longitude = big.porte.longitude;
        this.user_id = big.porte.user_id;
    }

    @Override
    public String toString() {
        return "Militant{ adresseResume ='" + adresseResume + '\'' +
                ", numS='" + numS + '\'' +
                ", numA='" + numA + '\'' +
                ", complement=" + complement +
                ", nom_rue='" + nom_rue + '\'' +
                ", nom_ville='" + nom_ville + '\'' +
                ", ouverte='" + ouverte + '\'' +
                ", latitude='" + latitude + '\'' +
                ", user_id='" + user_id + '\'' +
                ", longitude='" + longitude + '\'' +
                ", id='" + this.id_ + "'}";
    }
    // pour pouvoir etre Parcelable et s'envoyer avec des Intent
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id_);
        dest.writeString(adresseResume);
        dest.writeString(numS);
        dest.writeString(numA);
        dest.writeString(complement);
        dest.writeString(nom_rue);
        dest.writeString(nom_ville);
        dest.writeString(user_id);
        dest.writeString(String.valueOf(latitude));
        dest.writeString(String.valueOf(longitude));
        dest.writeString(String.valueOf(ouverte));
    }

    public static final Parcelable.Creator<Porte> CREATOR = new Parcelable.Creator<Porte>() {
        @Override
        public Porte createFromParcel(Parcel source) {
            return new Porte(source);
        }

        @Override
        public Porte[] newArray(int size) {
            return new Porte[size];
        }
    };

    public Porte(Parcel in) {
        id_ = in.readString();
        adresseResume = in.readString();
        numS = in.readString();
        numA = in.readString();
        complement = in.readString();
        nom_rue = in.readString();
        nom_ville = in.readString();
        user_id = in.readString();
        latitude= Double.parseDouble(in.readString());
        longitude=Double.parseDouble(in.readString());
        ouverte=Boolean.parseBoolean(in.readString());
    }
}
