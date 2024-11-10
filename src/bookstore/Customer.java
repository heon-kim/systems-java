package bookstore;

class Customer {
    private String name;
    private String contact;

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public void showInfo() {
        System.out.println("이름 " + name + " 연락처 " + contact);
    }
}
