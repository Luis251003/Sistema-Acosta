package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.RucDTOResponse;

public interface IRucService {
    RucDTOResponse consultarRUC(String ruc);
}
