package frontend.atm;

public abstract class BaseControllerATM {
	Main main = null;

    public BaseControllerATM(Main main) {
	this.main = main;
    }

    public Main getMain() {
	return main;
    }

    public void setMain(Main main) {
	this.main = main;
    }

    public abstract void onNavigate(String route);

}
