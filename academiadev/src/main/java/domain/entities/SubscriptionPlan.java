package domain.entities;

public abstract class SubscriptionPlan {
    protected String name;

    public SubscriptionPlan(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean canEnroll(int currentActiveEnrollments);
    public abstract int getMaxEnrollments();
}

