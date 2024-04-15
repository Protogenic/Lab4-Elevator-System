import javax.crypto.spec.PSource;
import java.util.ArrayList;
import java.util.Random;

public class ElevatorSystem {
    private ArrayList<Elevator> elevators;
    private ArrayList<Integer> requests;
    private int buildingHeight;

    public ElevatorSystem(int numOfElevators, int buildingHeight) {
        elevators = new ArrayList<>();
        requests = new ArrayList<>();
        this.buildingHeight = buildingHeight;

        for (int i = 0; i < numOfElevators; i++) {
            Elevator elevator = new Elevator();
            elevator.setNumber(i + 1);
            elevators.add(elevator);
        }

        Thread requestThread = new Thread(new RequestGenerator());
        requestThread.start();
        Thread elevatorThread = new Thread(new ElevatorController());
        elevatorThread.start();
    }

    public synchronized void generateRequest(int floor) {
        requests.add(floor);
        notify();
    }

    private class RequestGenerator implements Runnable {
        private Random rand = new Random();

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                    int floor = rand.nextInt(buildingHeight) + 1;
                    generateRequest(floor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ElevatorController implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (ElevatorSystem.this) {
                    while (requests.isEmpty()) {
                        try {
                            ElevatorSystem.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }



                    Elevator closestElevator = findClosest(requests.get(0));
                    if (closestElevator != null) {
                        closestElevator.setDestination(requests.get(0));
                        System.out.printf("Elevator №%d received a request for floor №%d\n",
                                closestElevator.getNumber(), requests.get(0));
                        requests.remove(0);
                    }


                    for (Elevator elevator : elevators) {
                        if (elevator.getCurrent() != elevator.getDestination()) {
                            if (elevator.getCurrent() > elevator.getDestination()) {
                                elevator.moveDown();
                            } else if (elevator.getCurrent() < elevator.getDestination()) {
                                elevator.moveUp();
                            }
                            if (elevator.getCurrent() == elevator.getDestination()) {
                                System.out.printf("Elevator №%d arrived at floor №%d\n",
                                        elevator.getNumber(), elevator.getDestination());
                            }
                        }
                    }

                    print();
                }
            }
        }

        private Elevator findClosest(Integer request) {
            int minDif = Integer.MAX_VALUE;
            Elevator closestElevator = null;
            for (Elevator elevator : elevators) {
                if (elevator.getCurrent() == elevator.getDestination()) {
                    if (Math.abs(elevator.getCurrent() - request) < minDif) {
                        minDif = Math.abs(elevator.getCurrent() - request);
                        closestElevator = elevator;
                    }
                }
            }

            return closestElevator;
        }

        private void print() {
            for (int i = 0; i < elevators.size(); i++) {
                Elevator elevator = elevators.get(i);
                System.out.println("Elevator " + (i + 1) + " - Current floor: " +
                        elevator.getCurrent() + " - Destination floor: " +
                        elevator.getDestination());
            }
            System.out.println("\n---\n");
        }
    }
}
