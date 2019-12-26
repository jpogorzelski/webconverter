package pl.pogorzelski.webconverter.util;

/**
 * @author Kuba
 */
public class Type {
    private String cssClass = "alert alert-";

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass += cssClass;
    }
}
