package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.repositories.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService implements  IFundService{

    @Autowired
    FundRepository fundRepository;


    @Override
    public Response create(RegisterFunds request) {
        Fund fund= new Fund();
        fund.setName(request.getName());
        fund.setCategory(request.getCategory());
        fund.setMinimumAmount(request.getMinimumAmount());

        fundRepository.save(fund);
        return  new Response("Fondo creado exitosamente");
    }

    @Override
    public List<Fund> getAll() {

        return  fundRepository.findAll();
    }
}
