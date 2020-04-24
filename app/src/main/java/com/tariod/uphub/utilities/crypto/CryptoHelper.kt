package com.tariod.uphub.utilities.crypto

import com.tariod.uphub.BuildConfig
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.params.KeyParameter

class CryptoHelper {

    companion object {
        private val CHARSET = Charsets.UTF_8
    }

    fun hashPassword(password: String): ByteArray {
        val bytes = password.toByteArray(CHARSET)
        val gen = PKCS5S2ParametersGenerator(SHA256Digest())
        gen.init(
            bytes,
            BuildConfig.PASSWORD_SALT.toByteArray(CHARSET),
            BuildConfig.PASSWORD_HASH_ITERATION
        )
        val p = gen.generateDerivedParameters(BuildConfig.PASSWORD_HASH_LENGTH * 8)
        return (p as? KeyParameter)?.key ?: byteArrayOf()
    }

    fun hashPasswordAsString(password: String) = hashPassword(password).toString(CHARSET)
}