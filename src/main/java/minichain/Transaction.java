package minichain;

import java.util.UUID;

public record Transaction(String id, String from, String to, int amount) {

    // Sender used for mining rewards.
    public static final String NETWORK = "network";

    public Transaction(String from, String to, int amount) {
        this(UUID.randomUUID().toString(), from, to, amount);
    }

    @Override
    public String toString() {
        return from + " -> " + to + ": " + amount;
    }
}
