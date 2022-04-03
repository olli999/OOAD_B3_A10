package main;

import java.util.Objects;

public class Sammelbild {

    private String bildSerie;
    private int bildNr;

    public Sammelbild(String serie, int nr) {
        this.bildSerie = serie;
        this.bildNr = nr;

    }

    public String getBildSerie() {
        return bildSerie;
    }

    public int getBildNr() {
        return bildNr;
    }

    @Override
    public String toString() {
        return "Bild{" + bildSerie + ',' + bildNr + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sammelbild that = (Sammelbild) o;
        return bildNr == that.bildNr && Objects.equals(bildSerie, that.bildSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bildSerie, bildNr);
    }
}
