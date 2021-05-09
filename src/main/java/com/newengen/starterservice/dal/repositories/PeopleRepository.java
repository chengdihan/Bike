package com.newengen.starterservice.dal.repositories;

import com.newengen.starterservice.dal.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<Person, Long> {
}
