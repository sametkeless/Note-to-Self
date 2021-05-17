package com.tolerans.notetoself.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper constructor(val key: ByteArray,val iv:ByteArray) {

    fun encrypt(message: String): String? {
        val srcBuff = message.toByteArray(charset("UTF8"))
        val skeySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        val ecipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
        val dstBuff: ByteArray = ecipher.doFinal(srcBuff)
        return Base64.encodeToString(dstBuff, Base64.DEFAULT)
    }

     fun decrypt(encrypted: String?): String? {
        val skeySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        val ecipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)
        val raw: ByteArray = Base64.decode(encrypted, Base64.DEFAULT)
        val originalBytes: ByteArray = ecipher.doFinal(raw)
        return String(originalBytes, charset("UTF8"))
    }

}