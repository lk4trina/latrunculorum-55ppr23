package model;

public enum Jogador {
    BRANCO,
    PRETO;

    public Jogador opposite() {
        return this == BRANCO ? PRETO : BRANCO;
    }
}
