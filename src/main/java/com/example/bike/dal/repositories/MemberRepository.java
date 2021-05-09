package com.example.bike.dal.repositories;

import com.example.bike.dal.models.Bike;
import com.example.bike.dal.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
