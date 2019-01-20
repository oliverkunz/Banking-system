package frontend.atm;

//basic controller structure that will be inherited to all administration controllers
public abstract class BaseControllerATM {
	ATMMain main = null;

	public BaseControllerATM(ATMMain main) {
		this.main = main;
	}

	public ATMMain getMain() {
		return main;
	}

	public void setMain(ATMMain main) {
		this.main = main;
	}

	public abstract void onNavigate(String route);

}
