package LoanSharkCodebase.Singletons;

public class CustomerSingleton {
    private static final CustomerSingleton customerInstance = new CustomerSingleton();

    private CustomerSingleton(){}

    //This will return the only existing existence of the singleton
    public static CustomerSingleton getInstance(){
        return customerInstance;
    }
    String CustomerID = "";

    //
    public String getCustomerID(){
        return CustomerID;
    };

    //
    public void setCustomerID(String incomingID){
        CustomerID = incomingID;
    }

}
