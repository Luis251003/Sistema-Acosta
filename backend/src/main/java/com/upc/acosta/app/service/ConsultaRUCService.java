package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.RucDTOResponse;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.ExternalServiceException;
import com.upc.acosta.app.error.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static com.upc.acosta.app.security.TokenJwtConfig.*;

@Service
public class ConsultaRUCService {

    private final RestClient restClient;
    private final String KEY;

    public ConsultaRUCService(
            RestClient.Builder builder,
            @Value("${app.chequea.pe.url}") String url,
            @Value("${app.chequea.pe.api-key}") String key
    ) {
        this.restClient = builder
                .baseUrl(url)
                .build();

        this.KEY = key;
    }

    public RucDTOResponse consultarRUC(String ruc){
        try{
            return restClient
            .get()
            .uri("/api/v1/ruc/{ruc}",ruc)
            .header(HEADER_AUTHORIZATION,PREFIX_TOKEN + KEY)
            .retrieve()
            .onStatus(
                    status -> status.value() == 400,
                    (request, response) -> {
                        throw new BadRequestException(
                                "El RUC ingresado no es válido"
                        );
                    }
            )
            .onStatus(
                    status -> status.value() == 401,
                    (request, response) -> {
                        throw new ExternalServiceException(
                                "Error de autenticación con la API de Chequea"
                        );
                    }
            )
            .onStatus(
                    status -> status.value() == 404,
                    (request, response) -> {
                        throw new NotFoundException(
                                "El RUC " + ruc + " no fue encontrado"
                        );
                    }
            )
            .onStatus(
                    status -> status.value() == 429,
                    (request, response) -> {
                        throw new ExternalServiceException(
                                "Se excedió el límite de consultas de la API de RUC"
                        );
                    }
            )
            .onStatus(
                    HttpStatusCode::is5xxServerError,
                    (request, response) -> {
                        throw new ExternalServiceException(
                                "La API de RUC no está disponible actualmente"
                        );
                    }
            )
            .body(RucDTOResponse.class);
        }catch(RestClientException e){
            throw new ExternalServiceException(
                    "No fue posible comunicarse con la API de RUC"
            );
        }
    }
    
}
