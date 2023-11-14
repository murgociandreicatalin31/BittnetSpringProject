package ro.ctrln.dtos;

import lombok.Data;
import ro.ctrln.enums.Currencies;

@Data
public class ProductDTO {

    private long id;
    private String code;
    private String description;
    private double price;
    private int stock;
    private boolean valid;
    private Currencies currency;
}
