package frontend.administration;

public class AdminPair<T, K> {
    private T value1;
    private K value2;

    private AdminPair(T value1, K value2) {
	this.value1 = value1;
	this.value2 = value2;
    }

    public static <T, K> AdminPair<T, K> of(T value1, K value2) {
	return new AdminPair(value1, value2);
    }

    public T getFirstValue() {
	return this.value1;
    }

    public K getSecondValue() {
	return this.value2;
    }
}
