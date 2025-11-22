package domain.entities;

public class BasicPlan extends SubscriptionPlan {
    private static final int MAX_ENROLLMENTS = 3;

    public BasicPlan() {
        super("BasicPlan");
    }

    @Override
    public boolean canEnroll(int currentActiveEnrollments) {
        return currentActiveEnrollments < MAX_ENROLLMENTS;
    }

    @Override
    public int getMaxEnrollments() {
        return MAX_ENROLLMENTS;
    }
}

