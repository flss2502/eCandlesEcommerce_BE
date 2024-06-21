package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.id != :addressId")
    void updateDefaultAddressForUser(@Param("userId") Long userId, @Param("addressId") Long addressId);

    List<Address> findByUserId(Long userId);
}
