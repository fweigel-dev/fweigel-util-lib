package dev.fweigel.mobutils.core.client.sound;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public class WrappedSoundInstance implements SoundInstance {
    private final SoundInstance wrapped;
    private final float volumeMultiplier;

    public WrappedSoundInstance(SoundInstance wrapped, float volumeMultiplier) {
        this.wrapped = wrapped;
        this.volumeMultiplier = volumeMultiplier;
    }

    @Override
    public Identifier getIdentifier() {
        return wrapped.getIdentifier();
    }

    @Override
    public WeighedSoundEvents resolve(SoundManager soundManager) {
        return wrapped.resolve(soundManager);
    }

    @Override
    public Sound getSound() {
        return wrapped.getSound();
    }

    @Override
    public SoundSource getSource() {
        return wrapped.getSource();
    }

    @Override
    public boolean isLooping() {
        return wrapped.isLooping();
    }

    @Override
    public boolean isRelative() {
        return wrapped.isRelative();
    }

    @Override
    public int getDelay() {
        return wrapped.getDelay();
    }

    @Override
    public float getVolume() {
        return wrapped.getVolume() * volumeMultiplier;
    }

    @Override
    public float getPitch() {
        return wrapped.getPitch();
    }

    @Override
    public double getX() {
        return wrapped.getX();
    }

    @Override
    public double getY() {
        return wrapped.getY();
    }

    @Override
    public double getZ() {
        return wrapped.getZ();
    }

    @Override
    public SoundInstance.Attenuation getAttenuation() {
        return wrapped.getAttenuation();
    }
}
