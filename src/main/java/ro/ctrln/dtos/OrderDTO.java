package ro.ctrln.dtos;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {

    private List<OrderProductDTO> products;
}
