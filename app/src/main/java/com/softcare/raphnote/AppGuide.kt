package com.softcare.raphnote

import android.annotation.SuppressLint
import android.content.Intent
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
                imageDrawable = R.drawable.a_note_w_small,
                title = "Welcome to 'A Note Editor'",
                description = "You can always come to this guide by selecting" +
                        " App Guide in options menu of Note List.",
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.DKGRAY,
                        backgroundDrawable = R.drawable.ic_launcher_background
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.a_note_w_small,  // note_list with option menu
                title = "Notes List (main view)",
                description = "Click on plus sign  to add note. " +
                        "Click on a note to  read. " +
                        "Click on search to search for not that contain the search type word. Click on three vertical dot(Option menu) for more options",
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.MAGENTA
            )
        )

        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.a_note_w_small,
            title = "Reading mode",
            description = "Click on pen sign  to edit note. "
                    +"Search for a word in a note. " +
                    "Share note as text. "+
                    "Copy note as text. Export note as text file"+
                    "Delete note." ,
                    titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.DKGRAY
        ))
        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.a_note_w_small,
            title = "Editing mode",
            description = "Click to right check to save and exit editing mode. " +
                    "Click on a on top left icon to stop editing(you will ask if you want to save changes. ",
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.MAGENTA
        )        )

        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.a_note_w_small,
            title = "Note List -->options menu",
            description =    "Share the App link to others. " +
                    "Open File to open a text file in reading mode."+
                    "Help is see App guide. "+
                    "Settings for security, sorting and ordering." ,
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.DKGRAY
        ))

        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.a_note_w_small,
            title = "Security Setting",
            description = "Select settings in notes option menu. " +
                    "Enable security by selecting the enable security. "+
                    "This will ask for finger print authentication, " +
                    "pin or pattern on start of note. "  ,
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.MAGENTA
        ))
        addSlide(
        AppIntroFragment.newInstance(
            imageDrawable = R.drawable.a_note_w_small,
            title = "Create note from other apps",
            description = "Select text in chrome or any ohter app the click share in the app. " +
                    "List of app will be prompt to you. "+
                    "Select 'A Note Editor' then it will take you to editing mode " +
                    "select check to save and continue with your action the other app. Or back if you intender to cancel the operation. "  ,
            titleColor = Color.YELLOW,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.DKGRAY
        ))



        addSlide(
            AppIntroFragment.newInstance(
                imageDrawable = R.drawable.a_note_w_small,
                title = ".... Let get started",
                description = "You can always reach us at raphaelraymondproduct@gmail.com." +
                        "Don't forget to send us feedback at above contact us. "  ,
                titleColor = Color.YELLOW,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.MAGENTA
            ))





/*

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
        setTransformer(AppIntroPageTransformerType.Flow)
      //  setTransformer(AppIntroPageTransformerType.SlideOver)
     //   setTransformer(AppIntroPageTransformerType.Depth)

// You can customize your parallax parameters in the constructors. 
    /*    setTransformer(  AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
            )
        )  */

        isColorTransitionsEnabled = true  //// disable by default


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

    }


    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
      //  startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        //startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}