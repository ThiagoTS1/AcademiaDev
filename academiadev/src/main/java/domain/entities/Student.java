package domain.entities;

public class Student extends User {
    private SubscriptionPlan subscriptionPlan;

    public Student(String name, String email, SubscriptionPlan subscriptionPlan) {
        super(name, email);
        this.subscriptionPlan = subscriptionPlan;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void changeSubscriptionPlan(SubscriptionPlan newPlan) {
        this.subscriptionPlan = newPlan;
    }

    public boolean canEnroll(int currentActiveEnrollments) {
        return subscriptionPlan.canEnroll(currentActiveEnrollments);
    }
}

