package model;

public enum Jogador {
    BRANCO,
    MARROM;

    public Jogador opposite() {
        return this == BRANCO ? MARROM : BRANCO;
    }
}
