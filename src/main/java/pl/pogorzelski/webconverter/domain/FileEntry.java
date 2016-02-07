package pl.pogorzelski.webconverter.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Created by kuba on 11/23/15.
 */
@Entity
public class FileEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String extension;
    @ManyToOne
    private User owner;

    @Lob
    private File file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FileEntry{" + file.getName() + '}';
    }
}
