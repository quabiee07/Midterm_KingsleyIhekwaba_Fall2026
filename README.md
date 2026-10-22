# Midterm_KingsleyIhekwaba_Fall2026

## Business rules implemented

| Rule | Where |
|---|---|
| Overloaded `registerMember()` (basic / with type) | `Member` |
| Overloaded `calculateLateFee(int)` and `calculateLateFee(int, double)` | `Librarian` |
| Default late fee is $1.00 per overdue day | `Librarian.DEFAULT_DAILY_LATE_RATE` |
| Reject negative days, invalid rates, invalid registration data | `Librarian`, `Member`, constructors |
| Librarian pay = salary + $2.00 per book processed | `Librarian` |
| Senior librarian pay = librarian pay + seniority allowance | `SeniorLibrarian` |
| Assistant pay = salary + shift allowance | `LibraryAssistant` |
| Counters increment once per successfully created object | `Person.finishConstruction()` |

## Design notes and assumptions

- **Validation rules chosen**  age 1 to 120, phone numbers of 7 to 20
  characters (digits, `+`, `()`, `-`, spaces), positive IDs, positive salary, non-negative allowances.
  A late-fee rate of $0 is allowed (fee waiver).
- **`booksProcessed` must be greater than 0** for a `Librarian`. Change to `< 0` in
  `Librarian.java` if a brand-new librarian with zero books should be allowed.
- **Reservations** require a registered member and reject blank or duplicate book IDs.
- `calculateMonthlyCompensation()` prints the amount (as in the original `Librarian` code).
  `Librarian.getBaseCompensation()` exposes the number so `SeniorLibrarian` can add to it.

## Screenshots
 
- `screenshots/` : This folder contains the demo output from the Main file


# Design Documentation

## 1. Class hierarchy

```
                        Person  (abstract)
                       /        \
                      /          \
        Member                    LibraryStaff  (abstract)
 (implements Reportable,          /                    \
        Reservable)              /                      \
                          Librarian                LibraryAssistant
                              |
                       SeniorLibrarian
```

Interfaces: `Reportable { generateReport() }` and `Reservable { reserveBook(String bookId) }`.

| Class | Extends / implements | Own fields |
|---|---|---|
| `Person` | abstract | `final personId`, `name`, `phoneNumber`, `static personCount` |
| `Member` | `Person`, `Reportable`, `Reservable` | `final memberNumber`, `age`, `membershipType`, `outstandingFees`, `static memberCount` |
| `LibraryStaff` | `Person`, abstract | `final staffId`, `department`, `monthlySalary`, `static staffCount` |
| `Librarian` | `LibraryStaff` | `final dailyIssueLimit`, `final booksProcessed` |
| `SeniorLibrarian` | `Librarian` | `final specialization`, `final seniorityAllowance` |
| `LibraryAssistant` | `LibraryStaff` | `final shiftType`, `final shiftAllowance` |

## 2. OOP concepts and where to find them

| Concept | Where it is shown |
|---|---|
| **Single inheritance** | `Member extends Person` |
| **Multilevel inheritance** | `Person → LibraryStaff → Librarian → SeniorLibrarian` |
| **Hierarchical inheritance** | `Librarian` and `LibraryAssistant` both extend `LibraryStaff` |
| **Multiple inheritance (via interfaces)** | `Member implements Reportable, Reservable` (see section 3) |
| **Hybrid inheritance** | `Member` combines one superclass (`Person`) with two interfaces |
| **Abstract classes and methods** | `Person` (`getRoleDescription()`), `LibraryStaff` (`calculateMonthlyCompensation()`) |
| **Static variables** | `personCount`, `memberCount`, `staffCount` with static getters |
| **Final variables** | `personId`, `memberNumber`, `staffId`, `dailyIssueLimit`, `booksProcessed`, `specialization`, `seniorityAllowance`, `shiftType`, `shiftAllowance` |
| **Method overloading** | `Member.registerMember()` / `registerMember(String)`; `Librarian.calculateLateFee(int)` / `calculateLateFee(int, double)` |
| **Method overriding** | `getRoleDescription()`, `calculateMonthlyCompensation()`, `displayStaffDetails()`, `toString()`, `incrementCounters()` |
| **Runtime polymorphism** | `LibraryStaff[]` in `Main` section 7: one call, different behaviour per object |

## 3. Why Java does not allow multiple class inheritance

Java lets a class extend **only one** class. `class X extends A, B` is a compile error.

The reason is the *diamond problem*. If `A` and `B` both define `calculate()` with different
bodies and `X` inherits from both, `X` has two conflicting versions and the compiler cannot know
which one `x.calculate()` should run. Allowing state (fields) from two parents makes it worse.

**Interfaces are the alternative.** An interface only declares *what* must be done. The class
that implements it supplies the single *how*, so there is nothing to conflict. A class can
implement as many interfaces as it needs:

```java
public class Member extends Person implements Reportable, Reservable { ... }
```

`Member` gets the abilities of both interfaces and still has exactly one parent class. Mixing
one class parent with interfaces is also what makes this **hybrid inheritance**.

## 4. Runtime polymorphism

```java
LibraryStaff[] staff = { librarian, senior, assistant };
for (LibraryStaff s : staff) {
    s.calculateMonthlyCompensation();   // which version runs is decided at runtime
}
```

The reference type is `LibraryStaff`, but Java runs the method of the actual object:
`Librarian`, `SeniorLibrarian` or `LibraryAssistant`. Adding a new staff type needs no change
to this loop.

## 5. How the static counters stay correct

Business rule: a counter goes up **once per successfully created object, and never for a failed one**.

Two traps make a naive design wrong:

1. If `Person`'s constructor does `personCount++`, a subclass that fails *later* validation
   (say, `Librarian` rejects `dailyIssueLimit = 0`) has already been counted.
2. If every class in the chain incremented, a `SeniorLibrarian` would be counted by `Person`,
   `LibraryStaff`, `Librarian` and `SeniorLibrarian`.

The solution:

- Parent constructors **only validate**; they never touch counters.
- Each concrete class calls `finishConstruction(ThisClass.class)` as the **last line** of its
  constructor, which only happens if all of its checks passed.
- `finishConstruction` increments only when `getClass() == constructedClass`, i.e. when the
  class that just finished is the object's real class. A `Librarian` constructor running as part
  of building a `SeniorLibrarian` is therefore ignored.
- It calls the overridable `incrementCounters()`. `Person` bumps `personCount`, `LibraryStaff`
  adds `staffCount`, and `Member` adds `memberCount`.

Result: a `Member` adds 1 person + 1 member, any staff object adds 1 person + 1 staff, and a
failed creation changes nothing. TC5 checks every one of these cases.

## 6. Compensation formulas

| Role | Formula | Example from `Main` |
|---|---|---|
| Librarian | salary + $2.00 × booksProcessed | 4000 + 120 × 2 = **$4,240** |
| Senior librarian | salary + $2.00 × booksProcessed + seniorityAllowance | 5000 + 200 × 2 + 500 = **$5,900** |
| Library assistant | salary + shiftAllowance | 2800 + 150 = **$2,950** |

## 7. Error handling

- Bad object data (names, ages, IDs, salaries, allowances, membership type) throws the checked
  `InvalidPersonException`, so callers must handle it.
- Bad method arguments (negative days, invalid rates, blank or duplicate book IDs, invalid fee
  amounts) throw `IllegalArgumentException`.
- Reserving a book before registering throws `IllegalStateException`.