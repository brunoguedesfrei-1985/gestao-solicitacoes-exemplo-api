package com.empresa.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CrypterUtil {
	
	public static String getHash(String senhaPura) {
		return BCrypt.withDefaults().hashToString(12, senhaPura.toCharArray());
	}
	
	public static boolean isPasswordValid(String hash, String senha) {
		return BCrypt.verifyer().verify(hash.toCharArray(), senha).verified;
	}

}
