package hotel.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Bill;

//BillRepository được sử dụng trong việc tương tác với cơ sở dữ liệu để lấy và lưu thông tin hóa đơn.
//kế thừa crudrepository thao tác CRUD (Create, Read, Update, Delete) với đối tượng Bill.

//CrudRepository<Bill, Long> BillRepository sẽ hoạt động với các đối tượng Bill và sử dụng kiểu dữ liệu Long 
//cho thuộc tính ID của Bill.
@Repository
public interface BillRepository extends CrudRepository<Bill, Long>{

}
