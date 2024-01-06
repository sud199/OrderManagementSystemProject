package com.Sudhanshu.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Sudhanshu.productservice.dto.ProductRequest;
import com.Sudhanshu.productservice.dto.ProductResponse;
import com.Sudhanshu.productservice.model.Product;
import com.Sudhanshu.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	
	
	
	public void createProduct(ProductRequest productRequest) {
		
		Product product = Product.builder()
				.name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();
		
		productRepository.save(product);
		log.info("Product {} is saved" , product.getId());
		
				}



	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this:: mapToProductRepsonse).toList();
		
		
	}



	private ProductResponse mapToProductRepsonse(Product product) {
		return ProductResponse.builder().id(product.getId()).name(product.getName()).
				description(product.getDescription()).price(product.getPrice()).build();
	}

}
