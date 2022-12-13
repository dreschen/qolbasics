package com.qolbasics.network;

import com.qolbasics.QOLBasicsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkDirection;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(QOLBasicsMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(StoreExpPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StoreExpPacket::new)
                .encoder(StoreExpPacket::toBytes)
                .consumerMainThread(StoreExpPacket::handle)
                .add();

        net.messageBuilder(ThrowStoredExpBottlePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ThrowStoredExpBottlePacket::new)
                .encoder(ThrowStoredExpBottlePacket::toBytes)
                .consumerMainThread(ThrowStoredExpBottlePacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
