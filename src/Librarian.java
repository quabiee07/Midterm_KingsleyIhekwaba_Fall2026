public class Librarian extends LibraryStaff {
    private static final double PROCESSING_ALLOWANCE_PER_BOOK = 2.0;
    private static final double DEFAULT_DAILY_LATE_RATE = 1.00;

    private final int dailyIssueLimit;
    private final int booksProcessed;

    public Librarian(int personId, String name, String phoneNumber, int staffId, String department, double monthlySalary, int dailyIssueLimit, int booksProcessed) throws Exception {
        super(personId, name, phoneNumber, staffId, department, monthlySalary);
        if (dailyIssueLimit <= 0) throw new InvalidPersonException("Daily Issue Limit cannot be zero or negative");
        if (booksProcessed <= 0) throw new InvalidPersonException("cannot be zero or negative");
        this.dailyIssueLimit = dailyIssueLimit;
        this.booksProcessed = booksProcessed;
    }

    public int getBooksProcessed() {
        return booksProcessed;
    }

    public int getDailyIssueLimit() {
        return dailyIssueLimit;
    }

    /*
    * Late fee at the default rate of $1.00 per overdue day.
    */
    public double calculateLateFee(int daysOverdue) {
        return calculateLateFee(daysOverdue, DEFAULT_DAILY_LATE_RATE);
    }

    /*
    * Late fee at the supplied daily rate.
    */
    public double calculateLateFee(int daysOverdue, double dailyRate) {
        if (daysOverdue < 0) {
            throw new IllegalArgumentException("Days overdue cannot be negative");
        }
        if (dailyRate < 0) {
            throw new IllegalArgumentException("Daily rate must be a non-negative number");
        }
        return daysOverdue * dailyRate;
    }

    /** Salary plus $2.00 per book processed, returned so subclasses can build on it. */
    protected double getBaseCompensation() {
        return getMonthlySalary() + (booksProcessed * PROCESSING_ALLOWANCE_PER_BOOK);
    }

    @Override
    public void calculateMonthlyCompensation() {
        double compensation = getMonthlySalary() + (booksProcessed * PROCESSING_ALLOWANCE_PER_BOOK);
        System.out.printf("Monthly Compensation: $%.2f%n", compensation);
    }

    @Override
    public String getRoleDescription() {
        return "Librarian";
    }

    @Override
    public void displayStaffDetails() {
        super.displayStaffDetails();
        System.out.println("Role: " + getRoleDescription());
        System.out.println("Base Salary: " + getMonthlySalary());
        calculateMonthlyCompensation();
    }
}
