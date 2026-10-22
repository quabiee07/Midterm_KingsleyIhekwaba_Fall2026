//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.util.List;

public class Main {

        public static void main(String[] args) throws Exception {

            // ------------------------------------------------------------
            System.out.println("=== 1. Register members (overloaded registerMember) ===");
            Member ada = new Member(1, "Ada Obi", "416-555-0101", 1001, 28);
            Member sam = new Member(2, "Sam Lee", "416-555-0102", 1002, 34);
            Member leo = new Member(3, "Leo Park", "416-555-0103", 1003, 41);// never registered

            ada.registerMember();// basic registration
            sam.registerMember("Premium");// registration with a membership type

            try {
                sam.registerMember("   ");// invalid registration data
            } catch (InvalidPersonException e) {
                System.out.println("Registration FAILED: " + e.getMessage());
            }
            try {
                new Member(4, "Too Young", "416-555-0104", 1004, 0);// invalid age
            } catch (InvalidPersonException e) {
                System.out.println("Registration FAILED: " + e.getMessage());
            }

            // ------------------------------------------------------------
            System.out.println("\n=== 2. Create staff (hierarchical + multilevel inheritance) ===");
            // Hierarchical: Librarian and LibraryAssistant both extend LibraryStaff.
            // Multilevel:   Person -> LibraryStaff -> Librarian -> SeniorLibrarian.
            Librarian librarian = new Librarian(5, "Maria Chen", "416-555-0105", 5001,
                    "Circulation", 4000.00, 50, 120);
            SeniorLibrarian senior = new SeniorLibrarian(6, "John Reyes", "416-555-0106", 5002,
                    "Archives", 5000.00, 60, 200, "Rare Books", 500.00);
            LibraryAssistant assistant = new LibraryAssistant(7, "Priya Nair", "416-555-0107", 5003,
                    "Circulation", 2800.00, "Evening", 150.00);

            LibraryStaff[] staffs = {librarian, senior, assistant};

            for(LibraryStaff staff: staffs){
                System.out.println("Created: " + staff.getRoleDescription() );
            }


            // ------------------------------------------------------------
            System.out.println("\n=== 3. Member and staff details ===");
            System.out.println(ada);
            System.out.println(sam);
            System.out.println(leo);
            System.out.println("-----");
            librarian.displayStaffDetails();
            System.out.println("-----");
            senior.displayStaffDetails();
            System.out.println("-----");
            assistant.displayStaffDetails();

            // ------------------------------------------------------------
            System.out.println("\n=== 4. Late fees (overloaded calculateLateFee) ===");
            System.out.printf("5 days overdue, default $1.00/day: $%.2f%n", librarian.calculateLateFee(5));
            System.out.printf("5 days overdue, $1.50/day:         $%.2f%n", librarian.calculateLateFee(5, 1.50));
            try {
                librarian.calculateLateFee(-2);
            } catch (IllegalArgumentException e) {
                System.out.println("Late fee REJECTED: " + e.getMessage());
            }
            try {
                librarian.calculateLateFee(3, -1.0);
            } catch (IllegalArgumentException e) {
                System.out.println("Late fee REJECTED: " + e.getMessage());
            }

            // ------------------------------------------------------------
            System.out.println("\n=== 5. Monthly compensation for each staff category ===");
            System.out.println("Librarian (4000 + 120 x $2)");
            librarian.calculateMonthlyCompensation();
            System.out.println("Senior Librarian (5000 + 200 x $2 + 500)");
            senior.calculateMonthlyCompensation();
            System.out.println("Library Assistant (2800 + 150)");
            assistant.calculateMonthlyCompensation();

            // ------------------------------------------------------------
            System.out.println("\n=== 6. Static counters ===");
            // A failed creation must not change any counter.
            try {
                new Librarian(8, "No Limit", "416-555-0108", 5004, "Circulation", 4000.00, 0, 10);
            } catch (InvalidPersonException e) {
                System.out.println("Creation FAILED: " + e.getMessage());
            }
            System.out.println("Total people: " + Person.getPersonCount());
            System.out.println("Staff:        " + LibraryStaff.getStaffCount());

            // ------------------------------------------------------------
            System.out.println("\n=== 7. Runtime polymorphism (LibraryStaff references) ===");
            LibraryStaff[] staff = {librarian, senior, assistant};
            for (LibraryStaff s : staff) {
                // Same method call,but different behavior
                System.out.print(s.getRoleDescription() + " -> ");
                s.calculateMonthlyCompensation();
            }
            LibraryStaff  ref = senior;     // a Senior Librarian seen through a LibraryStaff reference
            System.out.println("A LibraryStaff reference holding a " + ref.getRoleDescription() + ":");
            ref.displayStaffDetails();

            // ------------------------------------------------------------
            System.out.println("\n=== 8. Interfaces: Reservable and Reportable (hybrid inheritance) ===");
            Reservable reservable = ada;
            Reportable reportable = ada;

            reservable.reserveBook("B-100");
            reservable.reserveBook("B-200");
            ada.addFee(3.50);
            System.out.println(reportable.generateReport());

            System.out.println();
            Reportable samReport = sam;
            System.out.println(samReport.generateReport());

            // ------------------------------------------------------------
            System.out.println("\n=== 9. Successful and unsuccessful reservations ===");
            tryReserve(sam, "B-300");      // success
            tryReserve(ada, "B-100");      // fail: already reserved
            tryReserve(ada, "  ");         // fail: blank book id
            tryReserve(leo, "B-400");      // fail: member never registered
        }

        /** Helper that reports whether a reservation succeeded or was rejected. */
        private static void tryReserve(Reservable who, String bookId) {
            try {
                who.reserveBook(bookId);
                System.out.println("Reservation SUCCESS for book '" + bookId.trim() + "'");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Reservation FAILED for book '" + bookId.trim() + "': " + e.getMessage());
            }
        }
    }
