package com.btgpactual.fondos.repositories;

import com.btgpactual.fondos.models.document.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundRepository  extends MongoRepository<Fund, String> {

    Optional<Fund> findById();

}
