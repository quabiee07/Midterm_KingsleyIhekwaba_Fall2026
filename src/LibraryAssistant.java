public class LibraryAssistant extends LibraryStaff {
    private final String shiftType;
    private final double shiftAllowance;

    public LibraryAssistant(int personId, String name, String phoneNumber, int staffId, String department,
                            double monthlySalary, String shiftType, double shiftAllowance) throws Exception {
        super(personId, name, phoneNumber, staffId, department, monthlySalary);
        if (shiftType == null || shiftType.trim().isEmpty()) {
            throw new InvalidPersonException("Shift type cannot be empty");
        }
        if (!Double.isFinite(shiftAllowance) || shiftAllowance < 0) {
            throw new InvalidPersonException("Shift allowance must be a non-negative number");
        }
        this.shiftType = shiftType.trim();
        this.shiftAllowance = shiftAllowance;
    }

    public String getShiftType() {
        return shiftType;
    }

    public double getShiftAllowance() {
        return shiftAllowance;
    }

    /** Compensation = monthly salary + shift allowance. */
    @Override
    public void calculateMonthlyCompensation() {
        double compensation = getMonthlySalary() + shiftAllowance;
        System.out.printf("Monthly Compensation: $%.2f%n", compensation);
    }

    @Override
    public String getRoleDescription() {
        return "Library Assistant";
    }

    @Override
    public void displayStaffDetails() {
        super.displayStaffDetails();
        System.out.println("Role: " + getRoleDescription());
        System.out.println("Base Salary: " + getMonthlySalary());
        System.out.println("Shift Type: " + shiftType);
        System.out.printf("Shift Allowance: $%.2f%n", shiftAllowance);
        calculateMonthlyCompensation();
    }
}