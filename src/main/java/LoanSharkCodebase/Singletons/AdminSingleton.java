package LoanSharkCodebase.Singletons;

public class AdminSingleton {
    private static final AdminSingleton AdminInstance = new AdminSingleton();
    private Boolean AdminBool = false;

    private AdminSingleton(){}

    //This will return the only existing existence of the singleton
    public static AdminSingleton getInstance(){
        return AdminInstance;
    }

    //used to check whether the box should be visible
    public Boolean getAdminBool(){
        return AdminBool;
    }

    //if admin is true on login, call this to set admin bool as true when entering the login session
    public void enableAdminBool(){
        this.AdminBool = true;
    }

    //only use this when logging out of the session.
    public void disableAdminBool(){
        this.AdminBool = false;
    }
}
