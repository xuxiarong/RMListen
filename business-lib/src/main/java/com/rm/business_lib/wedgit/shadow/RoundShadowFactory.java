package com.rm.business_lib.wedgit.shadow;


import androidx.annotation.NonNull;

/**
 * 圆角矩形阴影工厂
 */
public class RoundShadowFactory implements ShadowFactory {

    @Override
    public boolean supportKey(@NonNull Object key) {
        return key instanceof RoundShadow.Key;
    }

    @NonNull
    @Override
    public Object copyKey(@NonNull Object key) throws UnsupportedKeyException {
        if (!supportKey(key)) {
            throw new UnsupportedKeyException("Key:" + key);
        }
        return ((RoundShadow.Key) key).clone();
    }

    @NonNull
    @Override
    public Shadow create(@NonNull Object key) throws UnsupportedKeyException {
        if (!supportKey(key)) {
            throw new UnsupportedKeyException("Key:" + key);
        }
        if (key instanceof UnsupportedRoundShadow.Key) {
            return new UnsupportedRoundShadow((UnsupportedRoundShadow.Key) key);
        }
        return new RoundShadow((RoundShadow.Key) key);
    }
}
