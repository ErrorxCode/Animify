
# ScrollAnimator ~ Animify library
The scrolling in the listview, recycler view, or scroll view has no
animation by default and is okay until the length of the scroll is not so big. From then, it gets boring.
Here is this amazing android library that will make scrolling as interesting as playing a game.


## Screenshots

https://user-images.githubusercontent.com/65817230/163135134-ed87325c-d520-4c97-b378-8e85d4a5694d.mp4

## Features

- Easy 2 use, builder pattern.
- Supports **ListView, RecyclerView and ScrollView**
- Supports **Fade** animation
- Supports **Sliding** animation
- Supports **Rotating** animation
- Supports **Dropping** animation



## Implementation

Add it in your root build.gradle (or gradle.settings) in latest android studio) at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```groovy
	dependencies {
	      implementation 'com.github.ErrorxCode:Animify:v1.0'
	}
```


## Usage/Examples

### Simple Usage

To animate a listview, recycler view, or scroll view







 (NestedScrollView too)
with default animation properties:
```java
ScrollAnimator.create().animate(view);  // view can be list/recycler/scroll View
```
This will animate the respected view with the default **Alpha** animation, 
**Fast** duration and **linear** interpolator.

***Note: for listviews and recycler views, this must be called after setting the adapter to it.***

### Advance Usage
To customize the animation, set the values using the builder pattern before
calling `animate()`

**Example:**
```java
ScrollAnimator.create()
        .withAnimation(Animations.ANIMATION_ALPHA)
        .withInterpolator(Animations.INTERPOLATOR_OVERSHOOT)
        .tillDuration(Animations.DURATION_FAST)
        .playOnlyOnDownScroll(true)
        .animate(view);
```
> *Tip: You can view documentation for each method by hovering the cursor in android studio*

You can also pass multiple animations in the `withAnimation()` method to merge different animations.

**Note:** If you are already using `onScrollChangeListener()` on your scrollview then, use
`animateOnScroll(int,int)` instead of `animate(Scrollview)`. Call this method in **`onScrollChange()`** of your
listener.

#### Custom animations

To use a custom property for animation instead of the library provided one, use `withCustom()` method
**Example**:
```java
ScrollAnimator.create()
        .withCustom("backgroundColor", Color.GREEN,Color.BLUE)
        ...                         // rest of the methods
        .animate(view);
```
here,
- 'background-color' is the name of the property. It is similar to [this](https://developer.android.com/reference/android/animation/ObjectAnimator#setPropertyName(java.lang.String)) method.
- Color.GREEN is the starting value from where to animate.
- Color.BLUE is the ending value till were animate.

This will result in the animation that will animate specified *property* from
*starting* value to *ending* value.


### Full Example

#### Using on RecyclerView/ListView
```java
listView.setAdapter(adapter);
ScrollAnimator.create()
        .withAnimation(Animations.ANIMATION_ALPHA)
        .withInterpolator(Animations.INTERPOLATOR_OVERSHOOT)
        .tillDuration(Animations.DURATION_FAST)
        .playOnlyOnDownScroll(true)
        .animate(litview);
```

#### Using on Scrollview/NestedScrollView
```java
NestedScrollView scrollView = findViewById(R.id.scrollview);

// just make sure that the scroll view is attached to the window, 
// or else call this animation inside scroll view post runnable.

ScrollAnimator.create()
        .withAnimation(Animations.ANIMATION_ALPHA)
        .withInterpolator(Animations.INTERPOLATOR_OVERSHOOT)
        .tillDuration(Animations.DURATION_FAST)
        .playOnlyOnDownScroll(true)
        .animate(scrollView);
```
*In case when the view is not attached and animation didn't worked*,
```java
scrollView.post(() -> {
    // animate scrollview here
})
```
#### Using with onScrollChangeListener
If your code already use `onScrollChangeListener()` on scrollview, then you must use
this approch to animate it.
**Example:**
```java
scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        ScrollAnimator.create()
                .withAnimation(Animations.ANIMATION_ALPHA)
                .withInterpolator(Animations.INTERPOLATOR_OVERSHOOT)
                .tillDuration(Animations.DURATION_FAST)
                .playOnlyOnDownScroll(true)
                .animateOnScroll(scrollView, scrollY, oldScrollY);
    }
});
```


## Contributing

Contributions are always welcome!

There is a scope for improvement in this library. What you can always do is
you can add more **animations** and **interpolators** to this library.
To do so, you just need to :-
- Add animation/interpolator constant to [Animation](https://github.com/ErrorxCode/Animify/blob/main/ScrollAnimator/src/main/java/com/xcoder/animator/Animations.java) class
- Add new case for the animation in switch-case ladder of [getAnimation()](https://github.com/ErrorxCode/Animify/blob/bb9e9e5e8066ed7a14bd3c4322048713e01ba87b/ScrollAnimator/src/main/java/com/xcoder/animator/ScrollAnimator.java#L262) method.


## That's it

If you liked my hard work, you can start this library.
Moreover, you can submit us your app link to make it
appear in the used by section.

