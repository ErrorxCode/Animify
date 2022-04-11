package com.xcoder.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScrollAnimator {
    private static ScrollAnimator INSTANCE;
    private boolean onlyOnce;
    private String[] properties;
    private int duration = 500;
    private TimeInterpolator interpolator;
    private Animator.AnimatorListener listener;
    private int oldPosition = 0;


    /**
     * Creates or gets the instance of the class.
     *
     * @return Singleton instance of the class.
     */
    public static ScrollAnimator create() {
        if (INSTANCE == null)
            INSTANCE = new ScrollAnimator();
        return INSTANCE;
    }

    /**
     * Sets the property of animation to use.
     *
     * @param animation animation property. use constants defined in this class.
     * @return this
     */
    public ScrollAnimator withAnimation(String... animation) {
        properties = animation;
        return this;
    }

    /**
     * Sets the custom property for the animation. The property must be a valid property of the view.
     * <p>
     *     For example, if the target view has setter method {@code setBackgroundColor(int)}, then the property will be "backgroundColor".
     * </p>
     * Note : This must not be called along with {@link #withAnimation(String...)}
     *        as the last one will override the animation property.
     * @param property property of the view to animate
     * @param start start value of the property
     * @param end end value of the property
     * @return this
     */
    public ScrollAnimator withCustom(String property,float start,float end){
        properties = new String[]{property};
        return this;
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration duration of the animation. Use constants for best result.
     * @return this
     */
    public ScrollAnimator tillDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Sets the interpolator of the animation.
     *
     * @param interpolator interpolator of the animation.
     * @return this
     */
    public ScrollAnimator withInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    /**
     * Sets the listener of the animation.
     *
     * @param listener listener of the animation.
     * @return this
     */
    public ScrollAnimator listenOn(Animator.AnimatorListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Sets the animation to be played only when scrolled bottom to top.
     *
     * @param reverse true for yes, false otherwise. Default to false.
     * @return this
     */
    public ScrollAnimator playOnlyOnDownScroll(boolean reverse) {
        this.onlyOnce = reverse;
        return this;
    }

    /**
     * Starts the animation on the specified nested scroll view. If you are using scroll change listener on this scroll view,
     * use {@link #animateOnScroll(FrameLayout, int, int)} instead. Setting listener on this scroll view will not play the animation.
     * @param scrollView The scroll view to animate.
     */
    public void animate(@NonNull NestedScrollView scrollView) {
        View[] views = getChildren((ViewGroup) scrollView.getChildAt(0));
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (onlyOnce) {
                if (scrollY > oldScrollY) {
                    setUpAnimation(views, scrollView);
                }
            } else {
                setUpAnimation(views, scrollView);
            }
        });
    }

    /**
     * Starts the animation on the specified scroll view.
     *
     * @param scrollView The scroll view to animate.
     */
    public void animate(@NonNull ScrollView scrollView) {
        View[] views = getChildren((ViewGroup) scrollView.getChildAt(0));
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (onlyOnce) {
                if (scrollY > oldScrollY) {
                    setUpAnimation(views, scrollView);
                }
            } else {
                setUpAnimation(views, scrollView);
            }
        });
    }

    /**
     * Starts the animation on the specified list view. Must be called after setting the adapter.
     * otherwise, the animation will not work.
     *
     * @param listView The list view to animate.
     */
    public void animate(@NonNull ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        BaseAdapter animatedAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return adapter.getCount();
            }

            @Override
            public Object getItem(int position) {
                return adapter.getItem(position);
            }

            @Override
            public long getItemId(int position) {
                return adapter.getItemId(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = adapter.getView(position, convertView, parent);
                animate(view, position);
                return view;
            }
        };
        listView.setAdapter(animatedAdapter);
    }

    /**
     * Starts the animation on the specified recycler view. Must be called after setting adapter to it
     * or else you will get a null pointer exception.
     * @param recyclerView The recycler view to animate.
     */
    public void animate(@NonNull RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        RecyclerView.Adapter animatedAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(parent, viewType);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                adapter.onBindViewHolder(holder, position);
                animate(holder.itemView, position);
            }

            @Override
            public int getItemCount() {
                return adapter.getItemCount();
            }
        };
        recyclerView.setAdapter(animatedAdapter);
    }

    /**
     * Call this in adapters onBindViewHolder/getView to animate the items.
     *
     * @param view The item that is to be displayed in the recycler/list view.
     */
    protected void animate(@NonNull View view, int position) {
        Animator animator = getAnimation(view);
        if (onlyOnce) {
            System.out.println("oldPosition: " + oldPosition + " position: " + position);
            if (position > oldPosition) {
                animator.start();
                oldPosition = position;
            } else {
                oldPosition = position - 1;
            }
        } else {
            animator.start();
        }
    }

    /**
     * Animates the scrollview when scroll is changed. To be called in scrollview's onScrollChangeListener.
     * @param scrollview The scrollview to animate.
     * @param scrollY The current scrollY of the scrollview.
     * @param oldScrollY The previous scrollY of the scrollview.
     */
    public void animateOnScroll(@NonNull FrameLayout scrollview, int scrollY, int oldScrollY) {
        View[] views = getChildren((ViewGroup) scrollview.getChildAt(0));
        if (onlyOnce) {
            if (scrollY > oldScrollY) {
                setUpAnimation(views, scrollview);
            }
        } else {
            setUpAnimation(views, scrollview);
        }
    }



    private void setUpAnimation(View[] views, FrameLayout scrollView) {
        for (View view : views) {
            if (isHidden(view, scrollView)) {
                getAnimation(view).start();
            }
        }
    }

    private AnimatorSet getAnimation(View view) {
        ObjectAnimator animator;
        List<Animator> animators = new ArrayList<>();
        for (String property : properties){
            switch (property) {
                case "slide4left" -> animator = ObjectAnimator.ofFloat(view, "translationX", -view.getLeft() - 150, 0);
                case "slide4right" -> animator = ObjectAnimator.ofFloat(view, "translationX", view.getRight(), 0);
                case "slide4top" -> animator = ObjectAnimator.ofFloat(view, "translationY", -view.getResources().getDisplayMetrics().heightPixels, 0);
                case "slide4bottom" -> animator = ObjectAnimator.ofFloat(view, "translationY", view.getBottom() + 150, 0);
                case "drop4top" -> animator = ObjectAnimator.ofFloat(view, "rotation", 90, 0);
                case "drop4bottom" -> animator = ObjectAnimator.ofFloat(view, "rotation", -90, 0);
                case "rotate" -> animator = ObjectAnimator.ofFloat(view, "rotation", 360, 0);
                default -> animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
            }
            animators.add(animator);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(interpolator);
        if (listener != null)
            animatorSet.addListener(listener);
        return animatorSet;
    }

    private View[] getChildren(ViewGroup layout) {
        View[] views = new View[layout.getChildCount()];
        for (int i = 0; i < layout.getChildCount(); i++) {
            views[i] = layout.getChildAt(i);
        }
        return views;
    }

    public boolean isHidden(View view, FrameLayout scrollView) {
        Rect scrollBounds = new Rect();
        scrollView.getDrawingRect(scrollBounds);
        float top = view.getY();
        float bottom = top + view.getHeight();
        return scrollBounds.top > top && scrollBounds.bottom < bottom;
    }
}
