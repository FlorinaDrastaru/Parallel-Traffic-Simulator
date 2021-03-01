package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection {
    private int noHighPrior, noLowPrior;
    public LinkedBlockingQueue<Car> intersectionCars = new LinkedBlockingQueue<>();
    public Semaphore s = new Semaphore(1);
    public final AtomicInteger intCars = new AtomicInteger(0);

    public void setLimits(int noHighPrior, int noLowPrior) {
        this.noHighPrior = noHighPrior;
        this.noLowPrior = noLowPrior;

    }




}
