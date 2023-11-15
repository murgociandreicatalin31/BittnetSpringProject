package ro.ctrln.entities;

import lombok.Getter;
import lombok.Setter;
import ro.ctrln.enums.Currencies;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "valid")
    private boolean valid;

    @Enumerated(EnumType.STRING)
    private Currencies currency;
}
