package io.hydrocarbon.moutai.generator;

import io.hydrocarbon.moutai.util.UUIDUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.util.EnumSet;

/**
 * @author Zou Zhenfeng
 * @since 2024-06-17
 */
public class ShortUUIDGenerator implements BeforeExecutionGenerator {

    /**
     * Generate a value.
     *
     * @param session      The session from which the request originates.
     * @param owner        The instance of the object owning the attribute for which we are generating a value.
     * @param currentValue The current value assigned to the property, or {@code null}
     * @param eventType    The type of event that has triggered generation of a new value
     * @return The generated value
     */
    @Override
    public Long generate(SharedSessionContractImplementor session,
                         Object owner,
                         Object currentValue,
                         EventType eventType) {
        return UUIDUtil.generate();
    }

    /**
     * The {@linkplain EventType event types} for which this generator should be called
     * to produce a new value.
     * <p>
     * Identifier generators must return {@link EventTypeSets#INSERT_ONLY}.
     *
     * @return a set of {@link EventType}s.
     */
    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}
