package com.example.gestox.dao;

import com.example.gestox.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
