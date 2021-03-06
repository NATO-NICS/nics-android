/*
 * Copyright (c) 2008-2021, Massachusetts Institute of Technology (MIT)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mit.ll.nics.android.auth;

import java.util.HashMap;

public class AuthManager {

    private static AuthManager sInstance;
    private static HashMap<String, AuthProvider> mRegisteredAuthTypes;
    private static String mUsername;
    private static String mPassword;
    private static boolean requestingAuth = true;

    public static AuthManager getInstance() {
        if (sInstance == null) {
            sInstance = new AuthManager();
        }
        return sInstance;
    }

    private AuthManager() {
        mRegisteredAuthTypes = new HashMap<>();
    }

    public void setCredentials(String userName, String password) {
        mUsername = userName;
        mPassword = password;
    }

    public void registerAuthType(AuthProvider provider) {
        mRegisteredAuthTypes.put(provider.getType(), provider);
    }

    public AuthProvider getClient() {
//		return mRegisteredAuthTypes.get("Basic");		//enable for Basic Auth
//		return mRegisteredAuthTypes.get("OpenAM");		//enable for OpenAM Auth

        return mRegisteredAuthTypes.get("OpenID");        //enable for OpenID Auth
    }

    public String getCurrentOidAccessToken() {
        return String.format("Bearer %s", getClient().getOidAccessToken());
    }

    public void requestAuth() {
        setRequestingAuth(true);
        for (AuthProvider authType : mRegisteredAuthTypes.values()) {
            authType.stopPendingRequests();
            authType.setupAuth(mUsername, mPassword);
        }
    }

    public boolean isRequestingAuth() {
        return requestingAuth;
    }

    public void setRequestingAuth(boolean requestingAuth) {
        AuthManager.requestingAuth = requestingAuth;
    }

    public void clearAuthTypes() {
        mRegisteredAuthTypes.clear();
    }
}
