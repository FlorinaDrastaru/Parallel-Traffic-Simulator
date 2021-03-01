package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;


import java.util.concurrent.*;

public class Railroad implements Intersection {
    public CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
    public BlockingQueue<Car> cars = new ArrayBlockingQueue<Car>(Main.carsNo);
    public Semaphore semaphore = new Semaphore(1);

}
