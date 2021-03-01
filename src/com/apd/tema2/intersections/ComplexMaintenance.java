package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class ComplexMaintenance implements Intersection {
    private int freeLanes, maxLanes, maxCars;
    public CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
    public Semaphore semaphore;
    public Semaphore s = new Semaphore(1);
    public LinkedList<Queue<Car>> lanes = new LinkedList<Queue<Car>>();

    public void setLimits(int freeLanes, int maxLanes, int maxCars) {
        this.freeLanes = freeLanes;
        this.maxLanes = maxLanes;
        this.maxCars = maxCars;
        semaphore = new Semaphore(this.maxCars);
        for (int i = 0; i < maxLanes; i++) {
            lanes.add(new LinkedList<Car>());
        }

    }
}
