package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaintenance implements Intersection {
    private int maxCars;
    public CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
    public Semaphore s0;
    public Semaphore s1;
    public Queue<Car> lane0 = new LinkedList<>();
    public Queue<Car> lane1 = new LinkedList<>();
    public Semaphore s = new Semaphore(1);

    public void setLimits(int maxCars) {
        this.maxCars = maxCars;
        s0 = new Semaphore(this.maxCars);
        s1 = new Semaphore(1);
    }
}
