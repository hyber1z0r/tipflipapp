package com.ohnana.tipflip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakobgaardandersen on 06/05/15.
 */
public class DataDownloadedListener {
    private List<DownloadObserver> observers;
    private int tasks = 0;

    public DataDownloadedListener(){
        observers = new ArrayList<>();
    }

    public void registerObserver(DownloadObserver observer) {
        observers.add(observer);
    }

    public void done() {
        tasks++;
        if(tasks == 3) {
            notifyObservers();
        }
    }

    private void notifyObservers() {
        tasks = 0;
        for(DownloadObserver observer : observers) {
            observer.update();
        }
    }

}
