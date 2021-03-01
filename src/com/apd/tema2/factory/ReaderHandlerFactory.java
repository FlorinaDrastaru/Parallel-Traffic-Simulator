package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        switch (handlerType) {
            case "simple_semaphore":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) {
                        // Exemplu de utilizare:
                         Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                    }
                };
            case "simple_n_roundabout":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        // To parse input line use:
                        Main.intersection = IntersectionFactory.getIntersection("simple_n_roundabout");
                        String[] line = br.readLine().split(" ");
                        ((SimpleRoundabout)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    }
                };
            case "simple_strict_1_car_roundabout":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("simple_strict_1_car_roundabout");
                        String[] line = br.readLine().split(" ");
                        ((SimpleStrict1Roundabout)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    }
                };
            case "simple_strict_x_car_roundabout":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("simple_strict_x_car_roundabout");
                        String[] line = br.readLine().split(" ");
                        ((SimpleStrictXRoundabout)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                        Integer.parseInt(line[2]));
                    }
                };
            case "simple_max_x_car_roundabout":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("simple_max_x_car_roundabout");
                        String[] line = br.readLine().split(" ");
                        ((SimpleMaxXRoundabout)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                        Integer.parseInt(line[2]));
                    }
                };
            case "priority_intersection":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("priority_intersection");
                        String[] line = br.readLine().split(" ");
                        ((PriorityIntersection)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    }
                };
            case "crosswalk":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("crosswalk");
                        String[] line = br.readLine().split(" ");
                        //((Crosswalk)Main.intersection)
                                //.setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                        Main.pedestrians = new Pedestrians(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    }
                };
            case "simple_maintenance":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("simple_maintenance");
                        String[] line = br.readLine().split(" ");
                        ((SimpleMaintenance)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]));
                    }
                };
            case "complex_maintenance":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("complex_maintenance");
                        String[] line = br.readLine().split(" ");
                        ((ComplexMaintenance)Main.intersection)
                                .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                        Integer.parseInt(line[2]));
                    }
                };
            case "railroad":
                return new ReaderHandler() {
                    @Override
                    public void handle(final String handlerType, final BufferedReader br) throws IOException {
                        Main.intersection = IntersectionFactory
                                .getIntersection("railroad");
                        //String[] line = br.readLine().split(" ");
                        //((Railroad)Main.intersection)
                               // .setLimits(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                 //       Integer.parseInt(line[2]));
                    }
                };
            default:
                return null;
        }
    }

}
