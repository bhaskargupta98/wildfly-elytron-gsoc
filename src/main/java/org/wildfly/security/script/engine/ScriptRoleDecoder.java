/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2019 Red Hat, Inc., and individual contributors
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
import main.java.RoleDecoder;
import org.wildfly.security.authz.AuthorizationIdentity;
import org.wildfly.security.authz.Roles;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * Adds support for scripting to RoleDecoder
 *
 * @author <a href="mailto:guptab3@gmail.com">Bhaskar Gupta</a>
 */

public class ScriptRoleDecoder implements RoleDecoder {
    private ScriptEngineManager manager;
    private javax.script.ScriptEngine jsEngine;
    private Invocable invocable;
    private String pathToJSFile;
    private String jsFunction;
    private String attribute;
    public ScriptRoleDecoder(){
        manager  = new ScriptEngineManager();
        jsEngine = manager.getEngineByName("nashorn");
        invocable = (Invocable) jsEngine;
    }
    public void initialize(Map<String, String> configuration) throws ScriptException {
        pathToJSFile = configuration.get("pathToJSFile");
        if(pathToJSFile == null){
            throw new IllegalArgumentException("pathToJSFile cannot be null");
        }
        jsFunction = configuration.get("jsFunction");
        attribute = configuration.get("attribute");
        if(attribute == null){
            throw new IllegalArgumentException("attribute cannot be null");
        }
        jsEngine.eval(pathToJSFile);
    }
    public Roles decodeRoles(AuthorizationIdentity authorizationIdentity){ //returns Roles object
        try {
            return decodeRolesHelper(authorizationIdentity);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    private Roles decodeRolesHelper(AuthorizationIdentity authorizationIdentity) throws ScriptException, NoSuchMethodException { //helper function to use custom method written in JS
        List<String> myStrings = authorizationIdentity.getAttributes().get(attribute);
        String[] attributeValues = myStrings.toArray(new String[myStrings.size()]);
        Set<String> setOfStrings =  (Set<String>) invocable.invokeFunction((jsFunction!=null) ? jsFunction : "returnSetOfRoles",attributeValues); ////By default JS Function "returnSetOfRoles" will be used unless passed in while object creation
        Roles decodedRoleSet = Roles.fromSet(setOfStrings);
        return decodedRoleSet;
    }
}
