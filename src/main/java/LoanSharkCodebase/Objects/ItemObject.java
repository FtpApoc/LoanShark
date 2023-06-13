package LoanSharkCodebase.Objects;

import javafx.scene.control.Button;

public class ItemObject {
    private String Name;
    private Integer Copies;
    private String Genre;
    private Integer ReadLevel;
    private Button BtnViewItemPage;

    public void itemObject(){};

    public ItemObject(String name, Integer copies, String genre, Integer readlevel, Button btnViewItemPage){
        this.Name = name;
        this.Copies = copies;
        this.Genre = genre;
        this.ReadLevel = readlevel;
        this.BtnViewItemPage = btnViewItemPage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getCopies() {
        return Copies;
    }

    public void setCopies(Integer copies) {
        Copies = copies;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Integer getReadLevel() {
        return ReadLevel;
    }

    public void setReadLevel(Integer readLevel) {
        ReadLevel = readLevel;
    }

    public Button getBtnViewItemPage() {
        return BtnViewItemPage;
    }

    public void setBtnViewItemPage(Button btnViewItemPage) {
        BtnViewItemPage = btnViewItemPage;
    }
}
