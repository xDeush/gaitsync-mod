package com.gaitsync.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gaitsync.client.GaitClock;
import com.gaitsync.client.GaitState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

/**
 * Podmienia faze chodu, zanim model policzy z niej katy konczyn.
 *
 * Wchodzimy na HEAD, czyli przed cala reszta animacji -- dzieki temu wszystko,
 * co vanilla liczy z walkAnimationPos (nogi, rece, bujanie), dostaje juz
 * zsynchronizowana wartosc. Nie dotykamy niczego innego: skradanie, uzywanie
 * przedmiotu czy plywanie dzialaja normalnie.
 */
@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void gaitsync$syncPhase(HumanoidRenderState state, CallbackInfo ci) {
        if (!GaitState.enabled) {
            return;
        }

        // Gracze maja wlasny typ stanu, wiec nie trzeba porownywac id.
        if (GaitState.playersOnly && !(state instanceof AvatarRenderState)) {
            return;
        }

        // Stoi w miejscu -- nie ma czego synchronizowac, a wymuszenie fazy
        // kazaloby mu przebierac nogami bez ruchu.
        if (state.walkAnimationSpeed <= 0.01F) {
            return;
        }

        state.walkAnimationPos = GaitClock.phase();

        if (GaitState.amplitude > 0.0F) {
            state.walkAnimationSpeed = GaitState.amplitude;
        }
    }
}
