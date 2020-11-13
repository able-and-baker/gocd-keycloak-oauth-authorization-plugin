/*
 * Copyright 2017 ThoughtWorks, Inc.
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

package cd.go.authorization.keycloak.requests;

import cd.go.authorization.keycloak.executors.AuthConfigValidateRequestExecutor;
import cd.go.authorization.keycloak.models.KeycloakConfiguration;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;

public class AuthConfigValidateRequest extends Request {
    private final KeycloakConfiguration keycloakConfiguration;

    public AuthConfigValidateRequest(KeycloakConfiguration keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }

    public static final AuthConfigValidateRequest from(GoPluginApiRequest apiRequest) {
        return new AuthConfigValidateRequest(KeycloakConfiguration.fromJSON(apiRequest.requestBody()));
    }

    public KeycloakConfiguration keycloakConfiguration() {
        return keycloakConfiguration;
    }

    @Override
    public AuthConfigValidateRequestExecutor executor() {
        return new AuthConfigValidateRequestExecutor(this);
    }
}
