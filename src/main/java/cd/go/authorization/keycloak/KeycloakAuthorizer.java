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

package cd.go.authorization.keycloak;

import cd.go.authorization.keycloak.models.AuthConfig;
import cd.go.authorization.keycloak.models.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cd.go.authorization.keycloak.KeycloakPlugin.LOG;
import static java.text.MessageFormat.format;

public class KeycloakAuthorizer {
    private final MembershipChecker membershipChecker;

    public KeycloakAuthorizer() {
        this(new MembershipChecker());
    }

    public KeycloakAuthorizer(MembershipChecker membershipChecker) {
        this.membershipChecker = membershipChecker;
    }

    public List<String> authorize(KeycloakUser loggedInUser, AuthConfig authConfig, List<Role> roles) throws IOException {
        final KeycloakUser user = loggedInUser;
        final List<String> assignedRoles = new ArrayList<>();

        if (roles.isEmpty()) {
            return assignedRoles;
        }

        LOG.info(format("[Authorize] Authorizing user {0}", user.getEmail()));

        for (Role role : roles) {
            final List<String> allowedUsers = role.roleConfiguration().users();
            if (!allowedUsers.isEmpty() && allowedUsers.contains(user.getEmail().toLowerCase())) {
                LOG.debug(format("[Authorize] Assigning role `{0}` to user `{1}`. As user belongs to allowed users list.", role.name(), user.getEmail()));
                assignedRoles.add(role.name());
                continue;
            }

            if (membershipChecker.isAMemberOfAtLeastOneGroup(loggedInUser, authConfig, role.roleConfiguration().groups())) {
                LOG.debug(format("[Authorize] Assigning role `{0}` to user `{1}`. As user is a member of at least one group.", role.name(), user.getEmail()));
                assignedRoles.add(role.name());
            }
        }

        LOG.debug(format("[Authorize] User `{0}` is authorized with `{1}` role(s).", user.getEmail(), assignedRoles));

        return assignedRoles;
    }
}
