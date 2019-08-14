package com.mencurigakansekali.servicecoordinator.util;

public class ToneUtil {
    public static final double C4 = 262;
    public static final double D4 = 294;
    public static final double E4 = 330;
    public static final double F4 = 349;
    public static final double G4 = 392;
    public static final double A5 = 440;
    public static final double B5 = 494;

    public static byte[] generate() {
        int duration = 1;
        int sampleRate = 8000;
        int numSamples = duration * sampleRate;
        double sample[] = new double[numSamples];

        double freqOfTone = C4; // hz

        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        sample = new double[] {
            C4,
            D4,
            E4,
            F4
        };

        byte generatedSnd[] = new byte[2 * numSamples];

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }


        return generatedSnd;
    }
}
