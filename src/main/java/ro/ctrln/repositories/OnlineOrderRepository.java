package ro.ctrln.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.ctrln.entities.OnlineOrder;

@Repository
public interface OnlineOrderRepository extends CrudRepository<OnlineOrder, Long> {

    OnlineOrder findOneById(Long orderId);
}
