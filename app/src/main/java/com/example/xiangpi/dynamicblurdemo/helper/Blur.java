package com.example.xiangpi.dynamicblurdemo.helper;

import android.content.Context;

/**
 * Created by xiangpi on 16/9/7.
 */
public class Blur {

    // 模糊算法，模糊实现方式，模糊半径，尺寸缩放，输入图像

    public enum BlurMode {
        BOX, GAUSSIAN, STACK
    }

    public enum BlurScheme {
        RENDER_SCRIPT, OPENGL, NATIVE, JAVA
    }

    private static final BlurMode DEFAULT_MODE = BlurMode.GAUSSIAN;
    private static final BlurScheme DEFAULT_SCHEME = BlurScheme.RENDER_SCRIPT;
    private static final int DEFAULT_BLUR_RADIUS = 5;
    private static final float DEFAULT_SAMPLE_FACTOR = 1.0f;

    private static volatile Blur sHelper;

    private Context mCtx;

    private BlurMode mBlurMode = DEFAULT_MODE;
    private BlurScheme mBlurScheme = DEFAULT_SCHEME;
    private int mRadius;
    private float mSampleFactor;


    private Blur(Context context) {
        mCtx = context.getApplicationContext();
    }

    public static Blur with(Context context) {
        if (sHelper == null) {
            synchronized (Blur.class) {
                if (sHelper == null) {
                    sHelper = new Blur(context);
                }
            }
        }

        return sHelper;
    }

    public Blur setBlurMode(BlurMode mode) {
        mBlurMode = mode;
        return sHelper;
    }

    public Blur setBlurScheme(BlurScheme scheme) {
        mBlurScheme = scheme;
        return sHelper;
    }

    public Blur setBlurRadius(int radius) {
        mRadius = radius;
        return sHelper;
    }

    public Blur setSampleFactor(float factor) {
        mSampleFactor = factor;
        return sHelper;
    }

    /**
     * 创建不同的模糊发生器
     * @return
     */
    public IBlur getBlurGenerator() {

        IBlur generator = null;

        if (mBlurScheme == BlurScheme.RENDER_SCRIPT) {
            generator = RenderScriptBlurGenerator.getInstance(mCtx);
        } else if (mBlurScheme == BlurScheme.OPENGL) {
            generator = OpenGLBlurGenerator.getInstance();
        } else if (mBlurScheme == BlurScheme.NATIVE){
            generator = NativeBlurGenerator.getInstance();
        } else if (mBlurScheme == BlurScheme.JAVA) {
            generator = OriginBlurGenerator.getInstance();
        }

        if (generator != null) {
            generator.setBlurMode(mBlurMode);
            generator.setBlurRadius(mRadius);
        }

        return generator;

    }


}
