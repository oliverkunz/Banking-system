package frontend.administration;

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
