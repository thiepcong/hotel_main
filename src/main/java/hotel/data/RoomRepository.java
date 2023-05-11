package hotel.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Room;


@Repository
public interface RoomRepository extends CrudRepository<Room, Long>{

}
