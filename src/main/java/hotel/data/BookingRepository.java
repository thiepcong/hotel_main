package hotel.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Booking;
import hotel.model.Client;
import hotel.model.Room;


//BookingRepository được sử dụng trong việc tương tác với cơ sở dữ liệu để lấy và lưu thông tin đặt trước.
//kế thừa crudrepository thao tác CRUD (Create, Read, Update, Delete) với đối tượng Booking.

//CrudRepository<Booking, Long> BookingRepository sẽ hoạt động với các đối tượng Booking và sử dụng kiểu dữ liệu Long 
//cho thuộc tính ID của Booking.
@Repository
public interface BookingRepository extends CrudRepository<Booking, Long>{

	//phương thức findAllByClient(Client client) để tìm kiếm tất cả các đặt trước(booking) của 1 khách hàng (client) cụ thể
	List<Booking> findAllByClient(Client client);

	//phương thức findAllByRoom(Room room) để tìm kiếm tất cả các đặt trước(booking) của 1 phòng (room) cụ thể
	List<Booking> findAllByRoom(Room room);

}
