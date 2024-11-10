package company;

abstract class Worker implements Employee {
    protected String name;

    public Worker(String name) {
        this.name = name;
    }
}
