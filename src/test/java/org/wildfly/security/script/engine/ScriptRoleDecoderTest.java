/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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
 */
 
package org.wildfly.security.script.engine;

import org.junit.jupiter.api.Test;
import org.wildfly.security.authz.AuthorizationIdentity;
import org.wildfly.security.authz.Attributes;
import org.wildfly.security.authz.MapAttributes;
import org.wildfly.security.authz.Roles;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ScriptRoleDecoder tests 
 *
 * @author <a href="mailto:guptab3@gmail.com">Bhaskar Gupta</a>
 */

class ScriptRoleDecoderTest {


    @Test
    public void testInitialiseMethodSpecified() throws ScriptException {
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("jsFunction","myFunction");
        configuration.put("attribute","department");

        ScriptRoleDecoder obj = new ScriptRoleDecoder();

        obj.initialize(configuration);

        Set<String> ss = createSet("student");
        Attributes att = new MapAttributes(createMap("department",ss));
        AuthorizationIdentity authId = AuthorizationIdentity.basicIdentity(att);

        Roles checkRole = Roles.fromSet(createSet("gate","class","room"));
        Roles roleDefault = obj.decodeRoles(authId);
        assertEquals(checkRole,roleDefault);
        Iterator<String> iterator = roleDefault.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(count, 3);

    }
    @Test
    public void testInitialiseDefaultMethod() throws  ScriptException{
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("attribute","department");

        ScriptRoleDecoder obj = new ScriptRoleDecoder();

        obj.initialize(configuration);

        Set<String> ss = createSet("student");
        Attributes att = new MapAttributes(createMap("department",ss));
        AuthorizationIdentity authId = AuthorizationIdentity.basicIdentity(att);

        Roles checkRole = Roles.fromSet(createSet("gate","class","room"));
        Roles roleDefault = obj.decodeRoles(authId);
        assertEquals(checkRole,roleDefault);
        Iterator<String> iterator = roleDefault.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(count, 3);
    }
    @Test
    public void testMissingPath() throws ScriptException {
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("jsFunction","myFunction");
        configuration.put("attribute","department");

        ScriptRoleDecoder obj = new ScriptRoleDecoder();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> obj.initialize(configuration));
        assertEquals("pathToJSFile cannot be null",exception.getMessage());
    }
    @Test
    public void testWrongFilePath(){
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("jsFunction","myFunction");
        configuration.put("attribute","department");

        ScriptRoleDecoder obj = new ScriptRoleDecoder();
        Throwable exception = assertThrows(FileNotFoundException.class, () -> obj.initialize(configuration));
        assertEquals("file not found",exception.getMessage());
    }
    @Test
    public void testJSFunctionNotExisting() throws ScriptException {
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("jsFunction","myFunction");
        configuration.put("attribute","department");

        Set<String> ss = createSet("student");
        Attributes att = new MapAttributes(createMap("department",ss));
        AuthorizationIdentity authId = AuthorizationIdentity.basicIdentity(att);

        ScriptRoleDecoder obj = new ScriptRoleDecoder();
        obj.initialize(configuration);
        Throwable exception = assertThrows(NoSuchMethodException.class, () -> obj.decodeRoles(authId));
        assertEquals("no such method exists",exception.getMessage());
    }
    @Test
    public void testNullAttribute(){
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("jsFunction","myFunction");

        ScriptRoleDecoder obj = new ScriptRoleDecoder();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> obj.initialize(configuration));
        assertEquals("attribute cannot be null",exception.getMessage());
    }
    @Test
    public void testAttributeAbsentInAuthID() throws ScriptException {
        HashMap<String,String> configuration = new HashMap<>();

        configuration.put("pathToJSFile","src//TestJSFile.js");
        configuration.put("jsFunction","myFunction");
        configuration.put("attribute","department");

        Set<String> ss = createSet("student");
        Attributes att = new MapAttributes(createMap("department",ss));
        AuthorizationIdentity authId = AuthorizationIdentity.basicIdentity(att);

        ScriptRoleDecoder obj = new ScriptRoleDecoder();
        obj.initialize(configuration);

        Roles checkRole = Roles.fromSet(createSet("gate","class","room"));
        Roles roleDefault = obj.decodeRoles(authId);

        assertNotEquals(checkRole,roleDefault);

        Iterator<String> iterator = roleDefault.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(count, 0);

    }
    private Set<String> createSet(String... values) {
        HashSet<String> set = new HashSet<>();
        for (String s : values) set.add(s);
        return set;
    }
    private HashMap<String,Set<String>> createMap(String key, Set<String> value){
        HashMap<String,Set<String>> hashmap = new HashMap<>();
        hashmap.put(key,value);
        return hashmap;
    }
}
