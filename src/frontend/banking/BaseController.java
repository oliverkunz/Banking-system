package frontend.banking;

//basic controller structure that will be inherited to all administration controllers
public abstract class BaseController {
	EBankingMain main = null;

	public BaseController(EBankingMain main) {
		this.main = main;
	}

	public EBankingMain getMain() {
		return main;
	}

	public void setMain(EBankingMain main) {
		this.main = main;
	}

	public abstract void onNavigate(String route);

}
