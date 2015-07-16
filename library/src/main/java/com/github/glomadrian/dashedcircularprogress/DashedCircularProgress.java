package com.github.glomadrian.dashedcircularprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.github.glomadrian.dashedcircularprogress.painter.BottomIconPainter;
import com.github.glomadrian.dashedcircularprogress.painter.ExternalCirclePainter;
import com.github.glomadrian.dashedcircularprogress.painter.ExternalCirclePainterImp;
import com.github.glomadrian.dashedcircularprogress.painter.LeftIconPainter;
import com.github.glomadrian.dashedcircularprogress.painter.RightIconPainter;
import com.github.glomadrian.dashedcircularprogress.painter.TopIconPainter;
import com.github.glomadrian.dashedcircularprogress.painter.InternalCirclePainter;
import com.github.glomadrian.dashedcircularprogress.painter.InternalCirclePainterImp;
import com.github.glomadrian.dashedcircularprogress.painter.ProgressPainter;
import com.github.glomadrian.dashedcircularprogress.painter.ProgressPainterImp;

/**
 * @author Adrián García Lomas
 */
public class DashedCircularProgress extends RelativeLayout {

    private ExternalCirclePainter externalCirclePainter;
    private InternalCirclePainter internalCirclePainter;
    private ProgressPainter progressPainter;
    private TopIconPainter iconPainter;
    private BottomIconPainter bottomiconPainter;
    private LeftIconPainter lefticonPainter;
    private RightIconPainter righticonPainter;
    private Bitmap image;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private int externalColor = Color.WHITE;
    private int internalBaseColor = Color.YELLOW;
    private int progressColor = Color.WHITE;
    private float min = 0;
    private float last = min;
    private float max = 100;
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private float value;
    private int duration = 1000;
    private int padingTop = 22;
    private int heightNormalittation = 10;
    private Context _context;

    public DashedCircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        init(context, attrs);
    }

    public DashedCircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * All the children must have a max height and width, never bigger than the internal circle
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int count = getChildCount();
        int maxWidth = getWidth() / 2;
        int maxHeight = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int mesaureWidth = child.getMeasuredWidth();
            int measureHeight = child.getMeasuredWidth();

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            child.setTranslationY(padingTop);

            RelativeLayout.LayoutParams relativeLayoutParams =
                    (RelativeLayout.LayoutParams) child.getLayoutParams();
            relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            child.setLayoutParams(relativeLayoutParams);

            if (mesaureWidth > maxWidth) {
                layoutParams.width = maxWidth;
            }

            if (measureHeight > maxHeight) {
                layoutParams.height = maxHeight;
            }
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        setWillNotDraw(false);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet,
                R.styleable.DashedCircularProgress);
        initAttributes(attributes);
        initPainters();
        initValueAnimator();
    }

    private void initAttributes(TypedArray attributes) {
        externalColor = attributes.getColor(R.styleable.DashedCircularProgress_external_color,
                externalColor);
        internalBaseColor = attributes.getColor(R.styleable.DashedCircularProgress_base_color,
                internalBaseColor);
        progressColor = attributes.getColor(R.styleable.DashedCircularProgress_progress_color,
                progressColor);
        max = attributes.getFloat(R.styleable.DashedCircularProgress_max, max);
        duration = attributes.getInt(R.styleable.DashedCircularProgress_duration, duration);
        image = BitmapFactory.decodeResource(getResources(), attributes
                .getResourceId(R.styleable.DashedCircularProgress_progress_icon,
                        R.drawable.ic_action_perm_identity));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progressPainter.onSizeChanged(h, w);
        externalCirclePainter.onSizeChanged(h, w);
        internalCirclePainter.onSizeChanged(h, w);
        iconPainter.onSizeChanged(h, w);
        bottomiconPainter.onSizeChanged(h, w);
        lefticonPainter.onSizeChanged(h, w);
        righticonPainter.onSizeChanged(h, w);
        animateValue();
    }

    private void initPainters() {
        progressPainter = new ProgressPainterImp(progressColor, min, max);
        externalCirclePainter = new ExternalCirclePainterImp(externalColor);
        internalCirclePainter = new InternalCirclePainterImp(internalBaseColor);



        iconPainter = new TopIconPainter(image);
        bottomiconPainter = new BottomIconPainter(image);
        lefticonPainter = new LeftIconPainter(image);
        righticonPainter = new RightIconPainter(image);
    }

    private void initValueAnimator() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimatorListenerImp());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  externalCirclePainter.draw(canvas);
        // internalCirclePainter.draw(canvas);
        progressPainter.draw(canvas);
        bottomiconPainter.draw(canvas);
        iconPainter.draw(canvas);
        lefticonPainter.draw(canvas);
        righticonPainter.draw(canvas);
        invalidate();
    }

    public void setValue(float value) {
        this.value = value;
        if (value <= max || value >= min) {
            animateValue();
        }
    }

    private void animateValue() {
        if (valueAnimator != null) {

            valueAnimator.setFloatValues(last, value);
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec + heightNormalittation);
    }

    public void setOnValueChangeListener(OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;

        if (valueAnimator != null) {
            valueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
        progressPainter.setMin(min);
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
        progressPainter.setMax(max);
    }

    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            progressPainter.setValue(value);
            if (valueChangeListener != null) {
                valueChangeListener.onValueChange(value);
            }
            Resources res = _context.getResources();
            last = value;
            if (last >= 998 || last == 999) {
                last = min;
                setValue(999);

                iconPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_perm_identity));
                righticonPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_perm_identity));
                bottomiconPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_perm_identity));
                lefticonPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_perm_identity));

            }

            if (value > 0 && value <= 250) {
                iconPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_person));

            } else if (value >= 251 && value <= 500) {
                righticonPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_person));
            } else if (value >= 501 && value <= 750) {
                bottomiconPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_person));
            } else if (value >= 751 && value <= 998) {
                lefticonPainter.setImage(BitmapFactory.decodeResource(res, R.drawable.ic_action_person));
            }


        }


    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public void setIcon(int drawable) {
        if (iconPainter != null) {
            iconPainter.setImage(BitmapFactory.decodeResource(getContext().getResources(),
                    drawable));
        }

        if (bottomiconPainter != null) {
            bottomiconPainter.setImage(BitmapFactory.decodeResource(getContext().getResources(),
                    drawable));
        }

        if (lefticonPainter != null) {
            lefticonPainter.setImage(BitmapFactory.decodeResource(getContext().getResources(),
                    drawable));
        }

        if (righticonPainter != null) {
            righticonPainter.setImage(BitmapFactory.decodeResource(getContext().getResources(),
                    drawable));
        }


    }


    public void reset() {
        last = min;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPainter.setColor(progressColor);
    }

    public int getInternalBaseColor() {
        return internalBaseColor;
    }

    public void setInternalBaseColor(int internalBaseColor) {
        this.internalBaseColor = internalBaseColor;
        internalCirclePainter.setColor(progressColor);
    }

    public int getExternalColor() {
        return externalColor;
    }

    public void setExternalColor(int externalColor) {
        this.externalColor = externalColor;
        externalCirclePainter.setColor(externalColor);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
