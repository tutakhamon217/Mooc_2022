package fpt.capstone.payroll;

public class ClassRoomNotFoundException extends RuntimeException {
    public ClassRoomNotFoundException(Integer id) {
        super("Class room is not existed");
    }
}
