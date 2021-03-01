package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaxXRoundabout implements Intersection {
    private int lanes, t, noCars;
    public CyclicBarrier barrier;
    public Semaphore semaphore;

    public void setLimits(int lanes, int t, int noCars) {
        this.lanes = lanes;
        this.t = t;
        this.noCars = noCars;
        this.barrier = new CyclicBarrier(this.noCars);
        this.semaphore = new Semaphore(this.noCars);
    }

    public int getLanes() {
        return lanes;
    }

    public int getT() {
        return t;
    }
}
