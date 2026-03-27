package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.ResponseFunds;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;

import java.util.List;

public interface IInvestment {



    public Response cancellation(String email,CancellationRequest request);

    public Response subscribe (String email, SubscriptionRequest request) throws Exception;

    public List<Transaction> getAll(String email);

}
