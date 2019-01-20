package frontend.common;

// class for changing scenes forth and back
public class Pair<T, K> {
	private T value1;
	private K value2;

	private Pair(T value1, K value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	public static <T, K> Pair<T, K> of(T value1, K value2) {
		return new Pair<T, K>(value1, value2);
	}

	public T getFirstValue() {
		return this.value1;
	}

	public K getSecondValue() {
		return this.value2;
	}
}