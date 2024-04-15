import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number of elevators and floors: ");

        int numElevators = 0, buildingHeight = 0;
        try {
            numElevators = scanner.nextInt();
            buildingHeight = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Wrong input.");
            return;
        }

        ElevatorSystem elevatorSystem = new ElevatorSystem(numElevators, buildingHeight);
    }
}