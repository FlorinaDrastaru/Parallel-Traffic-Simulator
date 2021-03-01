package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXRoundabout implements Intersection {
    private int lanes, t, noCars;
    public CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
    public CyclicBarrier barrier2;
    public ArrayList<Semaphore> semaphores = new ArrayList<>();

    public void setLimits(int lanes, int t, int noCars) {
        this.lanes = lanes;
        this.t = t;
        this.noCars = noCars;
        this.barrier2 = new CyclicBarrier(this.lanes * this.noCars);

        for(int i = 0; i < lanes; i++) {
            semaphores.add(new Semaphore(noCars));
        }
    }

    public int getT() {
        return t;
    }
}
