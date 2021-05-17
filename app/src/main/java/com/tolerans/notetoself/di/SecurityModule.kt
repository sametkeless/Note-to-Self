package com.tolerans.notetoself.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tolerans.notetoself.util.EncryptionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class SecurityModule {


    @Provides
    @Singleton
    fun providesSecurePreferences(@ApplicationContext context:Context): SharedPreferences {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        return EncryptedSharedPreferences.create(
                "shared_preferences_filename",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    @Provides
    @Singleton
    fun providesEncryptionHelper(sharedPreferences: SharedPreferences): EncryptionHelper {

        var key:String = sharedPreferences.getString("key","")!!
        var iv:String = sharedPreferences.getString("iv","")!!

        if(key.equals("") || iv.equals(""))
        {
            key = UUID.randomUUID().toString().substring(0,32)
            iv = UUID.randomUUID().toString().substring(0,16)
            sharedPreferences.edit().putString("key",key).apply()
            sharedPreferences.edit().putString("iv",iv).apply()

        }
        return EncryptionHelper(key = key.toByteArray(),iv = iv.toByteArray())

    }

}