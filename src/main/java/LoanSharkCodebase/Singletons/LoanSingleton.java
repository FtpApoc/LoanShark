package LoanSharkCodebase.Singletons;

public class LoanSingleton {
    private static final LoanSingleton loanInstance = new LoanSingleton();

    private LoanSingleton(){};

    public static LoanSingleton getInstance(){return loanInstance;}
    String LoanID = "";

    public String getLoanID(){return LoanID;}

    public void setLoanID(String incomingID){LoanID = incomingID;}
}
