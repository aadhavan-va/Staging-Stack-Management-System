package net.codejava.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.codejava.model.Instance;


public interface InstanceRepository extends JpaRepository<Instance , Integer>
{
	/*
	 * Used to find the specific Engineer from the Instance class.
	 */
	List<Instance> findByName(String name);
}
