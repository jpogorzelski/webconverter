package pl.pogorzelski.webconverter.domain.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class NewConverterForm {
    @NotEmpty
    private String sourceFormat = "";
    @NotEmpty
    private String targetFormat = "";

    @NotEmpty
    private String sourceCode = "";


    public String getSourceFormat() {
        return sourceFormat;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }


    @Override
    public String toString() {
        return "NewConverterForm{" +
                "sourceFormat='" + sourceFormat + '\'' +
                ", targetFormat='" + targetFormat + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                '}';
    }
}
