package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrict1Roundabout implements Intersection {
    private int lanes, t;
    public static CyclicBarrier barrier;
    public static Semaphore semaphore = new Semaphore(1);

    public void setLimits(int lanes, int t) {
        this.lanes = lanes;
        this.t = t;
        this.barrier = new CyclicBarrier(this.lanes);
    }

    public int getLanes() {
        return lanes;
    }

    public int getT() {
        return t;
    }
}
