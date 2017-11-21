package fr.Brainstorm.pap.MongoDB;

/**
 * Created by thoma on 24/02/2017.
 * Project : Porte à Porte pour Brainstorm
 */

public interface InterfaceReceivedData<T> {

    void onResponseReceived(T result);
}
