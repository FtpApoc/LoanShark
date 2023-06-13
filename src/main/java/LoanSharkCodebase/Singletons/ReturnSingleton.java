package LoanSharkCodebase.Singletons;

public class ReturnSingleton {
    private static final ReturnSingleton returnInstance = new ReturnSingleton();

    private ReturnSingleton(){}

    //This will return the only existing existence of the singleton
    public static ReturnSingleton getInstance(){
        return returnInstance;
    }
    String ReturnID = "";

    //
    public String getReturnID(){return ReturnID;};

    //
    public void setReturnID(String incomingID){
        ReturnID = incomingID;
    }
}
