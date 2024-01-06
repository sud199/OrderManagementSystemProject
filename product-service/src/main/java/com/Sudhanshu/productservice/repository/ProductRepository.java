package com.Sudhanshu.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Sudhanshu.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
