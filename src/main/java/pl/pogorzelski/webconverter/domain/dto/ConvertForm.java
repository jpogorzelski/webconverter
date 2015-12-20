package pl.pogorzelski.webconverter.domain.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class ConvertForm {

    @NotEmpty
    private MultipartFile file;

    @NotEmpty
    private String name = "";

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConvertForm{" +
                "name='" + name + '\'' +
                '}';
    }
}
