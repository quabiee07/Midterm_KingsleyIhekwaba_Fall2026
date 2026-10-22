public abstract class Person {
    private static int personCount = 0;

    private final int personId;
    private final String name;
    private final String phoneNumber;

    protected Person(int personId, String name, String phoneNumber) throws Exception {
        if (personId <= 0) {
            throw new InvalidPersonException("Person id must be positive");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPersonException("Name cannot be empty");
        }
        if (phoneNumber == null || !phoneNumber.matches("[0-9+()\\-\\s]{7,20}")) {
            throw new InvalidPersonException("Phone number is invalid");
        }
        this.personId = personId;
        this.name = name.trim();
        this.phoneNumber = phoneNumber.trim();
        // The person count field is NOT incremented here: because a subclass may still fail validation.
        // I implemented finishConstruction() to fix this.
    }

    /**
     * Each concrete class calls this as the LAST line of its constructor.
     * Counters go up only when the class being constructed is the object's actual
     * class, i.e. the whole constructor chain succeeded, so each object counts once.
     */
    protected final void finishConstruction(Class<?> constructedClass) {
        if (getClass() == constructedClass) {
            incrementCounters();
        }
    }

    /** Subclasses with their own counters will override this and call super. */
    protected void incrementCounters() {
        personCount++;
    }

    public static int getPersonCount() {
        return personCount;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public abstract String getRoleDescription();

    public void displayBasicDetails() {
        System.out.println("Person ID: " + personId);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phoneNumber);
    }

    @Override
    public String toString() {
        return getRoleDescription() + " [ID=" + personId + ", Name=" + name + ", Phone=" + phoneNumber + "]";
    }
}