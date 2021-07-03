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

    @OneToMany(mappedBy = "provider")
    private Set<Position> positions;

    private String name;
    private String phone;
    private String managerName;
}
