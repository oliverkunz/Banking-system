package frontend.administration;

//basic controller structure that will be inherited to all administration controllers
public abstract class AdminBaseController {
	AdminMain adminMain = null;

	public AdminBaseController(AdminMain adminMain) {
		this.adminMain = adminMain;
	}

	public AdminMain getAdminMain() {
		return adminMain;
	}

	public void setAdminMain(AdminMain adminMain) {
		this.adminMain = adminMain;
	}

	public abstract void onNavigate(String route);

}
