package net.codejava.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "instance", catalog = "employeedb")
public class Instance {
	@Id
    private int id;
    private String name;
    @Value("${some.key:false}")
    private boolean state;

    public boolean isState() {
        return state;
    }
    
    public void setState(boolean state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
