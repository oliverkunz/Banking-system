package backend.data;

public class PrivateAccount extends Account {
    public boolean withdraw(double amount) {
	return true;
    }

    public boolean overdrawInterests() {
	return true;
    }
}
