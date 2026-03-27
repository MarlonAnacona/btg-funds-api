package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;

import java.util.List;

public interface IFundService {

    public Response create (RegisterFunds request);

    public List<Fund> getAll ();
}
