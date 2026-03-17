package dev.fweigel.mobutils.core.client.mixin;

import dev.fweigel.mobutils.core.client.sound.SoundVolumeRegistry;
import dev.fweigel.mobutils.core.client.sound.WrappedSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @ModifyVariable(method = "play", at = @At("HEAD"), argsOnly = true)
    private SoundInstance modifySoundVolume(SoundInstance sound) {
        String path = sound.getIdentifier().getPath();
        float volume = SoundVolumeRegistry.getVolume(path);
        if (volume < 1.0f) {
            return new WrappedSoundInstance(sound, volume);
        }
        return sound;
    }
}
