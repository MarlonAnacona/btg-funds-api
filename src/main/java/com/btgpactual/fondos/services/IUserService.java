package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.RegisterRequest;
import com.btgpactual.fondos.models.dto.Response;

import java.util.List;

public interface IUserService {

    public Response create (RegisterRequest request);

    public List<User> get ();
}
