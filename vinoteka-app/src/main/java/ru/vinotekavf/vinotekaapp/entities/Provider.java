package ru.vinotekavf.vinotekaapp.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private Set<Position> positions;

    private String name;
    private String phone;
    private String managerName;
    private boolean isActive;

    public Provider() {
    }

    public Provider(String name, String phone, String managerName) {
        this.name = name;
        this.phone = phone;
        this.managerName = managerName;
        this.isActive = true;
    }
}
