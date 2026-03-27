package com.btgpactual.fondos.repositories;

import com.btgpactual.fondos.models.document.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TransactionRespository extends MongoRepository <Transaction, String> {

    Optional <Transaction> getAll();
}
