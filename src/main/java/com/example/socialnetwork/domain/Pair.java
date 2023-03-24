package com.example.socialnetwork.domain;

import java.util.Objects;

public class Pair<E extends Comparable<E>> {
    private E e1;
    private E e2;

    public Pair(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E getFirst() {
        return e1;
    }

    public void setFirst(E e1) {
        this.e1 = e1;
    }

    public E getSecond() {
        return e2;
    }

    public void setSecond(E e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "e1=" + e1 +
                ", e2=" + e2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return Objects.equals(e1, pair.e1) && Objects.equals(e2, pair.e2) || Objects.equals(e1, pair.e2) && Objects.equals(e2, pair.e1);
    }

    @Override
    public int hashCode() {
        if (e1.compareTo(e2) < 0)
            return Objects.hash(e1, e2);
        else
            return Objects.hash(e2, e1);
    }
}
