package org.jss.prototype.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GroupFormPk.class)
@Table(name = "prototype_concept")
public class Concept {

    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "json")
    private String json;
    @Id
    @Column(name = "category")
    private String category;

    public Concept(String name, String json, String category) {
        this.name=name;
        this.json=json;
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}






