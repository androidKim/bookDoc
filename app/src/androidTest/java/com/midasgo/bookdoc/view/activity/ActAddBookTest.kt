package com.midasgo.bookdoc.view.activity


import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.midasgo.bookdoc.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActAddBookTest {

    @Rule
    @JvmField
    var mActivityTestRule:ActivityTestRule<ActAddBook> = ActivityTestRule(ActAddBook::class.java)

    @Before
    fun startActivityTest(){

    }

    @Test
    fun inputBookDataTest(){
        Espresso.onView(withId(R.id.editTitle)).perform(typeText("title"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.editDesc)).perform(typeText("contents"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.editReadPage)).perform(typeText("0"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.editTotalPage)).perform(typeText("455"), closeSoftKeyboard())


        //입력값이 같은지 확인
        Espresso.onView(withId(R.id.editTitle)).check(matches(withText("title")))
    }
}
