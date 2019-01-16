package frontend.atm;

public class ATMPair<T, K> {
    private T value1;
    private K value2;

    private ATMPair(T value1, K value2) {
	this.value1 = value1;
	this.value2 = value2;
    }

    public static <T, K> ATMPair<T, K> of(T value1, K value2) {
	return new ATMPair(value1, value2);
    }

    public T getFirstValue() {
	return this.value1;
    }

    public K getSecondValue() {
	return this.value2;
    }

}