package net.codejava.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.codejava.model.Product;

public interface ProductReprository extends JpaRepository<Product , Integer>{
	

}
