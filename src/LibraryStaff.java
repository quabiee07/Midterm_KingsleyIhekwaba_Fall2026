abstract public class LibraryStaff extends Person {
    private final int staffId;
    private final String department;
    private final double monthlySalary;
    static int staffCount = 0;


    public LibraryStaff(int personId, String name, String phoneNumber, int staffId, String department, double monthlySalary) throws Exception {
        super(personId, name, phoneNumber);
        staffCount += 1;
        if (staffId <= 0) throw new InvalidPersonException("Staff ID cannot be zero or negative");
        if (department.trim().isEmpty()) throw new InvalidPersonException("Department cannot be empty");
        if (monthlySalary < 0) throw new InvalidPersonException("Monthly Salary cannot be zero or negative");
        this.staffId = staffId;
        this.department = department;
        this.monthlySalary = monthlySalary;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getDepartment() {
        return department;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public static int getStaffCount() {
        return staffCount;
    }

    public void displayStaffDetails() {
        super.displayBasicDetails();
        System.out.println("Staff ID: S" + getStaffId());
        System.out.println("Department: " + getDepartment());
    }

    abstract public void calculateMonthlyCompensation();


}
