package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        switch (handlerType) {
            case "simple_semaphore":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        // masina ajunge la semafor
                        System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                        // masina asteapta la semafor
                        try {
                            sleep(car.getWaitingTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // dupa asteptare, masina porneste
                        System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                    }
                };
            case "simple_n_roundabout":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        int t = ((SimpleRoundabout)Main.intersection).getT();
                        // masina ajunge la semafor
                        System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                        try {
                            ((SimpleRoundabout)Main.intersection).semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // masina intra in sensul giratoriu
                        System.out.println("Car " + car.getId() + " has entered the roundabout");
                        // sta t milisecunde in sens
                        try {
                            sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // masina paraseste sensul giratoriu
                        System.out.println("Car " + car.getId()
                                + " has exited the roundabout after " + (t/1000) + " seconds");
                        ((SimpleRoundabout)Main.intersection).semaphore.release();
                    }
                };
            case "simple_strict_1_car_roundabout":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        int t = ((SimpleStrict1Roundabout)Main.intersection).getT();
                        // fiecare masina ajunge la sensul giratoriu
                        System.out.println("Car " + car.getId() + " has reached the roundabout");

                        try {
                            ((SimpleStrict1Roundabout)Main.intersection).semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // intra in sensul giratoriu
                        System.out.println("Car " + car.getId()
                                + " has entered the roundabout from lane " + car.getStartDirection());

                        // sta t milisecunde in sensul giratoriu
                        try {
                            sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // paraseste sensul giratoriu
                        System.out.println("Car " + car.getId()
                                + " has exited the roundabout after " + (t/1000) + " seconds");
                        ((SimpleStrict1Roundabout)Main.intersection).semaphore.release();


                    }
                };
            case "simple_strict_x_car_roundabout":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        int t = ((SimpleStrictXRoundabout)Main.intersection).getT();
                        // masina ajunge la sensul giratoriu
                        System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                        // se asteapta ca toate masinile sa ajunga
                        try {
                            ((SimpleStrictXRoundabout)Main.intersection).barrier.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // folosesc semafor si bariera
                        // pentru a permite trecerea doar a x masini pe sens
                        try {
                            ((SimpleStrictXRoundabout)Main.intersection).semaphores
                                    .get(car.getStartDirection()).acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId()
                                + " was selected to enter the roundabout from lane " + car.getStartDirection());

                       try {
                            ((SimpleStrictXRoundabout)Main.intersection).barrier2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Car " + car.getId()
                                + " has entered the roundabout from lane " + car.getStartDirection());

                        // timpul necesar pentru a parasi sensul giratoriu
                        try {
                            sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // masina paraseste sensul
                        System.out.println("Car " + car.getId()
                                + " has exited the roundabout after " + (t/1000) + " seconds");

                        // se asteapta sa treaca toate masinile din directia respectiva
                        try {
                            ((SimpleStrictXRoundabout)Main.intersection).barrier2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        ((SimpleStrictXRoundabout)Main.intersection).semaphores
                                .get(car.getStartDirection()).release();

                    }


                };
            case "simple_max_x_car_roundabout":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        try {
                            sleep(car.getWaitingTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        int t = ((SimpleMaxXRoundabout)Main.intersection).getT();
                        // masina ajunge la sensul giratoriu dintr-o anumita directie
                        System.out.println("Car " + car.getId()
                                + " has reached the roundabout from lane " + car.getStartDirection());

                        // folosesc semafor pentru a limita nr de masini ce patrund dintr-o directie
                        try {
                            ((SimpleMaxXRoundabout)Main.intersection).semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // intra in sensul giratoriu
                        System.out.println("Car " + car.getId()
                                + " has entered the roundabout from lane " + car.getStartDirection());

                        // timpul necesar pentru a traversa sensul
                        try {
                            sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // paraseste sensul giratoriu
                        System.out.println("Car " + car.getId()
                                + " has exited the roundabout after " + (t/1000) + " seconds");

                        // se asteapta sa iasa toate masinile care au intrat intr-o runda
                        try {
                            ((SimpleMaxXRoundabout)Main.intersection).barrier.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        ((SimpleMaxXRoundabout)Main.intersection).semaphore.release();

                    }
                };
            case "priority_intersection":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        try {
                            sleep(car.getWaitingTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // verific daca masina are prioritate mare
                        if (car.getPriority() != 1) {
                            // intra in intersectie
                            System.out.println("Car " + car.getId()
                                    + " with high priority has entered the intersection");
                            ((PriorityIntersection)Main.intersection).intCars.getAndAdd(1);
                            // durata timpului petrecut in intersectie
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // iese din intersectie
                            System.out.println("Car " + car.getId()
                                    + " with high priority has exited the intersection");
                            ((PriorityIntersection)Main.intersection).intCars.getAndAdd(-1);
                            // daca nu mai sunt masini cu prioritate mare in intersectie,
                            // anunt celelalte threaduri
                            if (((PriorityIntersection)Main.intersection).intCars.get() == 0) {
                                synchronized (((PriorityIntersection)Main.intersection).intCars) {
                                    ((PriorityIntersection)Main.intersection).intCars.notifyAll();
                                }
                            }
                        } else { // cazul pentru prioritate mica
                            ((PriorityIntersection) Main.intersection).intersectionCars.add(car);
                            System.out.println("Car " + car.getId()
                                    + " with low priority is trying to enter the intersection...");
                            // daca in intersectie exista masini cu prioritate mare,
                            // cele cu prioritate mica asteapta
                            if (((PriorityIntersection) Main.intersection).intCars.get() != 0) {
                                synchronized (((PriorityIntersection)Main.intersection).intCars) {
                                    try {
                                        ((PriorityIntersection)Main.intersection).intCars.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            // cand se goleste intersectia, incep sa treaca masinile cu prioritate mica
                            // in ordinea in care au ajuns in intersectie
                            if (((PriorityIntersection) Main.intersection).intCars.get() == 0) {
                                try {
                                    ((PriorityIntersection) Main.intersection).s.acquire();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Car car1 = null;
                                try {
                                    car1 = ((PriorityIntersection) Main.intersection).intersectionCars.take();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Car " + car1.getId()
                                        + " with low priority has entered the intersection");
                                ((PriorityIntersection) Main.intersection).s.release();
                            }

                        }

                    }
                };
            case "crosswalk":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        AtomicBoolean nowRed = new AtomicBoolean();
                        AtomicBoolean prevRed = new AtomicBoolean(true);
                        // repet actiunea pana cand trec toti pietonii
                        while (!Main.pedestrians.isFinished()) {
                            // daca pietonii trec, e rosu la masini
                            if (Main.pedestrians.isPass()) {
                                    nowRed.set(true);
                                    if (!prevRed.get()) {
                                        System.out.println("Car " + car.getId() + " has now red light");
                                    }
                            } else { // daca nu trec pietoni, e verde la masini
                                    nowRed.set(false);
                                    if (prevRed.get()) {
                                        System.out.println("Car " + car.getId() + " has now green light");
                                    }
                            }
                            prevRed.set(nowRed.get());
                        }
                        nowRed.set(false);
                        // dupa ce trece ultima runda de pietoni, masinile au verde
                        if (prevRed.get() != nowRed.get()) {
                            System.out.println("Car " + car.getId() + " has now green light");
                        }
                    }
                };
            case "simple_maintenance":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        // masina ajunge la drum dintr-o anumita directie
                        System.out.println("Car " + car.getId()
                                + " from side number " + car.getStartDirection()
                                + " has reached the bottleneck");

                        // mutex
                        try {
                            ((SimpleMaintenance) Main.intersection).s.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // in functie de directie, adaug masina in coada potrivita
                        if (car.getStartDirection() == 0) {
                            ((SimpleMaintenance)Main.intersection).lane0.add(car);
                        } else {
                            ((SimpleMaintenance)Main.intersection).lane1.add(car);
                        }

                        ((SimpleMaintenance) Main.intersection).s.release();

                        try {
                            ((SimpleMaintenance)Main.intersection).barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        // trec masinile care vin din directia 0
                        if (car.getStartDirection() == 0) {
                            try {
                                ((SimpleMaintenance) Main.intersection).s0.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Car car0 = ((SimpleMaintenance) Main.intersection).lane0.poll();
                            System.out.println("Car " + car0.getId()
                                    + " from side number " + car0.getStartDirection()
                                    + " has passed the bottleneck");
                            ((SimpleMaintenance) Main.intersection).s0.release();
                        }
                        // toate masinile asteapta finalizarea actiunii precedente
                        try {
                            ((SimpleMaintenance)Main.intersection).barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        // daca au trecut masinile din directia 0,
                        // incep sa treaca si cele din directia 1
                        if (((SimpleMaintenance) Main.intersection).lane0.isEmpty() == true)
                        while (((SimpleMaintenance) Main.intersection).lane1.isEmpty() == false) {
                            Car car1 = ((SimpleMaintenance) Main.intersection).lane1.poll();
                            System.out.println("Car " + car1.getId()
                                    + " from side number " + car1.getStartDirection()
                                    + " has passed the bottleneck");
                        }
                    }
                };

            case "complex_maintenance":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        System.out.println("Car " + car.getId() +
                                " has come from the lane number " + car.getStartDirection());
                        try {
                            ((ComplexMaintenance) Main.intersection).s.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // adaug masina in coada potrivita
                        ((ComplexMaintenance)Main.intersection).lanes.get(car.getStartDirection()).add(car);
                        ((ComplexMaintenance) Main.intersection).s.release();

                        // astept finalizarea adaugarii in coada
                        try {
                            ((ComplexMaintenance)Main.intersection).barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        try {
                            ((ComplexMaintenance) Main.intersection).semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Car car1 = ((ComplexMaintenance)Main.intersection).lanes
                                .get(car.getStartDirection()).poll();
                        System.out.println("Car " + car1.getId() + " from the lane "
                                + car1.getStartDirection() + " has entered lane number "
                                + car1.getStartDirection());
                        // verific daca semaforul mai are permisiuni
                        if (((ComplexMaintenance) Main.intersection).semaphore.availablePermits() == 0) {

                            System.out.println("The initial lane " + car.getStartDirection()
                                    + " has no permits and is moved to the back of the new lane queue");
                        }
                        ((ComplexMaintenance) Main.intersection).semaphore.release();
                        try {
                            ((ComplexMaintenance) Main.intersection).s.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // verific daca mai exista masini in coada
                        if (((ComplexMaintenance)Main.intersection).lanes
                                .get(car.getStartDirection()).isEmpty()) {
                            System.out.println("The initial lane " + car.getStartDirection()
                                    + " has been emptied and removed from the new lane queue");
                        }
                        ((ComplexMaintenance) Main.intersection).s.release();
                    }
                };
            case "railroad":
                return new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {
                        synchronized (Main.lock) {
                            // masinile incep sa ajunga la bariera
                            System.out.println("Car " + car.getId()
                                + " from side number " + car.getStartDirection()
                                + " has stopped by the railroad");
                            // adaug fiecare masina in coada pentru a tine minte ordinea
                            try {
                                ((Railroad) Main.intersection).cars.put(car);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // toate masinile asteapta la bariera
                        try {
                            ((Railroad)Main.intersection).barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        // doar un thread anunta ca a trecut trenul
                        if (car.getId() == 0) {
                            System.out.println("The train has passed, cars can now proceed");
                        }

                        // toate masinile asteapta sa treaca trenul
                        try {
                            ((Railroad)Main.intersection).barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        // masinile incep sa conduca in functie de ordinea in care au ajuns la bariera
                        synchronized (Main.lock) {
                            Car car1 = null;
                            try {
                                car1 = ((Railroad) Main.intersection).cars.take();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Car " + car1.getId() + " from side number "
                                    + car1.getStartDirection() + " has started driving");
                        }

                    }
                };
            default:
                return null;
        }
    }
}
