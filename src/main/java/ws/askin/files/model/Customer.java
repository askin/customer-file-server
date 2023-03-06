package ws.askin.files.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {
    private @Id
    @GeneratedValue Long id;
    private String fullName;
    private boolean isDeleted = false;

    public Customer() {}

    public Customer(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Customer(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
