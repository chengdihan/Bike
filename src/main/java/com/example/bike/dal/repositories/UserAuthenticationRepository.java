package com.example.bike.dal.repositories;

import com.example.bike.dal.models.UserAuthentication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {

  List<UserAuthentication> findByUserID(Long userID);

  List<UserAuthentication> findByUserName(String userName);

  List<UserAuthentication> findByUserEmail(String userEmail);

  List<UserAuthentication> findByUserNameOrUserEmail(String userName, String userEmail);

}