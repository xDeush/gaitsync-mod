package com.gaitsync.client;

import com.mojang.brigadier.arguments.FloatArgumentType;

import com.gaitsync.GaitSync;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;

/**
 * Komenda /gaitsync.
 *
 * Komenda KLIENCKA, nie serwerowa -- inaczej nie dzialalaby podczas
 * odtwarzania nagrania, bo tam nie ma serwera, ktory by ja przyjal.
 *
 * Domyslne ustawienia dzialaja od razu i nie wymagaja niczego wpisywac.
 * Komenda jest po to, zeby dalo sie cos poprawic bez restartu gry.
 */
public class GaitSyncClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GaitClock.register();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) ->
                dispatcher.register(ClientCommands.literal("gaitsync")
                        .executes(ctx -> report(ctx.getSource()))

                        .then(ClientCommands.literal("reset").executes(ctx -> {
                            ctx.getSource().sendFeedback(
                                    Component.literal(GaitReset.reset()));
                            return 1;
                        }))

                        .then(ClientCommands.literal("on").executes(ctx -> {
                            GaitState.enabled = true;
                            return report(ctx.getSource());
                        }))
                        .then(ClientCommands.literal("off").executes(ctx -> {
                            GaitState.enabled = false;
                            return report(ctx.getSource());
                        }))
                        .then(ClientCommands.literal("freeze").executes(ctx -> {
                            GaitState.frozen = !GaitState.frozen;
                            return report(ctx.getSource());
                        }))

                        .then(ClientCommands.literal("rate")
                                .then(ClientCommands.argument("wartosc", FloatArgumentType.floatArg(0.0F, 5.0F))
                                        .executes(ctx -> {
                                            GaitState.rate = FloatArgumentType.getFloat(ctx, "wartosc");
                                            return report(ctx.getSource());
                                        })))

                        .then(ClientCommands.literal("offset")
                                .then(ClientCommands.argument("wartosc", FloatArgumentType.floatArg(-1000.0F, 1000.0F))
                                        .executes(ctx -> {
                                            GaitState.offset = FloatArgumentType.getFloat(ctx, "wartosc");
                                            return report(ctx.getSource());
                                        })))

                        .then(ClientCommands.literal("amplitude")
                                .then(ClientCommands.argument("wartosc", FloatArgumentType.floatArg(0.0F, 3.0F))
                                        .executes(ctx -> {
                                            GaitState.amplitude = FloatArgumentType.getFloat(ctx, "wartosc");
                                            return report(ctx.getSource());
                                        })))

                        .then(ClientCommands.literal("scope")
                                .then(ClientCommands.literal("gracz").executes(ctx -> {
                                    GaitState.playersOnly = true;
                                    return report(ctx.getSource());
                                }))
                                .then(ClientCommands.literal("wszyscy").executes(ctx -> {
                                    GaitState.playersOnly = false;
                                    return report(ctx.getSource());
                                })))));

        GaitSync.LOGGER.info("GaitSync gotowy -- {}", GaitState.describe());
    }

    private static int report(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
        source.sendFeedback(Component.literal(GaitState.describe()));
        return 1;
    }
}
