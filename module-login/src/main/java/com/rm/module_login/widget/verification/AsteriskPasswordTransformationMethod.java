package com.rm.module_login.widget.verification;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        @Override
        public char charAt(int index) {
            return '•'; // This is the important part
        }

        @Override
        public int length() {
            return mSource.length(); // Return default
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
