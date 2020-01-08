# ComplexView
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-ComplexView-green.svg?style=flat )]( https://android-arsenal.com/details/1/7786 )
\
\
A simple Android library that applies shadows of any color to views and allows easy manipulation of edges.
ComplexView now supports shadow positioning starting from v1.1

\
\
\
\
\
\
![Alt text](https://github.com/BluRe-CN/ComplexView/blob/master/screenshots/position.png "ComplexView sample")

# Gradle setup
``` gradle
repositories{
    maven { url 'https://jitpack.io' }
}

dependencies{

implementation 'com.github.BluRe-CN:ComplexView:v1.1'
    
}
```
# Maven setup

``` xml
<repositories>
  <repository>
    <id>jitpack.io</id>
     <url>https://jitpack.io</url>
  </repository>
</repositories>
    
<dependency>
  <groupId>com.github.BluRe-CN</groupId>
   <artifactId>ComplexView</artifactId>
   <version>Tag</version>
</dependency>
```
# Usage
![Alt text](https://github.com/BluRe-CN/ComplexView/blob/master/screenshots/position%20samples.png "ComplexView sample")



![Alt text](https://github.com/BluRe-CN/ComplexView/blob/master/screenshots/shadow.PNG "Shadow sample")

#### Xml
Code to achieve the above effect

``` xml
<com.blure.complexview.ComplexView
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:radius="50dp"
        app:shadow="true"
        app:shadowAlpha="100"
        app:shadowColor="#0061FF"
        app:shadowSpread="2">

        <com.blure.complexview.ComplexView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:color="#fdfcfc"
            app:radius="50dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true"
                android:layout_margin="5dp"
                android:text="Shadow" />
        </com.blure.complexview.ComplexView>
    </com.blure.complexview.ComplexView>
```
### Java
Code to achieve the above effect
``` java
        //Initialize parent as a shadow
        ComplexView shadow = new ComplexView(this);
        ComplexView.LayoutParams param = new ComplexView.LayoutParams(convertDpToPixel(100), convertDpToPixel(100));
        float[] radii = {50, 50, 50, 50, 50, 50, 50, 50};
        shadow.setShadow(new Shadow(2, 100, "#0061FF", GradientDrawable.RECTANGLE, radii));
        shadow.setLayoutParams(param);

        //Create holder for TextView
        ComplexView tvHolder = new ComplexView(this);
        ComplexView.LayoutParams tvHolderParam = new ComplexView.LayoutParams(ComplexView.LayoutParams.MATCH_PARENT,       ComplexView.LayoutParams.MATCH_PARENT);
        tvHolder.setRadius(50);
        tvHolderParam.addRule(CENTER_IN_PARENT);
        tvHolder.setColor(Color.parseColor("#fdfcfc"));
        tvHolder.setLayoutParams(tvHolderParam);

        //Create TextView object
        TextView tv = new TextView(this);
        ComplexView.LayoutParams tvParam = new ComplexView.LayoutParams(ComplexView.LayoutParams.WRAP_CONTENT, ComplexView.LayoutParams.WRAP_CONTENT);
        tvParam.addRule(CENTER_IN_PARENT);
        tvParam.setMargins(5, 5, 5, 5);
        tv.setLayoutParams(tvParam);
        tv.setText("Shadow");
        
        //Couple up
        tvHolder.addView(tv);
        shadow.addView(tvHolder);
        YourMainView.addView(shadow);

 ```
