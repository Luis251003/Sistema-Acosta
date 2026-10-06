package com.upc.acosta.app.validator;

public final class RucValidator {
    private RucValidator() {
    }

    public static boolean validar(String ruc) {

        if (ruc == null || !ruc.matches("\\d{11}")) {
            return false;
        }

        if (!ruc.matches("^(10|15|17|20)\\d{9}$")) {
            return false;
        }

        int suma = 0;
        int multiplicador = 2;

        for (int i = 9; i >= 0; i--) {

            int digito = ruc.charAt(i) - '0';

            suma += digito * multiplicador;

            multiplicador++;

            if (multiplicador > 7) {
                multiplicador = 2;
            }
        }

        int calculado = 11 - (suma % 11);

        if (calculado == 10 || calculado == 11) {
            calculado = 0;
        }

        int verificador = ruc.charAt(10) - '0';

        return calculado == verificador;
    }
}
