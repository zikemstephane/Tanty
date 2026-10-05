package com.NTFOODS.Api_tanty.shared.infrastructure.otp;

import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

/** Levée lorsqu'un code OTP est absent, expiré, déjà utilisé ou incorrect. */
public class OtpInvalideException extends RegleMetierException {
    public OtpInvalideException(String message) {
        super(message);
    }
}
