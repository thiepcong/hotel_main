package hotel.data;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Booking;
import hotel.model.Client;
import hotel.model.Room;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long>{

	//phương thức findAllByClient(Client client) để tìm kiếm tất cả các đặt trước(booking) của 1 khách hàng (client) cụ thể
	List<Booking> findAllByClient(Client client);

	//phương thức findAllByRoom(Room room) để tìm kiếm tất cả các đặt trước(booking) của 1 phòng (room) cụ thể
	List<Booking> findAllByRoom(Room room);


}
