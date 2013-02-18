package org.jss.prototype.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GroupFormPk implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String name;
    String category;



}
