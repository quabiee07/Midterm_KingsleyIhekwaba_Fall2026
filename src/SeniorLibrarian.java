public class SeniorLibrarian extends Librarian {
    private final String specialization;
    private final double seniorityAllowance;

    public SeniorLibrarian(int personId, String name, String phoneNumber, int staffId, String department,
                           double monthlySalary, int dailyIssueLimit, int booksProcessed,
                           String specialization, double seniorityAllowance) throws Exception {
        super(personId, name, phoneNumber, staffId, department, monthlySalary, dailyIssueLimit, booksProcessed);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidPersonException("Specialization cannot be empty");
        }
        if (!Double.isFinite(seniorityAllowance) || seniorityAllowance < 0) {
            throw new InvalidPersonException("Seniority allowance must be a non-negative number");
        }
        this.specialization = specialization.trim();
        this.seniorityAllowance = seniorityAllowance;
    }

    public String getSpecialization() {
        return specialization;
    }

    public double getSeniorityAllowance() {
        return seniorityAllowance;
    }

    /**
     * Librarian compensation (salary + $2.00 per book) plus the seniority allowance.
     */
    @Override
    public void calculateMonthlyCompensation() {
        System.out.printf("Monthly Compensation: $%.2f%n", getBaseCompensation());
    }

    @Override
    public String getRoleDescription() {
        return "Senior Librarian";
    }

    @Override
    public void displayStaffDetails() {
        // Parent prints the common details, role, base salary and (this class's) compensation.
        super.displayStaffDetails();
        System.out.println("Specialization: " + specialization);
        System.out.printf("Seniority Allowance: $%.2f%n", seniorityAllowance);
    }
}