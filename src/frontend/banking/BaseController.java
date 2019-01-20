package frontend.banking;

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
