package com.example.gametracker;


import java.util.Objects;

//Code obtain from this stackoverflow thread
//https://stackoverflow.com/questions/10234487/storing-number-pairs-in-java?noredirect=1&lq=1
public class Pair<T, V> {
    T p1;
    V p2;
    Pair(T p1, V p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public T getP1() {
        return p1;
    }

    public void setP1(T p1) {
        this.p1 = p1;
    }

    public V getP2() {
        return p2;
    }

    public void setP2(V p2) {
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(p1, pair.p1) &&
                Objects.equals(p2, pair.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}
