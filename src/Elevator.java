public class Elevator {
    private int current = 1;
    private int destination = 1;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCurrent() {
        return current;
    }

    public int getDestination() {
        return destination;
    }

    public void moveUp() {
        current++;
    }

    public void moveDown() {
            current--;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
