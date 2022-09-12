package fpt.capstone.repository;

import fpt.capstone.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    @Query("SELECT new fpt.capstone.vo.UserVo  (u.id, u.login,u.fullName,u.phoneNumber,u.imageUrl,u.actived) from User u where u.login = ?1 and u.passwordHash = ?2")
//    UserVo findUserByLoginAndPasswordHash(String login, String passwordHash);

    User findByLogin(String login);

    @Override
    <S extends User> S save(S entity);

}
