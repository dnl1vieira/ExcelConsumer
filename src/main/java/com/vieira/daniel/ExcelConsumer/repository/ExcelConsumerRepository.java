package com.vieira.daniel.ExcelConsumer.repository;

import com.vieira.daniel.ExcelConsumer.model.ExcelConsumer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelConsumerRepository extends CrudRepository<ExcelConsumer, Long> {
}
