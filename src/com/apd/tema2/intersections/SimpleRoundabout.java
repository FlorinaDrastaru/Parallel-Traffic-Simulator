package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleRoundabout implements Intersection {
    public Semaphore semaphore;
    public int n, t;
    public void setLimits(int n, int t) {
        this.n = n;
        this.t = t;
        this.semaphore = new Semaphore(this.n);
    }

    public int getT() {
        return t;
    }

    public int getN() {
        return n;
    }
}
