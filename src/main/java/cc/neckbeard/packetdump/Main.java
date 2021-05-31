package cc.neckbeard.packetdump;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Main implements ModInitializer {

    public static final BlockingQueue<Dump> QUEUE = new LinkedBlockingQueue<>();

    private static final File LOG_DIR = new File(MinecraftClient.getInstance().runDirectory, "PacketDump");
    private static final File LOG_FILE = new File(LOG_DIR, Instant.now().toString() + ".log");

    @Override
    public void onInitialize() {

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                if (QUEUE.size() == 0) return;

                if (!LOG_DIR.exists() && !LOG_DIR.mkdir()) {
                    System.err.println("Unable to create Log directory!");
                    return;
                }

                List<Dump> packets = new ArrayList<>();
                QUEUE.drainTo(packets);

                try {
                    FileWriter writer = new FileWriter(LOG_FILE, true);
                    writer.write(packets.stream()
                        .sorted(Comparator.comparing(p -> p.time))
                        .map(Dump::toString)
                        .collect(Collectors.joining(System.lineSeparator())));
                    writer.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

            }

        }, 1000, 1000);

    }

}
