import java.util.ArrayList;
import java.util.List;

/** Member extends Person (single inheritance) and implements Reportable and Reservable (hybrid). */
public class Member extends Person implements Reportable, Reservable {

    private static final String DEFAULT_TYPE = "Standard";

    private final int memberNumber;
    private int age;
    private String membershipType;
    private double outstandingFees;
    private boolean registered;
    private final List<String> reservedBooks = new ArrayList<>();

    public Member(int personId, String name, String phoneNumber, int memberNumber, int age) throws Exception {
        super(personId, name, phoneNumber);
        if (memberNumber <= 0) {
            throw new InvalidPersonException("Member number must be positive");
        }
        validateAge(age);
        this.memberNumber = memberNumber;
        this.age = age;
        this.membershipType = null;
        this.outstandingFees = 0.0;
        this.registered = false;
        finishConstruction(Member.class);
    }

    @Override
    public String getRoleDescription() {
        return "Library Member";
    }

    // ---- Overloaded registerMember() methods ----

    /** Basic registration: assigns the default membership type. */
    public void registerMember() throws Exception {
        registerMember(DEFAULT_TYPE);
    }

    /** Registration with a specific membership type. */
    public void registerMember(String membershipType) throws Exception {
        if (membershipType == null || membershipType.trim().isEmpty()) {
            throw new InvalidPersonException("Membership type cannot be empty");
        }
        this.membershipType = membershipType.trim();
        this.registered = true;
        System.out.println(getName() + " (#" + memberNumber + ") registered as "
                + this.membershipType + " member.");
    }

    // ---- Reservable ----

    @Override
    public void reserveBook(String bookId) {
        if (!registered) {
            throw new IllegalStateException("Member must be registered to reserve a book.");
        }
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book id cannot be empty.");
        }
        String id = bookId.trim();
        if (reservedBooks.contains(id)) {
            throw new IllegalArgumentException("Book " + id + " is already reserved by this member.");
        }
        reservedBooks.add(id);
        System.out.println(getName() + " reserved book " + id + ".");
    }

    public List<String> getReservedBooks() {
        return new ArrayList<>(reservedBooks);
    }

    // ---- Reportable ----

    @Override
    public String generateReport() {
        return "Member Report\n"
                + "Name: " + getName() + "\n"
                + "Member #: " + memberNumber + "\n"
                + "Age: " + age + "\n"
                + "Membership Type: " + (membershipType == null ? "Not registered" : membershipType) + "\n"
                + "Outstanding Fees: $" + String.format("%.2f", outstandingFees) + "\n"
                + "Reserved Books: " + (reservedBooks.isEmpty() ? "None" : String.join(", ", reservedBooks));
    }

    // ---- Fees ----

    public void addFee(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Fee must be a non-negative number.");
        }
        outstandingFees += amount;
    }

    public void payFee(double amount) {
        if (!Double.isFinite(amount) || amount < 0 || amount > outstandingFees) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }
        outstandingFees -= amount;
    }

    // ---- Getters / setters ----

    public int getMemberNumber() {
        return memberNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws Exception {
        validateAge(age);
        this.age = age;
    }

    private static void validateAge(int age) throws InvalidPersonException {
        if (age <= 0 || age > 120) {
            throw new InvalidPersonException("Age must be between 1 and 120");
        }
    }

    public String getMembershipType() {
        return membershipType;
    }

    public double getOutstandingFees() {
        return outstandingFees;
    }

    public boolean isRegistered() {
        return registered;
    }

    @Override
    public String toString() {
        return super.toString()
                + ", Member #: " + memberNumber
                + ", Age: " + age
                + ", Type: " + (membershipType == null ? "Not registered" : membershipType)
                + ", Outstanding Fees: $" + String.format("%.2f", outstandingFees);
    }
}

//public class Member extends Person {
//    private static final String DEFAULT_TYPE = "Standard";
//    private final int memberNumber;
//    private final int age;
//    private String membershipType;
//    private final double outstandingFees;
//
//
//    public Member(int personId, String name, String phoneNumber, int memberNumber, int age, String membershipType, double outstandingFees) throws Exception {
//        super(personId, name, phoneNumber);
//        if (memberNumber <= 0) throw new InvalidPersonException("Member Number cannot be zero or negative");
//        if (age < 18 || age >= 65) throw new InvalidPersonException("Age cannot be less than 18 or greater than 65");
//        if (membershipType.trim().isEmpty()) throw new InvalidPersonException("Membership type must not be empty!");
//        if (outstandingFees < 0) throw new InvalidPersonException("Outstanding fees must not be negative");
//        this.memberNumber = memberNumber;
//        this.age = age;
//        this.membershipType = membershipType;
//        this.outstandingFees = outstandingFees;
//    }
//
//    @Override
//    public String getRoleDescription() {
//        return "Member";
//    }
//
//    public int getMemberNumber() {
//        return memberNumber;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public String getMembershipType() {
//        return membershipType;
//    }
//
//    public double getOutstandingFees() {
//        return outstandingFees;
//    }
//
//    /*
//     * This function displays all information about a Member
//     */
//    public void displayBasicDetails() {
//        super.displayBasicDetails();
//        System.out.println("Member Number: M" + getMemberNumber());
//        System.out.println("Age: " + getAge());
//        System.out.println("Role: " + getRoleDescription());
//        System.out.println("Membership: " + getMembershipType());
//        System.out.printf("Outstanding Fees:  $%.2f", getOutstandingFees());
//    }
//
//    /*
//     * This function registers a Member without a membership type
//     */
//    public void registerMember() {
//        registerMember(DEFAULT_TYPE);
//    }
//
//    /*
//     * This function registers a Member with a membership type
//     */
//    public void registerMember(String membershipType) {
//        if (membershipType == null || membershipType.trim().isEmpty()) {
//            throw new IllegalArgumentException("Membership type cannot be empty.");
//        }
//        this.membershipType = membershipType;
//        System.out.println(getName() + " (M" + memberNumber + ") registered as "
//                + this.membershipType + " member.");
//        displayBasicDetails();
//    }
//}
