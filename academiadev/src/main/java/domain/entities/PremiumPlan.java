package domain.entities;

public class PremiumPlan extends SubscriptionPlan {
    public PremiumPlan() {
        super("PremiumPlan");
    }

    @Override
    public boolean canEnroll(int currentActiveEnrollments) {
        return true; // Unlimited enrollments
    }

    @Override
    public int getMaxEnrollments() {
        return Integer.MAX_VALUE;
    }
}

