package cc.neckbeard.packetdump;

import net.minecraft.network.Packet;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;

public class Dump {

    public final Instant time;
    public final String dump;
    public final Direction direction;

    private Dump(Packet<?> packet, Direction direction) {
        this.time = Instant.now();
        this.dump = ReflectionToStringBuilder.toString(packet, ToStringStyle.SHORT_PREFIX_STYLE);
        this.direction = direction;
    }

    public static Dump in(Packet<?> packet) {
        return new Dump(packet, Direction.IN);
    }

    public static Dump out(Packet<?> packet) {
        return new Dump(packet, Direction.OUT);
    }

    @Override
    public String toString() {
        return time.toString() + " " + direction.toString() + (direction == Direction.IN ? " " : "") + " " + dump;
    }

    private enum Direction {
        IN, OUT
    }

}
