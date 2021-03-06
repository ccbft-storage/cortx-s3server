/*
 * Copyright (c) 2020 Seagate Technology LLC and/or its Affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For any questions about this software or licensing,
 * please email opensource@seagate.com or cortx-questions@seagate.com.
 *
 */

package com.seagates3.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class KeyGenUtil {

  private
   static final int SALT_LENGTH = 4;
    /*
     * TODO
     * UserId and userAccessKeyIds are generated from uuid encoding it to base 64.
     * Replace it with a better approach in the next release.
     */

    /**
     * Generate a new unique Id. The first character of the ID should not be a
     * hyphen or an underscore.
     *
     * @return UserId
     */
    public static String createUserId() {
        String id = BinaryUtil.base64UUID().substring(0, 22);
        if (id.startsWith("-") || id.startsWith("_")) {
            id = getRandomChar() + id.substring(1);
        }

        return id;
    }

    /**
     * Generate a new Access Key Id for the user. The first character of the ID
     * should not be a hyphen or an underscore.
     *
     * TODO Since the Access Key Id is generated by encoding uuid to Base 64,
     * the length equals to 22 characters while AWS access key Ids are 20
     * characters. Improve the Access Key generator to generate 20 character
     * long access key id. The access key id can be generated based on
     * geographical location or other parameters.
     *
     * @return AccessKeyId
     */
    public static String createUserAccessKeyId() {
        String id = BinaryUtil.base64UUID().substring(0, 22);
        if (id.startsWith("-") || id.startsWith("_")) {
            id = getRandomChar() + id.substring(1);
        }

        return id;
    }

    /**
     * Generate a secret key for the user.The first character of the ID should
     * not be a hyphen or an underscore.
     *
     * @return SecretKey
     */
    public static String generateSecretKey() {
        byte[] digest = BinaryUtil.hashSHA256(BinaryUtil.getRandomUUIDAsByteArray());

        return BinaryUtil.encodeToBase64String(digest).substring(0, 40);
    }

    /**
     * Generate a new session Id for the temporary user (federated user).
     *
     * @param strToEncode
     * @return SessionId
     */
    public static String createSessionId(String strToEncode) {
        String id = BinaryUtil.base64EncodedHash(strToEncode);
        if (id.startsWith("-") || id.startsWith("_")) {
            id = getRandomChar() + id.substring(1);
        }

        return id;
    }

    /**
     * Return Base 64 encoded UUID.
     *
     * @return Unique ID.
     */
    public static String createId() {
        String id = BinaryUtil.base64UUID().substring(0, 22);
        if (id.startsWith("-") || id.startsWith("_")) {
            id = getRandomChar() + id.substring(1);
        }

        return id;
    }

    /**
     * Generate random character
     * @return random character in the range A..Z
     */
    private static char getRandomChar() {
       char c = (char)(new Random().nextInt(26) + 65);
        return c;
    }

    /**
     * This method generates SHA-1 hash of give string
     * @param String
     * @return
     * @throws NoSuchAlgorithmException
     */
   public
    static String generateSSHA(String text) throws NoSuchAlgorithmException {

      SecureRandom secureRandom = new SecureRandom();
      byte[] salt = new byte[SALT_LENGTH];
      secureRandom.nextBytes(salt);

      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(text.getBytes());
      crypt.update(salt);
      byte[] hash = crypt.digest();

      byte[] hashPlusSalt = new byte[hash.length + salt.length];
      System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
      System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

      return new StringBuilder()
          .append("{SSHA}")
          .append(Base64.getEncoder().encodeToString(hashPlusSalt))
          .toString();
    }

    /**
     * Geneate a new canonical id for Account.
     * canonical id should be,
     * 1. AlphaNumeric
     * 2. Length should be 64 characters
     * 3. Lower case letters
     * @return canonicalId
     */
   public
    static String createCanonicalId() {
      String uuid1 = BinaryUtil.getAlphaNumericUUID();
      String canonical_id = uuid1 + BinaryUtil.getAlphaNumericUUID();
      return canonical_id.toLowerCase();
    }

    /**
     * Generate a new user id for IAM user.
     * user id should be,
     * 1. Alphanumeric
     * 2. Length should be 21 i.e USER_ID_PREFIX + userId
     * 3. Upper case letters
     * e.g AIDA5KZQJXPTROAIAKCKO
     * where User_ID_Prefix i.e. "AIDA" is used to represent IAM user id
     * @return UserId
     */
   public
    static String createIamUserId() {
      String id = BinaryUtil.getAlphaNumericUUID().substring(0, 17);
      // PREFIX "AIDA" will be added to this userId in Controller class
      return id.toUpperCase();
    }

    /**
     * Generate a new account id.
     * account id should be,
     * 1. Contains digits only
     * 2. Length should be 12
     * e.g 234755435308
     * @return AccountId
     */
   public
    static String createAccountId() {
      long max = 999999999999L;
      long min = 100000000000L;
      long account_id = min + (long)(Math.random() * ((max - min) + 1));
      return String.valueOf(account_id);
    }
}
