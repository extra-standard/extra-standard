/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extra.client.crypto;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.ReturnCode;

/**
 * CLI Tool zur Verschlüsselung von Passwörtern, die in der Konfiguration genutzt werden.
 *
 * @author Thorsten Vogel
 */
public class ExtraPasswordEncryptor {

    public static void main(final String[] args) {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-encryption.xml");
        final StringEncryptor stringEncryptor = context.getBean("configurationEncryptor", StringEncryptor.class);
        if (args == null || args.length != 1) {
            System.err.println("Argument fehlt. Aufruf mit\nencrypt-password.bat <Passwort>");
            System.exit(ReturnCode.TECHNICAL.getCode());
        }
        System.out.println("ENC("+stringEncryptor.encrypt(args[0])+ ")");
        System.exit(ReturnCode.SUCCESS.getCode());
    }

}
