package LoanSharkCodebase.Objects;

import javafx.scene.control.Button;
import java.sql.Date;

public class CustomerLoans {

    private String Name;
    private Date DateDue;
    private String Overdue;
    private String EventID;
    private Button returnButton;

    public CustomerLoans(){
        this.Name = "";
        this.DateDue = null;
        this.Overdue = null;
        this.EventID = null;
    };

    public CustomerLoans(String Name, Date DateDue, String Overdue, String EventID,Button passButton){
        this.Name = Name;
        this.DateDue = DateDue;
        this.Overdue = Overdue;
        this.EventID = EventID;
        this.returnButton = passButton;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getDateDue() {
        return DateDue;
    }

    public void setDateDue(Date dateDue) {
        DateDue = dateDue;
    }

    public String getOverdue() {
        return Overdue;
    }

    public void setOverdue(String overdue) {
        Overdue = overdue;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setBtnReturn(Button button) {
        returnButton = button;}

    private void Return(){}

}
