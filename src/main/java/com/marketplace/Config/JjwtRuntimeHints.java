package com.marketplace.Config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class JjwtRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        String[] classes = {
                "io.jsonwebtoken.impl.security.KeysBridge",
                "io.jsonwebtoken.impl.security.JwkBuilderBridge",
                "io.jsonwebtoken.impl.security.JwkParserBridge",
                "io.jsonwebtoken.impl.security.MacProvider",
                "io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms",
                "io.jsonwebtoken.impl.DefaultJwtBuilder",
                "io.jsonwebtoken.impl.DefaultJwtParser",
                "io.jsonwebtoken.impl.DefaultJwtParserBuilder",
                "io.jsonwebtoken.jackson.io.JacksonSerializer",
                "io.jsonwebtoken.jackson.io.JacksonDeserializer",
                "io.jsonwebtoken.impl.security.StandardKeyOperations",
                "io.jsonwebtoken.impl.security.StandardEncryptionAlgorithms",
                "io.jsonwebtoken.impl.security.StandardKeyAlgorithms",
                "io.jsonwebtoken.impl.io.StandardCompressionAlgorithms",
                "io.jsonwebtoken.impl.io.Base64UrlStreamEncoder",
                "io.jsonwebtoken.impl.io.ByteBase64UrlStreamEncoder",
                "io.jsonwebtoken.impl.io.CountingInputStream",
                "io.jsonwebtoken.impl.io.EncodingOutputStream",
                "io.jsonwebtoken.impl.io.NamedSerializer",
                "io.jsonwebtoken.impl.io.Streams",
                "io.jsonwebtoken.impl.io.UncloseableInputStream",
                "io.jsonwebtoken.impl.lang.Bytes",
                "io.jsonwebtoken.impl.lang.Function",
                "io.jsonwebtoken.impl.lang.Functions",
                "io.jsonwebtoken.impl.lang.Parameter",
                "io.jsonwebtoken.impl.lang.Services",
                "io.jsonwebtoken.impl.security.DefaultAeadRequest",
                "io.jsonwebtoken.impl.security.DefaultAeadResult",
                "io.jsonwebtoken.impl.security.DefaultKeyRequest",
                "io.jsonwebtoken.impl.security.DefaultSecureRequest",
                "io.jsonwebtoken.impl.security.Pbes2HsAkwAlgorithm",
                "io.jsonwebtoken.impl.security.ProviderKey",
                "io.jsonwebtoken.impl.DefaultClaimsBuilder"
        };
        for (String className : classes) {
            hints.reflection().registerType(TypeReference.of(className),
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.DECLARED_FIELDS);
        }
    }
}
