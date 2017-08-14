package ru.iteco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatBlockRepository extends JpaRepository<FlatBlock, Integer> {
    FlatBlock findOneByName(String name);
}
