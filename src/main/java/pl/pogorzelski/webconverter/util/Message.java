package pl.pogorzelski.webconverter.util;

/**
 * @author Kuba
 */
public class Message {

    private Type type = new Type();
    private String text = "";

    public Message(String cssClass, String text) {
        this.type.setCssClass(cssClass);
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
