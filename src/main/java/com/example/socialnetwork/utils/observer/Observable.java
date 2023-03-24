package com.example.socialnetwork.utils.observer;

public interface Observable {
    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}
