package com.btgpactual.fondos.services.impl;

import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.services.IFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundService implements IFundService {

    @Autowired
    FundRepository fundRepository;


    /**
     * Creates a new fund in the database.
     *
     * This method validates the input request before persisting the fund.
     * Required fields are: name and minimum amount.
     *
     * @param request Object containing the data needed to create a new fund.
     * @return Response Message indicating the operation was successful.
     * @throws IllegalArgumentException if the request is null, the name is empty,
     *                                  or the minimum amount is less than or equal to zero.
     */
    @Override
    public Response create(RegisterFunds request) {

        valideRequest(request);

        Fund fund = new Fund();
        fund.setName(request.getName());
        fund.setCategory(request.getCategory());
        fund.setMinimumAmount(request.getMinimumAmount());

        fundRepository.save(fund);

        return new Response("Fondo creado exitosamente");
    }

    /**
     * Validates the input request for creating a fund.
     *
     * Extracting validation into a separate method keeps the main method clean
     * and focused on a single responsibility.
     *
     * @param request Fund registration request to validate.
     * @throws IllegalArgumentException if the request is null or contains invalid data.
     */
    private static void valideRequest(RegisterFunds request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (request.getMinimumAmount() == null ||
                request.getMinimumAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto mínimo debe ser mayor a 0");
        }
    }


    /**
     * Retrieves all registered funds from the database.
     *
     * Returns an empty list if no funds are found.
     * Handles exceptions to encapsulate data access errors.
     *
     * @return List<Fund> List of funds found, or an empty list if none exist.
     * @throws RuntimeException if an unexpected error occurs while accessing the database.
     */
    @Override
    public List<Fund> getAll() {

        try {
            List<Fund> funds = fundRepository.findAll();

            if (funds == null || funds.isEmpty()) {
                return List.of();
            }

            return funds;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los fondos", e);
        }
    }
}
