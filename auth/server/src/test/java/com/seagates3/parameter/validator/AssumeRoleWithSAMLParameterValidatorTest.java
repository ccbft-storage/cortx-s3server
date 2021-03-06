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

package com.seagates3.parameter.validator;

import com.seagates3.parameter.validator.AssumeRoleWithSAMLParameterValidator;
import java.util.Map;
import java.util.TreeMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AssumeRoleWithSAMLParameterValidatorTest {

    AssumeRoleWithSAMLParameterValidator validator;
    Map requestBody;

    public AssumeRoleWithSAMLParameterValidatorTest() {
        validator = new AssumeRoleWithSAMLParameterValidator();
    }

    @Before
    public void setUp() {
        requestBody = new TreeMap();
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - Principal ARN is not provided
     * (also covers invalid Principal ARN).
     */
    @Test
    public void Create_PrincipalArnNull_False() {
        assertFalse(validator.isValidCreateParams(requestBody));
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - Role ARN is not provided (also
     * covers invalid Role ARN).
     */
    @Test
    public void Create_RoleArnNull_False() {
        requestBody.put("PrincipalArn", "arn:seagate:iam::myorg:test");
        assertFalse(validator.isValidCreateParams(requestBody));
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - SAML Assertion is not
     * provided(also covers invalid SAML Assertion).
     */
    @Test
    public void Create_SAMLAssertionNull_False() {
        requestBody.put("PrincipalArn", "arn:seagate:iam::myorg:test");
        requestBody.put("RoleArn", "arn:seagate:iam::role/test");
        assertFalse(validator.isValidCreateParams(requestBody));
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - Duration Seconds is invalid.
     */
    @Test
    public void Create_InvalidDurationSeconds_False() {
        requestBody.put("DurationSeconds", "800");
        assertFalse(validator.isValidCreateParams(requestBody));
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - Invalid policy
     */
    @Test
    public void Create_InvalidPolicy_False() {
        requestBody.put("DurationSeconds", "900");
        requestBody.put("Policy", "");
        assertFalse(validator.isValidCreateParams(requestBody));
    }

    /**
     * Test AssumeRoleWithSAML#isValidCreateParams. Case - Valid inputs.
     */
    @Test
    public void Create_ValidInputs_False() {
        requestBody.put("DurationSeconds", "900");
        requestBody.put("Policy", "test");
        requestBody.put("PrincipalArn", "arn:seagate:iam::myorg:test");
        requestBody.put("RoleArn", "arn:seagate:iam::role:test");
        requestBody.put("SAMLAssertion", "c2VhZ2F0ZSB0ZXN0IGFzc2VydGlvbg==");

        assertTrue(validator.isValidCreateParams(requestBody));
    }
}
