package com.softcare.raphnote

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.*

/// AppIntro2() //AppIntro is shipped with two top-level layouts that you can use. The default layout (AppIntro) has textual buttons, while the alternative layout has buttons with icons.
class AppGuide : AppIntro() {
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.ico_note_w,
                title = getString(R.string.slide_1_title),
                description = getString(R.string.slide_1_msg),
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.DKGRAY,
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.slide_2,
                title = getString(R.string.slide_2_title),
                description = getString(R.string.slide_2_msg),
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.MAGENTA
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.slide_02,
                title = getString(R.string.slide_02_title),
                description =    getString(R.string.slide_02_msg) ,
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.DKGRAY
            ))


        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.slide_3,
            title = getString(R.string.slide_3_title),
            description = getString(R.string.slide_3_msg) ,
                    titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.MAGENTA
        ))
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.slide_03,
                title = getString(R.string.slide_03_title),
                description = getString(R.string.slide_03_msg) ,
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.DKGRAY
            ))
        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.slide_4,
            title = getString(R.string.slide_4_title),
            description = getString(R.string.slide_4_msg),
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.MAGENTA
        )        )


        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.ico_note_w,
            title = getString(R.string.slide_6_title),
            description = getString(R.string.slide_6_msg),
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.DKGRAY
        ))
        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.slide_07,
            title = getString(R.string.slide_7_title),
            description = getString(R.string.slide_7_msg)  ,
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.MAGENTA
        ))
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.slide_7,
                title = getString(R.string.slide_7_title),
                description = getString(R.string.slide_7_msg)  ,
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.DKGRAY
            ))
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.ico_note_g,
                title = getString(R.string.slide_8_title),
                description = getString(R.string.slide_8_msg),
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.MAGENTA
            ))

        setTransformer(AppIntroPageTransformerType.Flow)
        isColorTransitionsEnabled = true
    }
/*

//all in on createe method
        addSlide(
            AppIntroFragment.newInstance(
                title = "The title of your slide",
                description = "A description that will be shown on the bottom",
                imageDrawable = R.drawable.a_note_w_small,
                backgroundDrawable = R.drawable.ic_launcher_background,
                titleColor = Color.YELLOW,
                descriptionColor = Color.RED,
                backgroundColor = Color.BLUE   //,
                // titleTypefaceFontRes = R.font.opensans_regular,
                // descriptionTypefaceFontRes = R.font.opensans_regular,
            )
        )
        AppIntroCustomLayoutFragment.newInstance(R.layout.activity_start) //f you need further control on the customization of your slide, you can use the AppIntroCustomLayoutFragment. This will allow you pass your custom Layout Resource file:
        // AppIntroFragment.newInstance(sliderPage: SliderPage)  //If you need to programmatically create several slides you can also use the SliderPage class. This class can be passed to AppIntroFragment.newInstance(sliderPage: SliderPage) that will create a new slide starting from that instance.
        addSlide(
            AppIntroFragment.newInstance(
                title = "..Let's get started!",
                description = "This is the last slide, I won't annoy you more :)"
            )
        )
*/
       // setTransformer(AppIntroPageTransformerType.Fade)
       // setTransformer(AppIntroPageTransformerType.Zoom)
      //  setTransformer(AppIntroPageTransformerType.SlideOver)
     //   setTransformer(AppIntroPageTransformerType.Depth)

// You can customize your parallax parameters in the constructors. 
    /*    setTransformer(  AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
            )
        )  */

     //   isColorTransitionsEnabled = true  //// disable by default


// Toggle Indicator Visibility                
        //isIndicatorEnabled = true

// Change Indicator Color 
       // setIndicatorColor(     selectedIndicatorColor = resources.getColor(  R.color.purple_200),
        //    unselectedIndicatorColor = resources.getColor(R.color.purple_500)   )

// Switch from Dotted Indicator to Progress Indicator
      //  setProgressIndicator()

// Supply your custom `IndicatorController` implementation
// ,,,,,indicatorController = MyCustomIndicator(/* initialize me */)

/*

<uses-permission android:name="android.permission.VIBRATE" />
You can enable and customize the vibration with:

// Enable vibration and set duration in ms
isVibrate = true
vibrateDuration = 50L  */




    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val s: SharedPreferences = this.getSharedPreferences(
            "RaphNote",
            MODE_PRIVATE
        )
        if(s.getBoolean("first",true)){
            val e=  s.edit()
            e.putBoolean("first", false)
            e.apply()
            e.commit()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val s: SharedPreferences = this.getSharedPreferences(
            "RaphNote",
            MODE_PRIVATE
        )
        if(s.getBoolean("first",true)){
            val e=  s.edit()
            e.putBoolean("first", false)
            e.apply()
            e.commit()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        finish()
    }
}