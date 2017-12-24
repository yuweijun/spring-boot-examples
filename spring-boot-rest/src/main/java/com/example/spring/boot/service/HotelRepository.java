package com.example.spring.boot.service;

import com.example.spring.boot.domain.City;
import com.example.spring.boot.domain.Hotel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "hotels", path = "hotels")
interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

    Hotel findByCityAndName(City city, String name);

}