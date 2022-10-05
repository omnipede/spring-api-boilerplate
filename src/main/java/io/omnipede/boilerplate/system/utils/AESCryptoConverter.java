package io.omnipede.boilerplate.system.utils;


import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * JPA 를 이용해서 db CRUD 시
 * 보안이 필요한 사용자 정보 문자열을 암복호화하는 converter
 */
@Component
@Converter
public class AESCryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key KEY;

    /**
     * 설정 파일에서 비밀키를 읽어와 KEY 변수를 초기화해준다.
     * @param AESCryptoConfig configuration property 객체
     */
    @Autowired
    public void setKey(AESCryptoConfig AESCryptoConfig) {
        AESCryptoConverter.KEY = generateAESKey(AESCryptoConfig.getAesKey());
    }

    /**
     * Attribute 를 암호화하여 column 데이터로 맵핑하는 메소드
     * HEX(AES_ENCRYPT(column))
     * @param attribute JPA entity field
     * @return Encrypted data
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
            return new String(Hex.encodeHex(cipher.doFinal(attribute.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Column 데이터를 복호화하여 attribute 에 맵핑하는 메소드
     * AES_DECRYPT(UNHEX(column))
     * @param column DB column data
     * @return Decrypted data
     */
    @Override
    public String convertToEntityAttribute(String column) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, KEY);
            return new String(cipher.doFinal(Hex.decodeHex(column.toCharArray())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES 암호화시 사용할 키를 생성하는 메소드
     * @return AES 암호화 키
     */
    private SecretKeySpec generateAESKey(String key) {

        final byte[] finalKey = new byte[16];
        int i = 0;
        for(byte b: key.getBytes(StandardCharsets.UTF_8))
            finalKey[i++%16] ^= b;
        return new SecretKeySpec(finalKey, "AES");
    }
}
