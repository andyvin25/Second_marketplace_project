package com.marketplace.Util;

import de.huxhorn.sulky.ulid.ULID;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        ULID generateId = new ULID();
        return generateId.nextULID();
    }
}
