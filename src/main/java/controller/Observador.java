package controller;

public interface Observador<T> {
    void atualizar(T data);
}
