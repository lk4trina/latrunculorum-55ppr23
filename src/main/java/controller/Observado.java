package controller;

import java.util.ArrayList;
import java.util.List;

public class Observado<T> {
    private final List<Observador<T>> obs = new ArrayList<>();

    public void addObserver(Observador<T> observer) {
        obs.add(observer);
    }

    public void removeObserver(Observador<T> observer) {
        obs.remove(observer);
    }

    public void notifObservadores(T data) {
        for (Observador<T> observer : obs) {
            observer.atualizar(data);
        }
    }
}
