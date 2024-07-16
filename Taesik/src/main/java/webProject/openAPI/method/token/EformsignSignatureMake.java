package com.forcs.eformsign.webhook.openAPI.method.token;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EformsignSignatureMake {

    public static String secret = "";

    /**
     *
     * @return
     */
    public Map<String, Object> signMake() {
        //eformsign_signature 생성
        Map<String, Object> result = new HashMap<>();
        try {
            String privateKeyHexStr = secret;

            KeyFactory keyFact = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec psks8KeySpec = new PKCS8EncodedKeySpec(new BigInteger(privateKeyHexStr, 16).toByteArray());
            PrivateKey privateKey = keyFact.generatePrivate(psks8KeySpec);

            //execution_time - 서버 현재 시간
            long execution_time = new Date().getTime();
            String execution_time_str = String.valueOf(execution_time);

            //eformsign_signature 생성
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initSign(privateKey);
            ecdsa.update(execution_time_str.getBytes(StandardCharsets.UTF_8));
            String eformsign_signature = new BigInteger(ecdsa.sign()).toString(16);


            result.put("execution_time", execution_time);
            result.put("eformsign_signature", eformsign_signature);

        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException |
                 InvalidKeySpecException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

}
